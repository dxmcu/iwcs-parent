package com.wisdom.iwcs.service.task.scheduler;

import com.wisdom.iwcs.common.utils.YZConstants;
import com.wisdom.iwcs.common.utils.exception.Preconditions;
import com.wisdom.iwcs.domain.base.BaseMapBerth;
import com.wisdom.iwcs.domain.base.BasePodDetail;
import com.wisdom.iwcs.domain.upstream.mes.CreateTaskRequest;
import com.wisdom.iwcs.mapper.base.BaseMapBerthMapper;
import com.wisdom.iwcs.mapper.base.BasePodDetailMapper;
import com.wisdom.iwcs.service.task.intf.ITaskCreateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wisdom.iwcs.common.utils.InspurBizConstants.BizTypeConstants.LINEWORKAREA;
import static com.wisdom.iwcs.common.utils.InspurBizConstants.OperateAreaCodeConstants.WOKPWAREA;
import static com.wisdom.iwcs.common.utils.TaskConstants.taskCodeType.PLTOWOKPW;

/**
 * 产线呼叫搬离货架 的自动化任务
 */
@Component
public class PlToWokpwWorker implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(PlToWokpwWorker.class);

    @Autowired
    BaseMapBerthMapper baseMapBerthMapper;
    @Autowired
    private ITaskCreateService taskCreateService;
    @Autowired
    private BasePodDetailMapper basePodDetailMapper;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    autoTask();
                    this.wait(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        this.wait(30 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    private void autoTask() {
        logger.info("开始创建 产线呼叫搬离货架 的任务");
        List<BaseMapBerth> mapBerthList = baseMapBerthMapper.selectByBizTye(LINEWORKAREA);
        Preconditions.checkBusinessError(mapBerthList.size() <= 0, "基础数据异常: 找不到线体工作区");
        List<BaseMapBerth> berthList = baseMapBerthMapper.selectByOperateAreaCode(WOKPWAREA);
        Preconditions.checkBusinessError(berthList.size() <= 0, "基础数据异常: 找不到人工插线区");
        String srcWb = "";
        String map = "";

        //查找线体工作区的一个 货架
        for (BaseMapBerth baseMapBerth:mapBerthList){
            if (StringUtils.isNotEmpty(baseMapBerth.getPodCode()) &&  YZConstants.UNLOCK.equals(baseMapBerth.getInLock()) && StringUtils.isEmpty(baseMapBerth.getLockSource())){
                BasePodDetail basePodDetail = basePodDetailMapper.selectByPodCode(baseMapBerth.getPodCode());
                if (YZConstants.UNLOCK.equals(basePodDetail.getInLock()) && StringUtils.isEmpty(basePodDetail.getLockSource()) ){
                    srcWb = baseMapBerth.getPointAlias();
                    break;
                }

            }
        }

        //查找人工插线区的一个空位置
        for (BaseMapBerth baseMapBerth:berthList){
            if (StringUtils.isEmpty(baseMapBerth.getPodCode()) && YZConstants.UNLOCK.equals(baseMapBerth.getInLock()) && StringUtils.isEmpty(baseMapBerth.getLockSource())){
                map = baseMapBerth.getPointAlias();
                break;
            }
        }

        if (StringUtils.isNotEmpty(srcWb) && StringUtils.isNotEmpty(map)) {
            CreateTaskRequest createTaskRequest = new CreateTaskRequest();
            createTaskRequest.setTaskType(PLTOWOKPW);
            createTaskRequest.setSrcWb(srcWb);
            createTaskRequest.setTaskPri("normal");
            createTaskRequest.setTaskCode("SKUNO20180331");
            String reqCode = "T6000001970";
            taskCreateService.pToPHandlingTask(createTaskRequest, reqCode);
        }
    }
}
