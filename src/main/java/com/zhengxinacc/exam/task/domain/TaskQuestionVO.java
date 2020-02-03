/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.task.domain;

import com.zhengxinacc.exam.question.domain.Answer;
import lombok.*;

import java.util.List;

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
public class TaskQuestionVO {

	private String id;
	private String name;
	private Integer count;
	private Integer order;
}
