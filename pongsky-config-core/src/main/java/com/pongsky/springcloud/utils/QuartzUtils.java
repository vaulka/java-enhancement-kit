package com.pongsky.springcloud.utils;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

/**
 * 定时任务工具类
 *
 * @author pengsenhao
 */
@RequiredArgsConstructor
public class QuartzUtils {

    private final Scheduler scheduler;

    /**
     * 创建单次定时任务
     *
     * @param name        任务名称
     * @param group       任务组
     * @param description 描述
     * @param jobDataMap  数据
     * @param startTime   启动时间
     * @param clazz       定时任务类
     * @param <T>         泛型
     * @throws SchedulerException 调度器异常
     */
    public <T extends Job> void createIntervalJob(String name,
                                                  String group,
                                                  String description,
                                                  JobDataMap jobDataMap,
                                                  Date startTime,
                                                  Class<T> clazz) throws SchedulerException {
        checkExists(name, group);
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(name, group)
                .withDescription(description)
                .usingJobData(jobDataMap)
                .build();
        SimpleTriggerImpl trigger = new SimpleTriggerImpl();
        trigger.setName(name);
        trigger.setGroup(group);
        trigger.setJobKey(JobKey.jobKey(name, group));
        trigger.setDescription(description);
        trigger.setJobDataMap(jobDataMap);
        trigger.setStartTime(startTime);
        trigger.setRepeatCount(0);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 创建循环定时任务
     *
     * @param name        任务名称
     * @param group       任务组
     * @param description 描述
     * @param jobDataMap  定时任务属性数据
     * @param cron        cron 表达式
     * @param t           job 实现类
     * @param <T>         泛型
     * @throws SchedulerException 调度器异常
     */
    public <T extends Job> void createCronJob(String name,
                                              String group,
                                              String description,
                                              JobDataMap jobDataMap,
                                              String cron,
                                              Class<T> t) throws SchedulerException {
        checkExists(name, group);
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(t)
                .withIdentity(name, group)
                .withDescription(description)
                .usingJobData(jobDataMap)
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .withDescription(description)
                .usingJobData(jobDataMap)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .startNow()
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.start();
    }

    /**
     * 删除定时任务
     *
     * @param name  任务名称
     * @param group 任务组
     * @throws SchedulerException 调度器异常
     */
    public void removeJob(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(name, group));
    }

    /**
     * 根据任务名称和任务组检测存在
     *
     * @param name  任务名称
     * @param group 任务组
     * @return 根据任务名称和任务组检测存在
     * @throws SchedulerException 调度去异常
     */
    public boolean exists(String name, String group) throws SchedulerException {
        return scheduler.checkExists(TriggerKey.triggerKey(name, group));
    }

    /**
     * 根据任务名称和任务组检测存在
     *
     * @param name  任务名称
     * @param group 任务组
     * @throws SchedulerException 调度去异常
     */
    private void checkExists(String name, String group) throws SchedulerException {
        boolean checkExists = this.exists(name, group);
        if (checkExists) {
            throw new RuntimeException("该任务名称和任务组已存在");
        }
    }
}
