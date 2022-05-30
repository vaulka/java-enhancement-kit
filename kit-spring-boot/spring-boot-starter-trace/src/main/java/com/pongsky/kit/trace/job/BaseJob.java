package com.pongsky.kit.trace.job;

import com.pongsky.kit.common.trace.TraceConstants;
import com.pongsky.kit.common.trace.TraceInfo;
import com.pongsky.kit.common.trace.TraceThreadLocal;
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
     * 链路信息
     */
    private TraceInfo traceInfo;

    /**
     * 执行任务
     */
    @Override
    public void execute(JobExecutionContext context) {
        MDC.put(TraceConstants.X_TRACE_ID, traceInfo.getTraceId());
        TraceThreadLocal.setTraceInfo(traceInfo);
        try {
            this.exec(context);
        } finally {
            MDC.clear();
            TraceThreadLocal.delTraceInfo();
        }
    }

    /**
     * 执行任务
     *
     * @param context job context
     */
    protected abstract void exec(JobExecutionContext context);

}
