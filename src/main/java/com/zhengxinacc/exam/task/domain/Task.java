/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.task.domain;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zhengxinacc.config.BaseBean;
import com.zhengxinacc.exam.paper.domain.Paper;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年1月3日 下午8:46:28
 * @version 1.0
 */
@Document(collection="exam_task")
@Getter
@Setter
public class Task extends BaseBean {

	@Id
	private String id;
	@DBRef
	private Paper paper;
	private Double score;
	/**
	 * 当前用户已用时，单位秒
	 */
	private Integer limit;
	private Map<String, TaskQuestion> questions;
	/**
	 * 0-草稿状态 1-提交
	 */
	private Integer status = 0;
	@Transient
	List<Map.Entry<String, TaskQuestion>> questionList;
}
