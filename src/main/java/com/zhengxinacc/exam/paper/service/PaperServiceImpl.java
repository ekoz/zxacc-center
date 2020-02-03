/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.zhengxinacc.exam.question.domain.Answer;
import com.zhengxinacc.exam.task.domain.Task;
import com.zhengxinacc.exam.task.domain.TaskQuestionVO;
import com.zhengxinacc.exam.task.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
		Map<String, PaperQuestion> map = new HashMap<>();
		for (Object obj : arr){
			JSONObject json = (JSONObject) obj;
			Question ques = questionRepository.findOne(json.getString("id"));
			PaperQuestion question = PaperQuestion
                    .builder()
                    .id(json.getString("id"))
                    .name(ques.getName())
                    .type(ques.getType())
                    .order(Integer.valueOf(json.getString("order")))
                    .score(Double.parseDouble(json.getString("score")))
                    .build();
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
		List<Task> list = taskRepository.findByPaper(paper);

		// 错题集合
		Map<String, TaskQuestionVO> errQuesMap = new HashMap<>();

        List<Grade> gradeList = paper.getGrades();
        // 所有待考试人员
        Set<String> allUserSet = new HashSet<>();
        // 所有待考试人员 Map
		Map<String, String> allUserMap = new HashMap<>();
        // 所有参加考试人员
        Set<String> taskUserSet = new HashSet<>();
        if (gradeList!=null && gradeList.size()>0){
            gradeList.forEach(grade -> {
                grade
						.getUsers()
						.forEach(user -> {
							allUserSet.add(user.getUserInfo().getUsername());
							allUserMap.put(user.getUserInfo().getUsername(), user.getUsername());
						});
            });
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("学员成绩");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("学号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("成绩");
        row.createCell(3).setCellValue("状态");

        int i=1;
        for (Task task : list){
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(allUserMap.get(task.getCreateUser()));
            row.createCell(1).setCellValue(task.getCreateUser());
            row.createCell(2).setCellValue(task.getScore()==null?0.0:task.getScore());
            row.createCell(3).setCellValue(task.getStatus()==1?"已完成":"考试中");
            i++;
            // 已经考试的人员名单
            taskUserSet.add(task.getCreateUser());
            //
            task.getQuestions().forEach((key, taskQuestion) -> {
                if (taskQuestion.getType()==0){
                    //单选题
                    taskQuestion.getAnswers().forEach(answer -> {
                        if (answer.getKey() && !answer.getKey().equals(answer.getMark())){
                            setErrQuesMap(errQuesMap, taskQuestion);
                        }
                    });
                }else if (taskQuestion.getType()==1){
                    //多选题
                    Boolean b = true;
                    Iterator<Answer> iterator = taskQuestion.getAnswers().iterator();
                    while (iterator.hasNext()){
                        Answer answer = iterator.next();
                        if (answer.getKey() && answer.getKey().equals(answer.getMark())){
                            b = false;
                        }else if (!answer.getKey() && answer.getKey().equals(answer.getMark())){
                            b = false;
                        }
                    }
                    if (!b){
                        setErrQuesMap(errQuesMap, taskQuestion);
                    }
                }else if (taskQuestion.getType()==2){
                    //判断题
                    if (!taskQuestion.getKey().equals(taskQuestion.getKeyMark())){
                        setErrQuesMap(errQuesMap, taskQuestion);
                    }
                }
            });
        }

        // 未参加考试的人员，成绩为0，状态是未开始
        List<String> missUserList = allUserSet.stream()
                .filter(username -> !taskUserSet.contains(username))
                .collect(Collectors.toList());

        if (missUserList.size()>0){
            for (String username : missUserList){
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(allUserMap.get(username));
                row.createCell(1).setCellValue(username);
                row.createCell(2).setCellValue(0.0);
                row.createCell(3).setCellValue("未参加");
                i++;
            }
        }

        // 新起一个 sheet 页，输出错题集
        List<TaskQuestionVO> errQuesList = errQuesMap.values()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getCount() < o2.getCount()) {
                        return 1;
                    }
                    return -1;
                })
                .limit(100)
                .collect(Collectors.toList());
        if (errQuesList.size()>0){
            sheet = workbook.createSheet("常错题集");
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("题序");
            row.getCell(0).getCellStyle().setHidden(Boolean.TRUE);
            row.createCell(1).setCellValue("常错题");
            row.createCell(2).setCellValue("出错数");

            i = 1;
            for (TaskQuestionVO vo : errQuesList){
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(vo.getOrder());
                row.createCell(1).setCellValue(vo.getName());
                row.createCell(2).setCellValue(vo.getCount());
                i++;
            }
        }

        return workbook;
	}

    /**
     * 错题集合
     * @param errQuesMap
     * @param taskQuestion
     */
	private void setErrQuesMap(Map<String, TaskQuestionVO> errQuesMap, TaskQuestion taskQuestion){
        TaskQuestionVO vo = errQuesMap.get(taskQuestion.getId());
        if (vo==null){
            errQuesMap.put(taskQuestion.getId(), TaskQuestionVO.builder()
                    .id(taskQuestion.getId())
                    .name(taskQuestion.getName())
                    .order(taskQuestion.getOrder())
                    .count(1)
                    .build());
        }else{
            vo.setCount(vo.getCount()+1);
            errQuesMap.put(taskQuestion.getId(), vo);
        }
    }
}
