package com.wisdom.iwcs.service.task.conditions.conditonHandler;

import com.wisdom.iwcs.common.utils.InspurBizConstants;
import com.wisdom.iwcs.domain.task.AreaCondition;
import com.wisdom.iwcs.domain.task.SubTaskCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 缓存区补充空货架前置条件--锁定缓存区的一个空储位
 * @author  han
 */
@Service
public class CacheEmptyPodLockHandler implements IConditionHandler{

    @Autowired
    BaseLockEmptyMapService baseLockEmptyMapService;

    @Override
    public boolean handleCondition(SubTaskCondition subTaskCondition) {
        AreaCondition areaCondition = new AreaCondition();
        //查找电梯缓存区的空点位
        areaCondition.setArea(InspurBizConstants.BizTypeConstants.LINECACHEAREA);

        return baseLockEmptyMapService.handleConditionService(subTaskCondition, Arrays.asList(areaCondition));
    }

    @Override
    public boolean rollbackCondition(SubTaskCondition subTaskCondition) {
        return baseLockEmptyMapService.rollbackConditionService(subTaskCondition);
    }
}
