package com.wisdom.iwcs.service.task.template;

import com.wisdom.iwcs.common.utils.exception.BusinessException;
import com.wisdom.iwcs.domain.task.MainTask;
import com.wisdom.iwcs.domain.task.SubTask;
import com.wisdom.iwcs.domain.task.SubTaskTyp;
import com.wisdom.iwcs.mapper.task.MainTaskMapper;
import com.wisdom.iwcs.mapper.task.SubTaskMapper;
import com.wisdom.iwcs.mapper.task.SubTaskTypMapper;
import com.wisdom.iwcs.mapper.task.TaskRelConditionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送消息体注入信息类
 * @author han
 */
@Service
public class TemplateRelatedServer {

    private Pattern pattern = Pattern.compile(REGIX);
    /**
     * 正则表达式
     */
    private final static String REGIX = "\\$\\{[^\\}]*}";
    /**
     * 选填项
     */
    private final String OP = "OP";
    /**
     * 选填项
     */
    private final String NC = "NC";
    /**
     * 表示子任务类
     */
    private final String SUB_TASK = "SubTask";
    /**
     * 表示主任务类
     */
    private final String MAIN_TASK = "MainTask";

    @Autowired
    SubTaskMapper subTaskMapper;
    @Autowired
    MainTaskMapper mainTaskMapper;
    @Autowired
    SubTaskTypMapper subTaskTypMapper;
    @Autowired
    TaskRelConditionMapper taskRelConditionMapper;

    /**
     * 根据子任务编号,把子任务的信息插入到子任务对应的发送消息体中
     * @param subTaskNum 子任务编号
     * @return 加入参数后的发送消息体
     */
    public String templateIntoInfo(String subTaskNum) throws InvocationTargetException, IllegalAccessException {
        //1.查询子任务信息
        if (StringUtils.isEmpty(subTaskNum)) {
            throw new BusinessException("tempateIntoInfo()的参数不能为空");
        }
        SubTask subTask = subTaskMapper.selectBySubTaskNum(subTaskNum);
        checkNull(subTask);

        //2.查询发送消息体
        if (StringUtils.isEmpty(subTask.getSubTaskTyp())) {
            throw new BusinessException("数据异常: 子任务无任务类型");
        }
        SubTaskTyp subTaskTyp = subTaskTypMapper.selectByTypeCode(subTask.getSubTaskTyp());
        checkNull(subTaskTyp);
        String sendTemplate = subTaskTyp.getSubTaskMesSend();
        //仅测试使用
//        sendTemplate = "{"a": ${OP.SubTask.remark}, "b": ${NC.SubTask.appCode}}";


        //3.查询子任务对应的主任务信息
        if (StringUtils.isEmpty(subTask.getMainTaskNum())) {
            throw new BusinessException("数据异常: 子任务无主任务编号");
        }
        MainTask mainTask = mainTaskMapper.selectByMainTaskNum(subTask.getMainTaskNum());
        checkNull(mainTask);

        //4.向发送消息体中插入消息
        while(true) {
            String[] values = gainTemplateValue(sendTemplate);
            if (values == null) {
                break;
            }
            if (values.length != 3) {
                throw new BusinessException("发送消息体格式错误");
            }
            Object param = null;
            //拼接get方法名
            String methodName = "get" + values[2].substring(0, 1).toUpperCase() + values[2].substring(1);
            try {
                if (SUB_TASK.equals(values[1])) {
                    Method declaredMethod = SubTask.class.getDeclaredMethod(methodName);
                    param = declaredMethod.invoke(subTask);
                } else if (MAIN_TASK.equals(values[1])) {
                    Method declaredMethod = MainTask.class.getDeclaredMethod(methodName);
                    param = declaredMethod.invoke(mainTask);
                }
                if (param == null && NC.equals(values[0])) {
                    throw new BusinessException(values[2] + "是必填项,不能为null");
                }
            } catch (NoSuchMethodException e) {
                throw new BusinessException("模板参数名与真实类字段没有对应");
            }
            sendTemplate = replaceTemplateValue(sendTemplate, param);
        }

        return sendTemplate;
    }

    /**
     * 获取${}里面的值,并且将里面的值根据 . 分开
     * @return 如果没有${},则返回空字符串,如果有,则返回一个数组
     */
    private String[] gainTemplateValue(String data) {
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String paramCode = matcher.group();
            String substring = paramCode.substring(2, paramCode.length() - 1);
            return substring.split("\\.");
        }
        return null;
    }

    /**
     * 替换${}里面的值
     * @param param 替换的值
     */
    private String replaceTemplateValue(String data, Object param) {
        if (data == null) {
            return null;
        }
        if (param instanceof String) {
            return data.replaceFirst(REGIX, "\"" + param + "\"");
        } else if (param == null) {
            return data.replaceFirst(REGIX, "null");
        } else {
            return data.replaceFirst(REGIX,  param.toString());
        }
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new BusinessException("数据不存在");
        }
    }

}
