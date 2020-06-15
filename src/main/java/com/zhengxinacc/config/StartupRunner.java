/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.config;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhengxinacc.exam.paper.domain.Paper;
import com.zhengxinacc.exam.paper.repository.PaperRepository;
import com.zhengxinacc.exam.question.domain.QuestionCate;
import com.zhengxinacc.exam.question.repository.QuestionCateRepository;
import com.zhengxinacc.system.permission.domain.Permission;
import com.zhengxinacc.system.permission.repository.PermissionRepository;
import com.zhengxinacc.system.role.domain.Role;
import com.zhengxinacc.system.role.repository.RoleRepository;
import com.zhengxinacc.util.SystemKeys;
import org.springframework.util.StopWatch;

/**
 * <pre class="code">
 * 注意 spring quartz 和 schedule 的区别
 * &#064;Configuration 和 &#064;Component 的区别 https://blog.csdn.net/isea533/article/details/78072133
 * </pre>
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年1月3日 上午9:55:12
 * @version 1.0
 */
@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {

	@Resource
	private QuestionCateRepository questionCateRepository;
	@Resource
	private PermissionRepository permissionRepository;
	@Resource
	private RoleRepository roleRepository;
	
	@Resource
	private PaperRepository paperRepository;
	
	
	/**
	 * 每天凌晨2点执行试卷有效期检查
	 * @author eko.zhan at 2018年5月6日 下午5:29:57
	 */
	@Scheduled(cron="0 0 2 * * ?")
	//@Scheduled(cron="0/5 * * * * ?") //测试用
	public void validPaper(){
		List<Paper> list = paperRepository.findByEndDateLessThan(new Date());
		list.forEach(paper -> {
			paper.setDelFlag(1);
			paperRepository.save(paper);
		});
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		//初始化数据
		//题目分类为空时，创建相应的题目分类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
		List<QuestionCate> list = questionCateRepository.findAll();
		if (list.size()==0){
			QuestionCate questionCate = saveQuestionCate("初级会计实务", "ROOT");
			saveQuestionCate("单选题", questionCate.getId());
			saveQuestionCate("多选题", questionCate.getId());
			saveQuestionCate("判断题", questionCate.getId());
			
			questionCate = saveQuestionCate("经济法基础", "ROOT");
			saveQuestionCate("单选题", questionCate.getId());
			saveQuestionCate("多选题", questionCate.getId());
			saveQuestionCate("判断题", questionCate.getId());
		}
		
		//创建权限
		List<Permission> pList = permissionRepository.findAll();
		if (pList.size()==0){
			savePermission("管理员", "ADMIN");
			savePermission("普通用户", "USER");
			savePermission("考试管理员", "EXAM");
		}
		//创建角色
		List<Role> rList = roleRepository.findAll();
		if (rList.size()==0){
			saveRole("系统管理员", "SYS_ADMIN");
			saveRole("考试管理员", "SYS_EXAM");
		}
		stopWatch.stop();
        System.out.println(stopWatch);
	}
	
	private void saveRole(String name, String key) {
		Role role = new Role();
		role.setName(name);
		role.setKey(key);
		role.setCreateUser(SystemKeys.ANONYMOUS);
		role.setModifyUser(SystemKeys.ANONYMOUS);
		roleRepository.save(role);
	}

	private void savePermission(String name, String key) {
		Permission permission = new Permission();
		permission.setName(name);
		permission.setKey(key);
		permission.setCreateUser(SystemKeys.ANONYMOUS);
		permission.setModifyUser(SystemKeys.ANONYMOUS);
		permissionRepository.save(permission);
	}
	/**
	 * 保存 QuestionCate
	 * @author eko.zhan at 2018年1月3日 上午10:11:35
	 * @param name
	 * @param pid
	 * @return
	 */
	private QuestionCate saveQuestionCate(String name, String pid){
		QuestionCate cate = new QuestionCate();
		cate.setName(name);
		cate.setCreateUser(SystemKeys.ANONYMOUS);
		cate.setModifyUser(SystemKeys.ANONYMOUS);
		cate.setPid(pid);
		return questionCateRepository.save(cate);
	}

}
