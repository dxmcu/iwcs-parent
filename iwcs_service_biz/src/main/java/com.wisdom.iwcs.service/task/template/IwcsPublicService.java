package com.wisdom.iwcs.service.task.template;

import com.alibaba.fastjson.JSON;
import com.wisdom.base.context.ApplicationProperties;
import com.wisdom.iwcs.common.utils.InterfaceLogConstants;
import com.wisdom.iwcs.common.utils.NetWorkUtil;
import com.wisdom.iwcs.common.utils.RabbitMQUtil;
import com.wisdom.iwcs.common.utils.TaskConstants;
import com.wisdom.iwcs.common.utils.constant.SendStatus;
import com.wisdom.iwcs.common.utils.exception.BusinessException;
import com.wisdom.iwcs.domain.log.TaskOperationLog;
import com.wisdom.iwcs.domain.task.MainTask;
import com.wisdom.iwcs.domain.task.SubTask;
import com.wisdom.iwcs.domain.task.SubTaskTyp;
import com.wisdom.iwcs.domain.task.dto.*;
import com.wisdom.iwcs.mapper.task.AddressMapper;
import com.wisdom.iwcs.mapper.task.MainTaskMapper;
import com.wisdom.iwcs.mapper.task.SubTaskMapper;
import com.wisdom.iwcs.mapper.task.SubTaskTypMapper;
import com.wisdom.iwcs.service.base.ICommonService;
import com.wisdom.iwcs.service.log.logImpl.RabbitMQPublicService;
import com.wisdom.iwcs.service.task.impl.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wisdom.iwcs.common.utils.InterfaceLogConstants.SrcClientCode.SCADA;
import static com.wisdom.iwcs.common.utils.InterfaceLogConstants.SrcClientCode.SRC_HIK;
import static com.wisdom.iwcs.common.utils.InterfaceLogConstants.SrcClientCode.SRC_MES;
import static com.wisdom.iwcs.domain.task.dto.MainTaskStatusEnum.Canceled;

/**
 * iwcs的公共服务
 * @author han
 */
@Service
public class IwcsPublicService {
    private final Logger logger = LoggerFactory.getLogger(IwcsPublicService.class);

    @Autowired
    SubTaskMapper subTaskMapper;
    @Autowired
    SubTaskTypMapper subTaskTypMapper;
    @Autowired
    TemplateRelatedServer templateRelatedServer;
    @Autowired
    ICommonService iCommonService;
    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    MessageService messageService;
    @Autowired
    MainTaskMapper mainTaskMapper;

    /**
     * 根据子任务单号获取最新子任务信息,并将任务消息体取出并完善,然后发送给第三方
     * @param subTaskNum
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendInfoBySubTaskNum(String subTaskNum) {
        logger.info("子任务{}开始下发process ", subTaskNum);
        // 1. 从数据库获取子任务单
        SubTask subTask = subTaskMapper.selectBySubTaskNum(subTaskNum);
        if (TaskConstants.subTaskSendStatus.HAS_SEND.equals(subTask.getSendStatus())) {
            logger.warn("{}子任务下发状态为已下发，跳过本次下发过程", subTask.getSubTaskNum());
            return;
        }
        String jsonStr = templateRelatedServer.templateIntoInfo(subTaskNum);

        // 2. 根据subtask的值，完善下发的信息，并下发命令
        logger.debug("子任务{}开始完善任务消息体", subTaskNum);
        SubTaskTyp subTaskTyp = subTaskTypMapper.selectByTypeCode(subTask.getSubTaskTyp());
        String resultBody;
        String address = addressMapper.selectAddressByCode(subTaskTyp.getWorkerType());
        String url = address + subTaskTyp.getWorkerUrl();
        if (!checkTaskNoCancel(subTask.getMainTaskNum())) {
            return;
        }
        if (SRC_HIK.equals(subTaskTyp.getWorkerType())) {
            //如果执行者类型是海康,则调用海康的接口
            resultBody = NetWorkUtil.transferContinueTask(jsonStr, url);
            iCommonService.handleHikResponseAndThrowException(resultBody);
        } else if (SRC_MES.equals(subTaskTyp.getWorkerType())) {
            logger.info("MES发送任务:{}", jsonStr);
            resultBody = NetWorkUtil.transferContinueTaskUTF8(jsonStr, url);
            iCommonService.handleMesResponse(resultBody);
        } else if (SCADA.equals(subTaskTyp.getWorkerType())) {
            logger.info("SCADA发送任务:{}", jsonStr);
            resultBody = NetWorkUtil.transferContinueTaskUTF8(jsonStr, url);
            iCommonService.handleMesResponse(resultBody);
        } else {
            throw new BusinessException(subTaskTyp.getWorkerType() + "第三方未配置");
        }
        SubTask tmpSubask = new SubTask();
        tmpSubask.setId(subTask.getId());
        tmpSubask.setSendStatus(SendStatus.SEND.getCode());
        tmpSubask.setSendTime(new Date());
        tmpSubask.setTaskMsg(jsonStr);
        //更新子任务的下发状态以及发送的消息体
        subTaskMapper.updateByPrimaryKeySelective(tmpSubask);
        //向消息队列发送消息
        String message = messageService.get("send_success") + subTask.getMainTaskNum()
                        + messageService.get("send_success_2") + jsonStr;
        RabbitMQPublicService.successTaskLog(new TaskOperationLog(subTask.getSubTaskNum(), TaskConstants.operationStatus.SEND_SUCCESS,message));
    }

    /**
     * 检查任务是否已取消
     * @param mainTaskNum
     * @return 未取消返回true
     */
    private boolean checkTaskNoCancel(String mainTaskNum) {
        MainTask mainTask = mainTaskMapper.selectByMainTaskNum(mainTaskNum);
        return !Canceled.getStatusCode().equals(mainTask.getTaskStatus());
    }

