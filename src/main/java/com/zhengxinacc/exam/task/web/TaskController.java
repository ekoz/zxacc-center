/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.task.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zhengxinacc.system.user.domain.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhengxinacc.config.BaseController;
import com.zhengxinacc.exam.grade.domain.Grade;
import com.zhengxinacc.exam.paper.domain.Paper;
import com.zhengxinacc.exam.paper.service.PaperService;
import com.zhengxinacc.exam.task.domain.Task;
import com.zhengxinacc.exam.task.repository.TaskRepository;
import com.zhengxinacc.exam.task.service.TaskService;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年1月2日 下午7:44:44
 * @version 1.0
 */
@RestController
@RequestMapping("/exam/task")
public class TaskController extends BaseController {
	
	@Resource
	private PaperService paperService;
	@Resource
	private TaskRepository taskRepository;
	@Resource
	private TaskService taskService;
	
	/**
	 * 我的考试任务，求速度
	 * @author eko.zhan
	 * @param request
	 * @return
	 */
	@RequestMapping("/loadList")
	public JSONObject loadList(HttpServletRequest request){
		List<Paper> paperList = paperService.findByUser(getCurrentUser(request));
		if (paperList==null){
		    paperList = new ArrayList<>();
        }
		
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("message", "");
		result.put("count", paperList.size());
		
		JSONArray dataArr = new JSONArray();
		for (Paper paper : paperList){
			paper.setGrades(null);
			paper.setQuestionList(null);
			paper.setQuestions(null);
			JSONObject tmp = (JSONObject)JSONObject.toJSON(paper);
			Task task = taskRepository.findByPaperAndCreateUser(paper, getUsername(request));
			if (task!=null){
                // 判断用户是否考试完毕，只用到了 status 字段
				task.setPaper(null);
				task.setQuestionList(null);
				task.setQuestions(null);
				tmp.put("task", task);
			} else {
				task = new Task();
				task.setId("0");
				task.setStatus(0);
				tmp.put("task", task);
			}
			dataArr.add(tmp);
		}
		
		result.put("data", dataArr);
		
		return result;
	}
	
	@RequestMapping("/loadMyList")
	public JSONObject loadMyList(HttpServletRequest request){
		List<Task> list = taskRepository.findByCreateUser(getUsername(request));
		
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("message", "");
		result.put("count", list.size());
		
		JSONArray dataArr = new JSONArray();
		for (Task task : list){
			JSONObject tmp = (JSONObject)JSONObject.toJSON(task);
			tmp.put("createDate", DateFormatUtils.format(task.getCreateDate(), "yyyy-MM-dd HH:mm"));
			tmp.put("modifyDate", DateFormatUtils.format(task.getModifyDate(), "yyyy-MM-dd HH:mm"));
			if (task.getStatus()==1){
				tmp.put("statusDesc", "已完成");
			}else{
				tmp.put("statusDesc", "考试中");
			}
			tmp.put("paperName", task.getPaper().getName());
			dataArr.add(tmp);
		}
		
		result.put("data", dataArr);
		
		return result;
	}
	
	/**
	 * 查阅试卷，只读
	 * @author eko.zhan at 2018年1月6日 下午2:36:30
	 * @param param
	 */
	@RequestMapping("/loadTask")
	public Task loadTask(HttpServletRequest request, @RequestBody JSONObject param){
		Task task = taskRepository.findOne(param.getString("taskId"));

		// 如果是理工的学生，等到试卷停用后才能看到答案 task.getPaper().getDelFlag()==1
        User user = getCurrentUser(request);
        if (user.getUsername().length()==6 && user.getUsername().startsWith("1708") && task.getPaper().getDelFlag()==0){
            // 学号 170802 是指理工的学生
        }else{
            task = taskService.setQuestionList(task);
        }
        return task;
	}

    /**
     * 判断当前试卷是否完成
     * @param taskId
     * @return
     */
	@GetMapping("isFinish")
    public JSONObject isFinish(String taskId){
        Task task = taskRepository.findOne(taskId);
        return writeSuccess(task==null?"0":task.getStatus().toString());
    }
}
