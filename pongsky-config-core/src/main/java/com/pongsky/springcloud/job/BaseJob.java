package com.pongsky.springcloud.job;

import com.pongsky.springcloud.utils.trace.CurrentInfo;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import com.pongsky.springcloud.utils.trace.DiyHeader;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-01-27 10:45 上午
 */
@Setter
public abstract class BaseJob implements Job {

    /**
     * 当前请求信息
     */
    private CurrentInfo currentInfo;

    /**
     * 执行任务
     *
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-27 10:48 上午
     */
    @Override
    public void execute(JobExecutionContext context) {
        MDC.put(DiyHeader.X_TRACE_ID, currentInfo.getTraceId());
        CurrentThreadConfig.setCurrentInfo(currentInfo);
        try {
            this.exec(context);
        } finally {
            MDC.clear();
            CurrentThreadConfig.relCurrentInfo();
        }
    }

    /**
     * 执行任务
     *
     * @param context job context
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-27 10:48 上午
     */
    protected abstract void exec(JobExecutionContext context);

}
