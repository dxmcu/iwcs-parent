package com.wisdom.iwcs.service.linebody.impl;

import com.google.common.base.Strings;
import com.wisdom.iwcs.common.utils.plcUtils.CRCUtils;
import com.wisdom.iwcs.common.utils.plcUtils.PlcProtocolUtils;
import com.wisdom.iwcs.domain.base.BaseMapBerth;
import com.wisdom.iwcs.domain.linebody.LineBodyReport;
import com.wisdom.iwcs.domain.linebody.LineMsgLog;
import com.wisdom.iwcs.domain.task.TaskCreateRequest;
import com.wisdom.iwcs.mapper.base.BaseMapBerthMapper;
import com.wisdom.iwcs.mapper.linebody.LineMsgLogMapper;
import com.wisdom.iwcs.service.base.ICommonService;
import com.wisdom.iwcs.service.task.intf.ITaskCreateService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.wisdom.iwcs.common.utils.InspurBizConstants.AgingAreaPriorityProp.AUTO_FIRST;
import static com.wisdom.iwcs.common.utils.TaskConstants.taskCodeType.PLAUTOWBCALLPOD;
import static com.wisdom.iwcs.common.utils.TaskConstants.taskCodeType.PLTOAGING;

/**
 * 线控 service
 * @Author george
 * @Date 2019/7/16 16:17 
 */
public class LineNotifyService {
    private final Logger logger = LoggerFactory.getLogger(LineNotifyService.class);

    private static Channel ch;

    @Autowired
    private ICommonService iCommonService;
    @Autowired
    private LineMsgLogMapper lineMsgLogMapper;
    @Autowired
    private ITaskCreateService iTaskCreateService;
    @Autowired
    private BaseMapBerthMapper baseMapBerthMapper;

    /**
     * 线体通知WCS 呼叫空货架
     * @param
     * @return
     */
    public void lineCallEmptyPod(LineBodyReport lineBodyReport){
        //01 正常
        String msgStatus = "01";
        //接收到信号，拆分，获取点位
        String workPoint = lineBodyReport.getWorkPoint();
        //查询这点有货架或没有完结的任务没
        BaseMapBerth baseMapBerth = baseMapBerthMapper.selectByPointAlias(workPoint);
        if (baseMapBerth.getInLock() != 0 || !Strings.isNullOrEmpty(baseMapBerth.getPodCode())){
            msgStatus = "02";
        }else {
            //创建 呼叫空货架 任务
            TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
            taskCreateRequest.setTaskTypeCode(PLAUTOWBCALLPOD);
            taskCreateRequest.setTargetPointAlias(lineBodyReport.getWorkPoint());
            taskCreateRequest.setAreaCode(baseMapBerth.getAreaCode());
            iTaskCreateService.creatTask(taskCreateRequest);
        }
        //通知线体 是否成功
        byte[] leaveCommandBinary= this.lineMsgReturnCommandBinary(lineBodyReport.getAddress(), lineBodyReport.getDeviceType(), msgStatus, lineBodyReport.getReqCode());
        ch.writeAndFlush(leaveCommandBinary);
        //更新line_msg_log
        //this.insertLineMsgLog();
    }

    /**
     * 线体上报WCS 搬走货架
     * @param
     * @return
     */
    public void lineCallAgvPickPod(LineBodyReport lineBodyReport){
        //01 正常
        String msgStatus = "01";
        //接收到信号，拆分，获取点位
        String workPoint = lineBodyReport.getWorkPoint();
        //查询这点有货架没
        BaseMapBerth baseMapBerth = baseMapBerthMapper.selectByPointAlias(workPoint);
        if (Strings.isNullOrEmpty(baseMapBerth.getPodCode())){
            msgStatus = "02";
        }else{
            //创建 线体到老化区 任务
            //目标点，2楼全自动，随机选，3楼自动区域
            TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
            taskCreateRequest.setTaskTypeCode(PLTOAGING);
            taskCreateRequest.setTargetPointAlias(lineBodyReport.getWorkPoint());
            taskCreateRequest.setSubTaskBizProp(AUTO_FIRST);
            taskCreateRequest.setAreaCode(baseMapBerth.getAreaCode());
            iTaskCreateService.creatTask(taskCreateRequest);
        }
        //通知线体 是否成功
        byte[] leaveCommandBinary= this.lineMsgReturnCommandBinary(lineBodyReport.getAddress(), lineBodyReport.getDeviceType(), msgStatus, lineBodyReport.getReqCode());
        ch.writeAndFlush(leaveCommandBinary);
        //更新line_msg_log
        //this.insertLineMsgLog();
    }

