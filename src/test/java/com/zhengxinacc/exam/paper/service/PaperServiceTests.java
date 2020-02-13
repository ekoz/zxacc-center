/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.exam.paper.service;

import com.zhengxinacc.ZxaccCenterApplication;
import com.zhengxinacc.exam.paper.domain.Paper;
import com.zhengxinacc.exam.paper.repository.PaperRepository;
import com.zhengxinacc.exam.question.domain.Answer;
import com.zhengxinacc.exam.question.domain.Question;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.Charsets;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan>/a>
 * @version 1.0
 * @since 2020-2-10 17:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ZxaccCenterApplication.class)
public class PaperServiceTests {

    static final String YES = "√";
    static final String NO = "×";

    @Resource
    PaperService paperService;
    @Resource
    PaperRepository paperRepository;

    @Test
    public void print() throws IOException, TemplateException {
        Paper paper = paperRepository.findOne("5e3e56bfce0fbf1af4a67f0a");
        System.out.println(paperService.printWord(paper, 1));
    }

    @Test
    public void importWord() throws IOException {
        File file = new File("D://paper.doc");
        if (file.exists()){
            System.out.println(file.getAbsolutePath());
        }
        HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
        WordExtractor extractor = new WordExtractor(doc);
        String[] contextArray = extractor.getParagraphText();
        List<String> list = Arrays.asList(contextArray);
        StringBuffer sb = new StringBuffer();
        list.forEach(s -> sb.append(s));
        String result = sb.toString();
        List<Question> questionList = new ArrayList<>();
//        System.out.println(result);
        for (int i=0;i<list.size();){
            String s = list.get(i);
//            System.out.println(s);
            System.out.println("==========================");
            Pattern pattern = Pattern.compile("^(\\d+)\\.(.*)");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()){
//                System.out.println(matcher.group(0));
//                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
                Integer order = Integer.valueOf(matcher.group(1));
                String questionStr = matcher.group(2);
                Question question = Question.builder()
                        .name(questionStr)
                        .build();
                // 从问题往下找，找答案
//                System.out.println(order);
                if (order<=40){
                    // 单选和多选
                    String ans = list.get(++i);
                    matcher = pattern.matcher(ans);
                    List<Answer> ansList = new ArrayList<>();
                    while (true){
                        if (matcher.find()) {
                            question.setAnswers(ansList);
                            if (order<=30){
                                // 单选题
                                question.setType(0);
                            }else{
                                // 多选题
                                question.setType(1);
                            }
                            questionList.add(question);
                            break;
                        }else{
                            //
//                            System.out.println(ans);
                            if (ans.contains("D.")){
                                // ABCD 在一行 或者 AB CD 两行中的CD行
                                if (ans.contains("A.")){
                                    // ABCD 在一行
                                    String[] arr = new String[]{"A.", "B.", "C.", "D."};
                                    setAnswerList(arr, ans, ansList);
                                }else if (ans.contains("C.") && ans.contains("D.")){
                                    // CD 行
                                    String[] arr = new String[]{"C.", "D."};
                                    setAnswerList(arr, ans, ansList);
                                }
                            }else if (ans.contains("A.") && ans.contains("B.")){
                                //AB CD 两行
                                String[] arr = new String[]{"A.", "B."};
                                setAnswerList(arr, ans, ansList);
                            }else{
                                // ABCD 四行
                                ansList.add(Answer.builder().name(ans.substring(2)).build());
                            }
                            ans = list.get(++i);
                            matcher = pattern.matcher(ans);
                        }
                    }
                }else{
                    i++;
                    // 判断题
                    question.setType(2);
                    questionList.add(question);
                }
            }else {
                i++;
            }

        }
        System.out.println(questionList);

        extractor.close();
        doc.close();
    }

    /**
     * 设置答案
     * @param segArr ["A.", "B.", "C.", "D."]
     * @param ans
     * @param ansList
     */
    private void setAnswerList(String[] segArr, String ans, List<Answer> ansList){
        for (int seg=1;seg<segArr.length;seg++){
            String segStr = segArr[seg];
            String tmp = ans.substring(2, ans.indexOf(segStr)).trim();
            appendAnswer(tmp, ansList);

            ans = ans.substring(ans.indexOf(segStr)+2);
            if (seg+1==segArr.length){
                appendAnswer(ans, ansList);
            }
        }
    }
    /**
     * 设置正确答案
     * @param ans
     * @param ansList
     */
    private void appendAnswer(String ans, List<Answer> ansList){
        if (ans.endsWith(YES)){
            ans = ans.substring(0, ans.length()-1);
            ansList.add(Answer.builder().name(ans).key(true).build());
        }else{
            ansList.add(Answer.builder().name(ans).build());
        }
    }

}
