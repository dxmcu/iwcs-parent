package com.wisdom.iwcs.domain.task;

/**
 * 任务创建请求
 * @Author george
 * @Date 2019/7/3 8:49
 */
public class TaskCreateRequest {

    /**
     * 任务类型编号 必填
     */
    private String taskTypeCode;

    /**
     * 工作点编号
     */
    private String pointAlias;

    /**
     * 作业区域(如老化区、检验区)
     */
    private String operateAreaCode;

    /**
     * 业务次级区域(如老化区下的自动区、手动区)
     */
    private String  bizSecondAreaCode;

    /**
     * 子任务单业务属性 老化区 自动区/手动区
     */
    private String subTaskBizProp;

    /**
     * 目标点位
     */
    private String targetPoint;

    /**
     * 货架号
     */
    private String podCode;

    /**
     * 起始点
     */
    private String startBercode;

    /**
     * 任务优先级 不填
     */
    private Integer priority;

    public String getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(String taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public String getPointAlias() {
        return pointAlias;
    }

    public void setPointAlias(String pointAlias) {
        this.pointAlias = pointAlias;
    }

    public String getOperateAreaCode() {
        return operateAreaCode;
    }

    public void setOperateAreaCode(String operateAreaCode) {
        this.operateAreaCode = operateAreaCode;
    }

    public String getBizSecondAreaCode() {
        return bizSecondAreaCode;
    }

    public void setBizSecondAreaCode(String bizSecondAreaCode) {
        this.bizSecondAreaCode = bizSecondAreaCode;
    }

    public String getSubTaskBizProp() {
        return subTaskBizProp;
    }

    public void setSubTaskBizProp(String subTaskBizProp) {
        this.subTaskBizProp = subTaskBizProp;
    }

    public String getTargetPoint() {
        return targetPoint;
    }

    public void setTargetPoint(String targetPoint) {
        this.targetPoint = targetPoint;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getStartBercode() {
        return startBercode;
    }

    public void setStartBercode(String startBercode) {
        this.startBercode = startBercode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