    /**
     * Agv搬运货架到达线体工作点
     * @param
     * @return
     */
    public void agvArriveLine(String controllerNo, String controllerType, String workPoint){
        //通知线体
        byte[] arriveCommandBinary= this.agvArriveCommandBinary(controllerNo, controllerType, workPoint);
        ch.writeAndFlush(arriveCommandBinary);
        //写入line_msg_log
        //this.insertLineMsgLog();
    }

    /**
     * Agv搬运货架离开线体工作点
     * @param
     * @return
     */
    public void agvPickLine(String controllerNo, String controllerType, String workPoint){
        //通知线体
        byte[] leaveCommandBinary= this.agvLeaveCommandBinary(controllerNo, controllerType, workPoint);
        ch.writeAndFlush(leaveCommandBinary);
        //写入line_msg_log
        //this.insertLineMsgLog();
    }
    /**
     * Agv搬运货架离开线体工作点
     * @param
     * @return
     */
    private String agvLeaveCommandStr(String controllerNo, String controllerType, String workPoint){
        //获取随机码
        String randomNum = iCommonService.randomHexString(8);
        //写死询命令
        String commandBody = controllerNo + controllerType + randomNum + workPoint + "02";
        byte[] str16Tobyte = CRCUtils.hexStringToBytes(commandBody);
        String s = CRCUtils.Make_CRC(str16Tobyte);
        String commandComplete = commandBody + s;
        return commandComplete;
    }
    private byte[] agvLeaveCommandBinary(String controllerNo, String controllerType, String workPoint){
        String generatorQueryCommandStr = this.agvLeaveCommandStr(controllerNo, controllerType, workPoint);
        return PlcProtocolUtils.hexStrToBinaryStr(generatorQueryCommandStr);
    }

    /**
     * Agv搬运货架到达线体工作点
     * @param
     * @return
     */
    private String agvArriveCommandStr(String controllerNo, String controllerType, String workPoint){
        //获取随机码
        String randomNum = iCommonService.randomHexString(8);
        //写死询命令
        String commandBody = controllerNo + controllerType + randomNum + workPoint + "01";
        byte[] str16Tobyte = CRCUtils.hexStringToBytes(commandBody);
        String s = CRCUtils.Make_CRC(str16Tobyte);
        String commandComplete = commandBody + s;
        return commandComplete;
    }
    private byte[] agvArriveCommandBinary(String controllerNo, String controllerType, String workPoint){
        String generatorQueryCommandStr = this.agvArriveCommandStr(controllerNo, controllerType, workPoint);
        return PlcProtocolUtils.hexStrToBinaryStr(generatorQueryCommandStr);
    }

    /**
     * 线体通知成功或失败
     * msgStatus 0失败，1成功
     */
    private String lineMsgReturnCommandStr(String controllerNo, String controllerType, String msgStatus, String randomNum){
        //写死询命令
        String commandBody = controllerNo + controllerType + msgStatus + randomNum ;
        byte[] str16Tobyte = CRCUtils.hexStringToBytes(commandBody);
        String s = CRCUtils.Make_CRC(str16Tobyte);
        String commandComplete = commandBody + s;
        return commandComplete;
    }
    private byte[] lineMsgReturnCommandBinary(String controllerNo, String controllerType, String msgStatus, String randomNum){
        String generatorQueryCommandStr = this.lineMsgReturnCommandStr(controllerNo, controllerType, msgStatus, randomNum);
        return PlcProtocolUtils.hexStrToBinaryStr(generatorQueryCommandStr);
    }

    /**
     * common insert line_msg_log
     */
    private void insertLineMsgLog(String sendAddr, String msgBody, String msgType, String reqCode){
        LineMsgLog lineMsgLog = new LineMsgLog();
        lineMsgLog.setCreatedTime(new Date());
        lineMsgLog.setSendAddr(sendAddr);
        lineMsgLog.setMsgBody(msgBody);
        lineMsgLog.setMsgType(msgType);
        lineMsgLog.setReqCode(reqCode);
        lineMsgLogMapper.insertSelective(lineMsgLog);
    }
}
