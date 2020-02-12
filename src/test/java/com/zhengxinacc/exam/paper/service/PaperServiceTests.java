/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.service;

import com.zhengxinacc.ZxaccCenterApplication;
import com.zhengxinacc.exam.paper.domain.Paper;
import com.zhengxinacc.exam.paper.repository.PaperRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.Charsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.Locale;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan>/a>
 * @version 1.0
 * @since 2020-2-10 17:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ZxaccCenterApplication.class)
public class PaperServiceTests {

    @Resource
    PaperService paperService;
    @Resource
    PaperRepository paperRepository;

    @Test
    public void print() throws IOException, TemplateException {
        Paper paper = paperRepository.findOne("5e3e56bfce0fbf1af4a67f0a");
        System.out.println(paperService.printWord(paper, 1));
    }

}
