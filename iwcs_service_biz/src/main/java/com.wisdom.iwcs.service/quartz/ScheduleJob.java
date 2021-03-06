package com.wisdom.iwcs.service.quartz;

import com.wisdom.base.quartz.SpringContextUtils;
import com.wisdom.iwcs.common.utils.JSONUtils;
import com.wisdom.iwcs.domain.quartz.QuartzJob;
import com.wisdom.iwcs.domain.quartz.QuartzJobLog;
import com.wisdom.iwcs.mapper.quartz.QuartzJobLogMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务
 *
 * @author zcl<yczclcn                                                                                                                                                                                                                                                               @                                                                                                                                                                                                                                                               1                                                                                                                                                                                                                                                               6                                                                                                                                                                                                                                                               3                                                                                                                                                                                                                                                               .                                                                                                                                                                                                                                                               com>
 */
@DependsOn("springContextUtils")
public class ScheduleJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //	private ExecutorService service = Executors.newSingleThreadExecutor();
    ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        String jobStr = context.getMergedJobDataMap().getString(QuartzJob.JOB_PARAM_KEY);
        QuartzJob scheduleJob = JSONUtils.jsonToBean(jobStr, QuartzJob.class);

        //获取spring bean
        QuartzJobLogMapper quartzJobLogMapper = (QuartzJobLogMapper) SpringContextUtils.getBean("quartzJobLogMapper");

        //数据库保存执行记录
        QuartzJobLog log = new QuartzJobLog();
        log.setJobId(scheduleJob.getJobId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setMethodName(scheduleJob.getMethodName());
        log.setParams(scheduleJob.getParams());

        //任务开始时间
        long startTime = System.currentTimeMillis();

        try {
            //执行任务
            logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(),
                    scheduleJob.getMethodName(), scheduleJob.getParams());
            Future<?> future = service.submit(task);

            future.get();

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int) times);
            //任务状态    0：失败    1：成功
            log.setStatus("1");

            logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");

        } catch (Exception e) {

            logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int) times);

            //任务状态    0：失败    1：成功
            log.setStatus("0");
            log.setError(StringUtils.substring(e.toString(), 0, 2000));

        } finally {
            quartzJobLogMapper.insertSelective(log);
        }
    }
}
