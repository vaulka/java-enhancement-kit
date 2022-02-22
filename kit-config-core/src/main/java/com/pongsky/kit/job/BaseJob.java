package com.pongsky.kit.job;

import com.pongsky.kit.utils.trace.CurrentInfo;
import com.pongsky.kit.utils.trace.CurrentThreadConfig;
import com.pongsky.kit.utils.trace.DiyHeader;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;

/**
 * Job 配置
 * <p>
 * 将 Job execute 默认实现，套一层数据传递，再提供额外方法共 Job 实现
 *
 * @author pengsenhao
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
     * @author pengsenhao
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
     * @author pengsenhao
     */
    protected abstract void exec(JobExecutionContext context);

}
