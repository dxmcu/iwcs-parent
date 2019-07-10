package com.wisdom.iwcs.service.task.subtask.impl;


import com.rabbitmq.client.Channel;
import com.wisdom.base.context.AppContext;
import com.wisdom.base.quartz.SpringContextUtils;
import com.wisdom.iwcs.domain.task.SubTask;
import com.wisdom.iwcs.service.task.AbstractTaskWorker;
import com.wisdom.iwcs.service.task.conditions.ConditionBase;
import com.wisdom.iwcs.service.task.impl.SubTaskService;
import com.wisdom.iwcs.service.task.maintask.MainTaskWorker;
import com.wisdom.iwcs.service.task.template.IwcsPublicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubTaskWorker extends AbstractTaskWorker {
    private final Logger logger = LoggerFactory.getLogger(SubTaskWorker.class);

    private MainTaskWorker mainTaskWorker;
    private SubTask subTask;
    private List<ConditionBase> conditionList = new ArrayList<ConditionBase>();

    public SubTaskWorker(Channel channel, AbstractTaskWorker mainTask, SubTask subTask) {
        super(channel);
        this.mainTaskWorker = (MainTaskWorker) mainTask;
        this.subTask = subTask;
    }

    public boolean isRunnable(){
        // 1. 判断内存中记录的N个条件是否都已经满足，如果满足，直接返回OK
        // 判断是否有资源能满足条件，如果满足则直接锁定。
        // 满足的条件该锁定资源需要锁定，并记录锁定状态及谁锁定了该资源, in_lock and lock_source
        try {
            SubTaskService subTaskService = (SubTaskService) AppContext.getBean("subTaskService");
            subTaskService.preConditionsCheckAndExec(subTask);
            return true;
        } catch (Exception e) {
            logger.info("{}子任务前置条件暂不满足", subTask.getSubTaskNum());
            e.printStackTrace();
            return false;
        }

    }

    public boolean postRunnable() {
        try {
            SubTaskService subTaskService = (SubTaskService) AppContext.getBean("subTaskService");
            subTaskService.postConditionsCheckAndExec(subTask);
            return true;
        } catch (Exception e) {
            logger.info("{}子任务后置条件暂不满足", subTask.getSubTaskNum());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void preConditions() {
        while (true){
            try {
                synchronized (waitLock){
                    System.out.println("sub task is going to wait " + waitLock);
                    if (! isRunnable()) {
                        logger.info("Task {}, subtask: {} is gonging to wait 10*1000, go...", subTask.getMainTaskNum(), subTask.getSubTaskNum());
                        waitLock.wait(10 * 1000);
                    } else{
                        logger.info("Task {}, subtask: {} become runnable, go...", subTask.getMainTaskNum(), subTask.getSubTaskNum());
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // End of while

    }

    @Override
    public void postConditions() {
        while (true){
            try {
                synchronized (waitLock){
                    System.out.println("sub task is going to wait " + waitLock);
                    if (! postRunnable()) {
                        logger.info("子任务{}后置条件检查未符合,30s后重试", subTask.getSubTaskNum());
                        waitLock.wait(30 * 1000);
                    } else{
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //通知主任务的时机，待定......
        mainTaskWorker.onSubTaskDone(subTask);
        // 锁定的资源


    }

    @Override
    public void process() {
        try {
            IwcsPublicService iwcsPublicService = (IwcsPublicService) SpringContextUtils.getBean("iwcsPublicService");
            iwcsPublicService.sendInfoBySubTaskNum(subTask.getSubTaskNum());
        } catch (Exception e) {


        }
    }

    public SubTask getSubTask() {
        return subTask;
    }

    //    @Override
//    public void onMessage(WcsObservable o, Object arg) {
//
//    }
}
