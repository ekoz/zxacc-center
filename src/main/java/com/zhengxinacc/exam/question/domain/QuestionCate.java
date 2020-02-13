/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.question.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zhengxinacc.config.BaseBean;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月24日 上午9:59:11
 * @version 1.0
 */
@Document(collection="exam_question_cate")
@Getter
@Setter
@ToString
public class QuestionCate extends BaseBean {

	@Id
	private String id;
	private String pid;
	private String name;
	@DBRef
	private List<Question> questions;
}
