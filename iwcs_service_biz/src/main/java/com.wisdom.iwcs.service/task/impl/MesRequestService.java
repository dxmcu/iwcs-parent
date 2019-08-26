package com.wisdom.iwcs.service.task.impl;

import com.wisdom.iwcs.common.utils.exception.MesBusinessException;
import com.wisdom.iwcs.common.utils.taskUtils.TaskContextUtils;
import com.wisdom.iwcs.domain.task.TaskContext;
import com.wisdom.iwcs.domain.task.dto.ContextDTO;
import com.wisdom.iwcs.domain.upstream.mes.MesResult;
import com.wisdom.iwcs.domain.upstream.mes.StartRecyle;
import com.wisdom.iwcs.domain.upstream.mes.StartSupllyAndRecyle;
import com.wisdom.iwcs.domain.upstream.mes.SupplyInfoNotify;
import com.wisdom.iwcs.mapper.task.TaskContextMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mes系统请求的业务逻辑
 * @author han
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MesRequestService {
    private final Logger logger = LoggerFactory.getLogger(MesRequestService.class);
    @Autowired
    TaskContextMapper taskContextMapper;


    /**
     * 接收 Mes 通知 AGV 接料点目的地的请求
     * @param supplyInfoNotify
     * @return
     */
    public MesResult supplyUnloadWbNotify(SupplyInfoNotify supplyInfoNotify) {
        logger.info("接收到Mes通知AGV接料点目的地的请求,请求体:{}", supplyInfoNotify);
        //1.参数校验
        if (StringUtils.isBlank(supplyInfoNotify.getTaskCode())) {
            throw new MesBusinessException(supplyInfoNotify.getTaskCode(), "任务号不能为空");
        }
        if (StringUtils.isBlank(supplyInfoNotify.getSupplyUnLoadWbFirst())) {
            throw new MesBusinessException(supplyInfoNotify.getTaskCode(), "第一个接料点不能为空");
        }
        if (supplyInfoNotify.getSupplyUnLoadWbFirstCount() == null) {
            throw new MesBusinessException(supplyInfoNotify.getTaskCode(), "第一个接料点的接料数不能为空");
        }

        //2.获取原context
        TaskContext taskContext = taskContextMapper.selectByMainTaskNum(supplyInfoNotify.getTaskCode());
        ContextDTO contextDTO = TaskContextUtils.jsonToObject(taskContext.getContext(), ContextDTO.class);

        //3.更新context的json字符串
        contextDTO.setSupplyUnLoadWbFirst(supplyInfoNotify.getSupplyUnLoadWbFirst());
        contextDTO.setSupplyUnLoadWbFirstCount(supplyInfoNotify.getSupplyUnLoadWbFirstCount());
        contextDTO.setSupplyUnLoadWbSecond(supplyInfoNotify.getSupplyUnLoadWbSecond());
        contextDTO.setSupplyUnLoadWbSecondCount(supplyInfoNotify.getSupplyUnLoadWbSecondCount());
        String jsonStr = TaskContextUtils.objectToJson(contextDTO);
        taskContextMapper.updateByPrimaryKeySelective(new TaskContext(taskContext.getId(), jsonStr));

        logger.info("Mes通知AGV接料点目的地的请求处理结束,任务编号:{}", supplyInfoNotify.getTaskCode());
        return new MesResult();
    }

    /**
     * 接收 Mes 接料点通知供料及回收空框信息的请求
     * @param startSupllyAndRecyle
     * @return
     */
    public MesResult startSupllyAndRecyle(StartSupllyAndRecyle startSupllyAndRecyle) {
        logger.info("接收到Mes接料点通知供料及回收空框信息的请求,请求体:{}", startSupllyAndRecyle);
        //1.参数校验
        if (StringUtils.isBlank(startSupllyAndRecyle.getTaskCode())) {
            throw new MesBusinessException(startSupllyAndRecyle.getTaskCode(), "任务号不能为空");
        }
        if (StringUtils.isBlank(startSupllyAndRecyle.getEmptyRecyleWb()) && startSupllyAndRecyle.getEmptyRecyleNum() == null) {
            return new MesResult();
        }

        //校验送料点和送料数量

        //2.获取原context
        TaskContext taskContext = taskContextMapper.selectByMainTaskNum(startSupllyAndRecyle.getTaskCode());
        ContextDTO contextDTO = TaskContextUtils.jsonToObject(taskContext.getContext(), ContextDTO.class);

        //3.更新context的json字符串
        if (StringUtils.isNotBlank(startSupllyAndRecyle.getEmptyRecyleWb())) {
            contextDTO.setEmptyRecyleWb(startSupllyAndRecyle.getEmptyRecyleWb());
        }
        if (contextDTO.getEmptyRecyleNum() == null) {
            contextDTO.setEmptyRecyleNum(startSupllyAndRecyle.getEmptyRecyleNum());
        } else {
            contextDTO.setEmptyRecyleNum(contextDTO.getEmptyRecyleNum() + startSupllyAndRecyle.getEmptyRecyleNum());
        }
        String jsonStr = TaskContextUtils.objectToJson(contextDTO);
        taskContextMapper.updateByPrimaryKeySelective(new TaskContext(taskContext.getId(), jsonStr));

        logger.info("Mes接料点通知供料及回收空框信息的请求处理结束,任务编号:{}", startSupllyAndRecyle.getTaskCode());
        return new MesResult();
    }

    /**
     * 接收 Mes 通知可出空料框的请求
     * @param startRecyle
     * @return
     */
    public MesResult startRecyle(StartRecyle startRecyle) {
        logger.info("接收到Mes通知可出空料框的请求,请求体:{}", startRecyle);
        //1.参数校验
        if (StringUtils.isBlank(startRecyle.getTaskCode())) {
            throw new MesBusinessException(startRecyle.getTaskCode(), "任务号不能为空");
        }

        //校验回收点和回收数量是否匹配

        logger.info("Mes通知可出空料框的请求处理结束,任务编号:{}", startRecyle.getTaskCode());
        return new MesResult();
    }
}