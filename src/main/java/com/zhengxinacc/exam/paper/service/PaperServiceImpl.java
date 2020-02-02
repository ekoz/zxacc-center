/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhengxinacc.exam.task.domain.Task;
import com.zhengxinacc.exam.task.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhengxinacc.exam.grade.domain.Grade;
import com.zhengxinacc.exam.grade.repository.GradeRepository;
import com.zhengxinacc.exam.grade.service.GradeService;
import com.zhengxinacc.exam.paper.domain.Paper;
import com.zhengxinacc.exam.paper.domain.PaperQuestion;
import com.zhengxinacc.exam.paper.repository.PaperRepository;
import com.zhengxinacc.exam.question.domain.Question;
import com.zhengxinacc.exam.question.repository.QuestionRepository;
import com.zhengxinacc.exam.task.domain.TaskQuestion;
import com.zhengxinacc.system.user.domain.User;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月30日 下午8:07:09
 * @version 1.0
 */
@Service
public class PaperServiceImpl implements PaperService {

	@Resource
	private PaperRepository paperRepository;
	@Resource
	private GradeRepository gradeRepository;
	@Resource
	private GradeService gradeService;
	@Resource
	private QuestionRepository questionRepository;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
    private TaskRepository taskRepository;
	
	@Override
	public Paper save(JSONObject data) {
		String id = data.getString("id");
		Paper paper = null;
		if (StringUtils.isBlank(id)){
			paper = new Paper();
			paper.setCreateUser(data.getString("username"));
		}else{
			paper = paperRepository.findOne(id);
		}
		paper.setModifyUser(data.getString("username"));
		paper.setName(data.getString("name"));
		if (data.get("startDate")!=null){
			try {
				paper.setStartDate(DateUtils.parseDate(data.getString("startDate"), "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (data.get("endDate")!=null){
			try {
				paper.setEndDate(DateUtils.parseDate(data.getString("endDate"), "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		paper.setLimit(Integer.valueOf(data.getString("limit")));
		paper.setTotal(Integer.valueOf(data.getString("total")));
		
		JSONArray gradeArr = JSON.parseArray(data.getString("gradeIds"));
		List<Grade> gradeList = new ArrayList<Grade>();
		for (Object obj : gradeArr){
			String gradeId = (String)obj;
			Grade grade = gradeRepository.findOne(gradeId);
			if (grade!=null){
				gradeList.add(grade);
			}
		}
		paper.setGrades(gradeList);
		
		JSONArray arr = JSON.parseArray(data.getString("questions"));
		//System.out.println(arr);
		Map<String, PaperQuestion> map = new HashMap<String, PaperQuestion>();
		for (Object obj : arr){
			JSONObject json = (JSONObject) obj;
			Question ques = questionRepository.findOne(json.getString("id"));
			PaperQuestion question = new PaperQuestion(json.getString("id"), ques.getName(), ques.getType(), Integer.valueOf(json.getString("order")), Double.parseDouble(json.getString("score")));
			map.put(json.getString("id"), question);
		}
		paper.setQuestions(map);

		return paperRepository.save(paper);
	}

	@Override
	public List<Paper> findByUser(User user) {
		List<User> users = Arrays.asList(new User[]{user});
		List<Grade> gradeList = gradeRepository.findByUsersIn(users);
		
		Query query = new Query(Criteria.where("delFlag").is(0)
				.andOperator(Criteria.where("grades").in(gradeList)));
		return mongoTemplate.find(query, Paper.class);
		
		//TODO 为什么用下面这个方法无法获取返回值
//		return paperRepository.findByGradesIn(gradeList);
	}

	@Override
	public Paper setQuestionList(Paper paper) {
		Map<String, PaperQuestion> questions = paper.getQuestions();
		List<Map.Entry<String, PaperQuestion>> list = new ArrayList<Map.Entry<String, PaperQuestion>>(questions.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, PaperQuestion>>() {
			@Override
			public int compare(Map.Entry<String, PaperQuestion> o1, Map.Entry<String, PaperQuestion> o2) {
				PaperQuestion q1 = o1.getValue();
				PaperQuestion q2 = o2.getValue();
				if (q1.getOrder()!=null && q2.getOrder()!=null){
					return q1.getOrder().compareTo(q2.getOrder());
				}
				return 0;
			}
		});
		paper.setQuestionList(list);
		return paper;
	}

	@Override
	public Page<Paper> findAll(Integer page, Integer size, JSONObject data, Direction desc) {
		String property = data.getString("property");
		String keyword = data.getString("keyword");
		Order order = new Order(desc, property);
		Pageable pageable = new PageRequest(page-1, size, new Sort(order));
		
		if (StringUtils.isBlank(keyword)){
			return paperRepository.findAll(pageable);
		}else{
			return paperRepository.findByNameLike(keyword, pageable);
		}
	}

	@Override
	public void copy(String id) {
		/*
		 * 步骤
		 * 1、复制试卷
		 * 2、复制班级
		 * 3、复制题目
		 */
		Paper oPaper = paperRepository.findOne(id);
	}

	@Override
	public XSSFWorkbook exportTask(String paperId) {
		Paper paper = paperRepository.findOne(paperId);
		List<Task> list = taskRepository.findByPaper(paper, 1);


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("成绩");
        row.createCell(2).setCellValue("状态");

        int i=1;
        for (Task task : list){
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(task.getCreateUser());
            row.createCell(1).setCellValue(task.getScore()==null?0.0:task.getScore());
            row.createCell(2).setCellValue(task.getStatus()==1?"已完成":"考试中");
            i++;
        }

        return workbook;
	}
}
