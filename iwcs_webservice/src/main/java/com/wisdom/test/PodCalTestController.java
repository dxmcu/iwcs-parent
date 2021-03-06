package com.wisdom.test;

import com.rabbitmq.client.*;
import com.wisdom.config.RabbitConfig;
import com.wisdom.iwcs.common.utils.RabbitMQUtil;
import com.wisdom.iwcs.common.utils.Result;
import com.wisdom.iwcs.commonDto.fliterConOptions.LayerConTypeEnum;
import com.wisdom.iwcs.commonDto.fliterCondition.PodFliterCondition;
import com.wisdom.iwcs.domain.base.dto.LockPodCondition;
import com.wisdom.iwcs.service.common.IPodCal;
import com.wisdom.iwcs.service.task.impl.MapResouceService;
import com.wisdom.iwcs.service.task.template.IwcsPublicService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 货架计算测试
 *
 * @author ted
 * @create 2019-03-13 上午10:19
 **/

@RestController
@RequestMapping("api/test/podCal")
public class PodCalTestController {

    @Resource
    private MapResouceService mapResouceService;

    @Resource
    private IPodCal podCal;

    @Autowired
    IwcsPublicService iwcsPublicService;

    @PostMapping("/calPodByPodFliter")
    public List<String> calPodByConTest(@RequestBody PodFliterCondition podFliterCondition) {
        List<String> pods = podCal.calPodByPodFliterCondition(podFliterCondition);

        return pods;
    }

    @PostMapping("/calPodByPodFliter/testEnum")
    public void calPodByConTest(@RequestParam LayerConTypeEnum layerConTypeEnum) {
        System.out.println(layerConTypeEnum);
    }

    /**
     * 测试锁定货架
     */
    @GetMapping("/testLockPod")
    public void testLockPod() {
        LockPodCondition lockPodCondition = new LockPodCondition();
        lockPodCondition.setInStock("0");
        lockPodCondition.setLockSource("测试货架锁定");
        lockPodCondition.setMapCode("mapCode");
        lockPodCondition.setOperateAreaCode("operateAreaCode");
        List<LockPodCondition> lockPodConditions = new ArrayList<>();
        lockPodConditions.add(lockPodCondition);
        Result result = mapResouceService.lockPodByCondition(lockPodConditions);
        System.out.println(result.getReturnData());
    }

    /**
     * 测试查找货架工具
     */
    @GetMapping("/testFindPodCode/{berCode}")
    public void testFindPodCode(@PathVariable String berCode) {
        try {
            List<String> strings = iwcsPublicService.selectPodCodeByBerCode(berCode);
            for (String string : strings) {
                System.out.println(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    /**
     * 测试查找地码工具
     */
    @GetMapping("/testFindBerCode/{podCode}")
    public void testFindBerCode(@PathVariable String podCode) {
        try {
            List<String> strings = iwcsPublicService.selectBerCodeByPodCode(podCode);
            for (String string : strings) {
                System.out.println(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

}
