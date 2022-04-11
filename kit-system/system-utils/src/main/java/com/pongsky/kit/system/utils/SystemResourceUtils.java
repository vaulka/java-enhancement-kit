package com.pongsky.kit.system.utils;

import com.pongsky.kit.system.entity.Cpu;
import com.pongsky.kit.system.entity.Jvm;
import com.pongsky.kit.system.entity.Mem;
import com.pongsky.kit.system.entity.Os;
import com.pongsky.kit.system.entity.SystemResource;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 系统资源 工具类
 *
 * @author pengsenhao
 **/
public class SystemResourceUtils {

    /**
     * 获取系统资源信息
     *
     * @return 获取系统资源信息
     */
    public static SystemResource read() {
        Os os = SystemResourceUtils.readOs();
        Cpu cpu = SystemResourceUtils.readCpu();
        Mem mem = SystemResourceUtils.readMem();
        Jvm jvm = SystemResourceUtils.readJvm();
        return new SystemResource()
                .setOs(os)
                .setCpu(cpu)
                .setMem(mem)
                .setJvm(jvm);
    }

    /**
     * 系统属性配置
     */
    private static final Properties PROPS = System.getProperties();

    /**
     * 获取 OS 信息
     *
     * @return 获取 OS 信息
     */
    public static Os readOs() {
        String name = PROPS.getProperty("os.name");
        String arch = PROPS.getProperty("os.arch");
        String hostName;
        String hostAddress;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostName = inetAddress.getHostName();
            hostAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = null;
            hostAddress = null;
        }
        return new Os()
                .setName(name)
                .setArch(arch)
                .setHostName(hostName)
                .setHostAddress(hostAddress);
    }

    /**
     * 查询系统信息类
     */
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();

    /**
     * 获取 CPU 指标信息
     *
     * @return 获取 CPU 指标信息
     */
    public static Cpu readCpu() {
        CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long system = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + system + idle + ioWait + irq + softIrq + steal;
        return new Cpu()
                .setLogicalProcessorCount(processor.getLogicalProcessorCount())
                .setSystemUtilization(new DecimalFormat("#.##%").format(system * 1.0 / totalCpu))
                .setUserUtilization(new DecimalFormat("#.##%").format(user * 1.0 / totalCpu))
                .setIoWaitRate(new DecimalFormat("#.##%").format(ioWait * 1.0 / totalCpu))
                .setTotalUtilization(new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
    }

    /**
     * 获取 MEM 指标信息
     *
     * @return 获取 MEM 指标信息
     */
    public static Mem readMem() {
        GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
        long totalByte = memory.getTotal();
        long availableByte = memory.getAvailable();
        String memoryCapacity = SystemResourceUtils.formatByte(totalByte);
        String usedMemoryCapacity = SystemResourceUtils.formatByte(totalByte - availableByte);
        String freeMemoryCapacity = SystemResourceUtils.formatByte(availableByte);
        String memoryUtilization = new DecimalFormat("#.##%")
                .format((totalByte - availableByte) * 1.0 / totalByte);
        return new Mem()
                .setMemoryCapacity(memoryCapacity)
                .setUsedMemoryCapacity(usedMemoryCapacity)
                .setFreeMemoryCapacity(freeMemoryCapacity)
                .setMemoryUtilization(memoryUtilization);
    }

    /**
     * 获取 JVM 指标信息
     *
     * @return 获取 JVM 指标信息
     */
    public static Jvm readJvm() {
        Runtime runtime = Runtime.getRuntime();
        String version = PROPS.getProperty("java.version");
        long jvmTotalMemoryByte = runtime.totalMemory();
        long jvmFreeMemoryByte = runtime.freeMemory();
        String memoryCapacity = SystemResourceUtils.formatByte(jvmTotalMemoryByte);
        String usedMemoryCapacity = SystemResourceUtils.formatByte(jvmTotalMemoryByte - jvmFreeMemoryByte);
        String freeMemoryCapacity = SystemResourceUtils.formatByte(jvmFreeMemoryByte);
        String memoryUtilization = new DecimalFormat("#.##%")
                .format((jvmTotalMemoryByte - jvmFreeMemoryByte) * 1.0 / jvmTotalMemoryByte);
        return new Jvm()
                .setVersion(version)
                .setMemoryCapacity(memoryCapacity)
                .setUsedMemoryCapacity(usedMemoryCapacity)
                .setFreeMemoryCapacity(freeMemoryCapacity)
                .setMemoryUtilization(memoryUtilization);
    }

    /**
     * 字节换算单位
     */
    private static final double FORMAT = 1024;

    /**
     * 格式化字节
     *
     * @param byteNumber 字节
     * @return 格式化字节
     */
    private static String formatByte(long byteNumber) {
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

}
