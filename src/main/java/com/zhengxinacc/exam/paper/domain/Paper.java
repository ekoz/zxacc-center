/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zhengxinacc.config.BaseBean;
import com.zhengxinacc.exam.grade.domain.Grade;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月30日 上午10:43:09
 * @version 1.0
 */
@Document(collection="exam_paper")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Paper extends BaseBean {

	@Id
	private String id;
	private String name;
	/**
	 * 试卷时长（分钟）
	 */
	private Integer limit;
	/**
	 * 试卷总分
	 */
	private Integer total;

	/**
	 * 考试发布的班级
	 */
	@DBRef
	private List<Grade> grades;
	/**
	 * 登录的学员只能查阅在考试有效时间内的试卷
	 * 考试有效时间-开始
	 */
	private Date startDate;
	/**
	 * 考试有效时间-结束
	 */
	private Date endDate;
    /**
     * {"score":"10","id":"5a4c6f378149763070f067cf","order":""},{"score":"10","id":"5a4c6f3e8149763070f067d0","order":""}
     */
	private Map<String, PaperQuestion> questions;
	@Transient
	List<Map.Entry<String, PaperQuestion>> questionList;
    /**
     * 考试结束后是否立即显示答案，选择0，那么会在所有考试结束后才显示答案
     * 0-不显示；1-立即显示
     */
	private Integer displayTofAtReply;
	
}