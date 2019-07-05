package com.wisdom.iwcs.service.task.scheduler;


import com.wisdom.iwcs.domain.task.MainTask;
import com.wisdom.iwcs.service.task.intf.IMainTaskService;
import com.wisdom.iwcs.service.task.maintask.MainTaskWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WcsTaskScheduler implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(WcsTaskScheduler.class);
    public IMainTaskService mainTaskService;

    public WcsTaskScheduler() {
        Thread t = new Thread(this);
        t.start();
    }
    /**
     * 完成一次主任务下发检测及下发
     */
    public void dispatchMaintask() {
        //获取所有待下发的任务
        List<MainTask> allUnDispatchedTask = mainTaskService.getAllUnDispatchedTask();
        //顺序执行主任务可执行性检查及下发
        allUnDispatchedTask.stream().forEach(t -> {
            //检查主任务第一条子任务是否满足执行条件
            MainTaskWorker mainTaskWorker = new MainTaskWorker(null, t);
            Thread thread = new Thread(mainTaskWorker);
            thread.start();
//            if (subTaskOptional.isPresent()) {
//                SubTask firstSubTask = subTaskOptional.get();
//                boolean dispatchable = mainTaskService.subtaskPreConditionMetCheck(firstSubTask);
//                if (dispatchable) {
//                    logger.debug("主任务{}满足执行条件，开始下发", t.getMainTaskNum());
//                } else {
//                    logger.debug("主任务{}不满足执行条件，跳过本次下发", t.getMainTaskNum());
//                }
//            } else {
//                throw new BusinessException("主任务未生成有效子任务，请检查任务类型配置" + t.getMainTaskTypeCode());
//            }

        });
    }

    public static  void main(String[] args){
//        WcsObservable waitThread = new MainTask();
//        ((MainTask) waitThread).start();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        waitThread.notifyMe();

    }

    @Override
    public void run() {
        // 检查主任务列表，拿到所有可以执行的主任务列表，判断主任务是否可以执行，以主任务当前的子任务是否可以执行为标准
    }
}


