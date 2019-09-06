package robot.qa.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import robot.qa.bean.Answer;
import robot.qa.dao.AnswerRepository;

@Controller
public class SampleController {

    // 自动注入，相当于自动为我们new一个对象
    @Autowired
    private AnswerRepository answerRepository;

    @RequestMapping("/") // 声明URL
    public String h() {
        return "index";
    }

    @RequestMapping("/home") // 声明URL
    public String home() {
        return "home";
    }

    // 在使用@RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接返回字符串
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(String keyword) {
        // 用于接收返回信息的变量
        String result = null;

        if (result == null) {
            // 判断用户是否退出
            result = logout(keyword);
        }
        // 当用户没退出时
        if (result == null) {
            // 正常回答
            result = normal(keyword);
        }
        if (result == null) {
            // 未找到答案，3遍录入功能
            result = notFindAnswer(keyword);
        }

        sleep(500);

        return result;
    }

    /** 用户上一步想退出的状态，用于标记用户上一步是否输入退出 */
    private static boolean wantLogout = false;// 初始化

    /** 确认用户退出 */
    private String logout(String keyword) {
        String result = null;
        if (wantLogout) {
            if ("y".equalsIgnoreCase(keyword)) {
                result = "退出";
            } else {
                result = "谢谢你能留下来和我聊天";
            }
            // 如果用户不想退出或者已退出，那么logout重置。
            wantLogout = false;
        } else {
            if ("退出".equals(keyword) || "再见".equals(keyword)) {
                // 如果用户输入'退出'则提示，并且标记logout为用户想退出
                wantLogout = true;// 下次用户输入的时候就会走上边的if
                result = "真的要退出吗 Y/N";
            }
        }
        return result;
    }

    /** 正常回答 */
    private String normal(String keyword) {
        String result = null;
        List<Answer> answers = answerRepository.findByKeyword(keyword);
        if (answers != null && answers.size() > 0) {
            // 如果数据库已有答案，则随机选取一个进行回答
            Random r = new Random();
            int i = r.nextInt(answers.size());
            result = answers.get(i).getValue();
            System.out.println("找到答案，‘" + keyword + "’的答案是：" + result);
        }
        return result;
    }

    /** 未知答案的统计 */
    private static Map<String, Integer> unknowKeyWord = new HashMap<String, Integer>();

    /** 当前keyword 是否为答案 */
    private static boolean nowKeyWordIsAnswer = false;

    /** 找不到答案 */
    private String notFindAnswer(String keyword) {
        String result = null;

        // 遍历看看是否有等于2的keyword（即用户问了2遍同样问题都不知道答案的情况）
        Set<String> set = unknowKeyWord.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (unknowKeyWord.get(key) == 2) {
                if (nowKeyWordIsAnswer) {
                    // 保存keyword以及对应的答案
                    Answer answer = new Answer();
                    answer.setCreateTime(new Date());
                    answer.setKeyword(key);
                    answer.setValue(keyword);
                    answerRepository.save(answer);

                    // 已记录该答案，则从未知keyword中移除
                    unknowKeyWord.remove(key);

                    result = "我已经记住了，不信你可以再问我一遍试试。";
                    // 已经保存答案，切换状态
                    nowKeyWordIsAnswer = false;
                } else {
                    result = "你那么强大，就告诉我刚刚那个问题的答案呗！";
                    // 第一次map 的value 为2，切换状态使下次请求的keyword成为answer保存。
                    nowKeyWordIsAnswer = true;
                }
                break;
            }
        }
        if (result == null) {
            result = unknow(keyword);
        }

        return result;
    }

    /**
     * 前两遍未知问题回答
     * 
     * @param keyword
     * @return
     */
    private String unknow(String keyword) {
        Integer count = unknowKeyWord.get(keyword);// 以前输入过几次
        count = count == null ? 0 : count;
        unknowKeyWord.put(keyword, ++count);
        String result = "我听不懂你说什么";
        return result;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
