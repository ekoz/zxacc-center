/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhengxinacc.exam.question.domain.Answer;
import lombok.*;

import java.util.List;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年1月3日 下午9:22:29
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PaperQuestion {
	/**
	 * 对应问题主键id
	 */
	private String id;
	private String name;
	private Integer type;
	private Integer order = 0;
	private Double score;

    /**
     * 打印试卷时需要带上答案
     */
    @Setter
    @JsonIgnore
    @JSONField(serialize=false)
    private List<Answer> answers;
}
