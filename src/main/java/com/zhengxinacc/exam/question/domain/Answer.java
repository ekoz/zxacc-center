/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.question.domain;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月24日 上午10:03:11
 * @version 1.0
 */
@Document(collection="exam_answer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Answer {

	@Id
	private String id;
    /**
     * 答案描述
     */
	private String name;
    /**
     * 暂无实际用途
     */
	private String value;
    /**
     * true-正确答案 false-错误答案 答案默认都是错误
     * // @JSONField(serialize=false) 答案不应该抛到前端
     */
	private Boolean key = Boolean.FALSE;
	@JsonIgnore
	@JSONField(serialize=false)
	@DBRef
	private Question question;
    /**
     * 用户答题时是否选择该值，如果选择则为true
     */
	private Boolean mark = Boolean.FALSE;
}
