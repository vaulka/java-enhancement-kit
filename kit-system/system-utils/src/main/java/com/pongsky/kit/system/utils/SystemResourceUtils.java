package com.pongsky.kit.system.utils;

import com.pongsky.kit.system.entity.Cpu;
import com.pongsky.kit.system.entity.Disk;
import com.pongsky.kit.system.entity.Jdk;
import com.pongsky.kit.system.entity.Jvm;
import com.pongsky.kit.system.entity.Mem;
import com.pongsky.kit.system.entity.Os;
import com.pongsky.kit.system.entity.SystemResource;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        Jdk jdk = SystemResourceUtils.readJdk();
        Jvm jvm = SystemResourceUtils.readJvm();
        List<Disk> disks = SystemResourceUtils.readDisks();
        return new SystemResource()
                .setOs(os)
                .setCpu(cpu)
                .setMem(mem)
                .setJdk(jdk)
                .setJvm(jvm)
                .setDisks(disks);
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
                .setModel(processor.getProcessorIdentifier().getName())
                .setMicroArchitecture(processor.getProcessorIdentifier().getMicroarchitecture())
                .setPhysicalProcessorCount(processor.getPhysicalProcessorCount())
                .setLogicalProcessorCount(processor.getLogicalProcessorCount())
                .setSystemUsage(SystemResourceUtils.formatUsage(system * 1.0 / totalCpu))
                .setUserUsage(SystemResourceUtils.formatUsage(user * 1.0 / totalCpu))
                .setIoWaitUsage(SystemResourceUtils.formatUsage(ioWait * 1.0 / totalCpu))
                .setTotalUsage(SystemResourceUtils.formatUsage(1.0 - (idle * 1.0 / totalCpu)));
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
        String capacity = SystemResourceUtils.formatByte(totalByte);
        String usedCapacity = SystemResourceUtils.formatByte(totalByte - availableByte);
        String freeCapacity = SystemResourceUtils.formatByte(availableByte);
        return new Mem()
                .setCapacity(capacity)
                .setUsedCapacity(usedCapacity)
                .setFreeCapacity(freeCapacity)
                .setUsage(SystemResourceUtils.formatUsage((totalByte - availableByte) * 1.0 / totalByte));
    }

    /**
     * 获取 JDK 信息
     *
     * @return 获取 JDK 信息
     */
    public static Jdk readJdk() {
        String name = ManagementFactory.getRuntimeMXBean().getVmName();
        String version = PROPS.getProperty("java.version");
        String home = PROPS.getProperty("java.home");
        String runHome = PROPS.getProperty("user.dir");
        return new Jdk()
                .setName(name)
                .setVersion(version)
                .setHome(home)
                .setRunHome(runHome);
    }

    /**
     * 获取 JVM 指标信息
     *
     * @return 获取 JVM 指标信息
     */
    public static Jvm readJvm() {
        Runtime runtime = Runtime.getRuntime();
        long jvmTotalMemoryByte = runtime.totalMemory();
        long jvmFreeMemoryByte = runtime.freeMemory();
        String capacity = SystemResourceUtils.formatByte(jvmTotalMemoryByte);
        String usedCapacity = SystemResourceUtils.formatByte(jvmTotalMemoryByte - jvmFreeMemoryByte);
        String freeCapacity = SystemResourceUtils.formatByte(jvmFreeMemoryByte);
        return new Jvm()
                .setCapacity(capacity)
                .setUsedCapacity(usedCapacity)
                .setFreeCapacity(freeCapacity)
                .setUsage(SystemResourceUtils.formatUsage((jvmTotalMemoryByte - jvmFreeMemoryByte) * 1.0 / jvmTotalMemoryByte));
    }

    /**
     * 获取 Disk 指标信息
     *
     * @return 获取 Disk 指标信息
     */
    private static List<Disk> readDisks() {
        FileSystem fileSystem = SYSTEM_INFO.getOperatingSystem().getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        if (fileStores.size() == 0) {
            return Collections.emptyList();
        }
        List<Disk> disks = new ArrayList<>(fileStores.size());
        for (OSFileStore fileStore : fileStores) {
            String mount = fileStore.getMount();
            String type = fileStore.getType();
            String name = fileStore.getName();
            long free = fileStore.getUsableSpace();
            long total = fileStore.getTotalSpace();
            long used = total - free;
            String capacity = SystemResourceUtils.formatByte(total);
            String usedCapacity = SystemResourceUtils.formatByte(used);
            String freeCapacity = SystemResourceUtils.formatByte(free);
            Disk disk = new Disk()
                    .setMount(mount)
                    .setType(type)
                    .setName(name)
                    .setCapacity(capacity)
                    .setUsedCapacity(usedCapacity)
                    .setFreeCapacity(freeCapacity)
                    .setUsage(SystemResourceUtils.formatUsage(used * 1.0 / total));
            disks.add(disk);
        }
        return disks;
    }

    /**
     * 格式化使用率
     *
     * @param number 数值
     * @return 格式化使用率
     */
    private static String formatUsage(Double number) {
        if (number.equals(Double.NaN)) {
            return "";
        }
        return new DecimalFormat("#.##%").format(number);
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
