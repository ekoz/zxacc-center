/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.task.domain;

import java.util.List;

import lombok.*;

import com.zhengxinacc.exam.question.domain.Answer;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年1月3日 下午9:39:27
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskQuestion {

	/**
	 * 对应问题主键id
	 */
	private String id;
	private String name;
	private Integer type;
	private Integer order = 0;
	private Double score;
    /**
     * 最终得分
     */
	private Double finalScore;
    /**
     * 是否回答正确？
     */
	private Boolean finalTof = Boolean.FALSE;
	/**
	 * 记录判断题正确答案
	 */
	private Boolean key;
	/**
	 * 记录判断题答题答案
	 */
	private Boolean keyMark = Boolean.FALSE;
	/**
	 * 标识当前是答卷中的问题
	 */
	private Boolean isReply = Boolean.TRUE;
	/**
	 * 根据是否有  question 对象，判断是否答题
	 */
	private List<Answer> answers;
}