    /**
     * 调用海康的接口,根据地码查询对应的货架
     * @param berCode
     * @return
     * @throws JSONException
     */
    public List<String> selectPodCodeByBerCode(String berCode) throws JSONException {
        TempdateRelatedContext template = templateRelatedServer.getRequestInfo();
        HikFindPodCode requestInfo = new HikFindPodCode();
        requestInfo.setClientCode(template.getClientCode());
        requestInfo.setReqCode(template.getReqCode());
        requestInfo.setReqTime(template.getReqTime());
        requestInfo.setTokenCode(template.getTokenCode());
        requestInfo.setPositionCode(berCode);
        String jsonStr = JSON.toJSONString(requestInfo);
        List<FindPodOrMapResult> resultList = queryPodBerthAndMatUtil(jsonStr);
        if (resultList == null) {
            return null;
        }
        List<String> podCodes = new ArrayList<>();
        //提取货架编号
            for (FindPodOrMapResult result : resultList) {
            if (StringUtils.isNotEmpty(result.getPodCode())) {
                podCodes.add(result.getPodCode());
            }
        }
        return podCodes;
    }

    /**
     * 调用海康的接口,根据货架查询对应的地码编号
     * @param podCode
     * @return
     * @throws JSONException
     */
    public List<String> selectBerCodeByPodCode(String podCode) throws JSONException {
        TempdateRelatedContext template = templateRelatedServer.getRequestInfo();
        HikFindBerCode requestInfo = new HikFindBerCode();
        requestInfo.setClientCode(template.getClientCode());
        requestInfo.setReqCode(template.getReqCode());
        requestInfo.setTokenCode(template.getTokenCode());
        requestInfo.setReqTime(template.getReqTime());
        requestInfo.setPodCode(podCode);
        String jsonStr = JSON.toJSONString(requestInfo);
        List<FindPodOrMapResult> resultList = queryPodBerthAndMatUtil(jsonStr);
        if (resultList == null) {
            return null;
        }
        List<String> berCodes = new ArrayList<>();
        //提取地码编号
        for (FindPodOrMapResult result : resultList) {
            if (StringUtils.isNotEmpty(result.getPositionCode())) {
                berCodes.add(result.getPositionCode());
            }
        }
        return berCodes;
    }

    /**
     * 根据json字符串和url调用海康接口,并提取其中的data属性数据
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    private List<FindPodOrMapResult> queryPodBerthAndMatUtil(String jsonStr) throws JSONException {
        String url = applicationProperties.getHikParam().getReturnMapOrPodUrl();
        //调用海康接口
        String resultBody = NetWorkUtil.transferContinueTask(jsonStr, url);
        JSONObject obj = new JSONObject(resultBody);
        if ("".equals(obj.getString("data"))) {
            return null;
        }
        JSONArray data = obj.getJSONArray("data");
        List<FindPodOrMapResult> list = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            FindPodOrMapResult result = JSON.parseObject(data.get(i).toString(), FindPodOrMapResult.class);
            list.add(result);
        }
        return list;
    }

}
