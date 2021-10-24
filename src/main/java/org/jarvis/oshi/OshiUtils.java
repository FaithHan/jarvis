package org.jarvis.oshi;


import oshi.SystemInfo;
import oshi.driver.linux.proc.CpuInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.Sensors;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * Github: https://github.com/oshi/oshi
 * 这个库可以监测的内容包括：
 *
 *      计算机系统和固件，底板
 *      操作系统和版本/内部版本
 *      物理（核心）和逻辑（超线程）CPU，处理器组，NUMA节点
 *      系统和每个处理器的负载百分比和滴答计数器
 *      CPU正常运行时间，进程和线程
 *      进程正常运行时间，CPU，内存使用率，用户/组，命令行
 *      已使用/可用的物理和虚拟内存
 *      挂载的文件系统（类型，可用空间和总空间）
 *      磁盘驱动器（型号，序列号，大小）和分区
 *      网络接口（IP，带宽输入/输出）
 *      电池状态（电量百分比，剩余时间，电量使用情况统计信息）
 *      连接的显示器（带有EDID信息）
 *      USB设备
 *      传感器（温度，风扇速度，电压）
 */
public abstract class OshiUtils {

    private static final SystemInfo systemInfo;
    /**
     * 硬件信息
     */
    private static final HardwareAbstractionLayer hardware;
    /**
     * 系统信息
     */
    private static final OperatingSystem os;

    static {
        systemInfo = new SystemInfo();
        hardware = systemInfo.getHardware();
        os = systemInfo.getOperatingSystem();
    }

    /**
     * 获取操作系统相关信息，包括系统版本、文件系统、进程等
     *
     * @return 操作系统相关信息
     */
    public static OperatingSystem getOs() {
        return os;
    }

    /**
     * 获取当前进程信息{@link OSProcess}
     *
     * @return 进程信息 {@link OSProcess}
     * @since 5.7.12
     */
    public static OSProcess getCurrentProcess() {
        return os.getProcess(os.getProcessId());
    }

    /**
     * 获取硬件相关信息，包括内存、硬盘、网络设备、显示器、USB、声卡等
     *
     * @return 硬件相关信息
     */
    public static HardwareAbstractionLayer getHardware() {
        return hardware;
    }

    /**
     * 获取BIOS中计算机相关信息，比如序列号、固件版本等
     *
     * @return 获取BIOS中计算机相关信息
     */
    public static ComputerSystem getSystem() {
        return hardware.getComputerSystem();
    }

    /**
     * 获取内存相关信息，比如总内存、可用内存等
     *
     * @return 内存相关信息
     */
    public static GlobalMemory getMemory() {
        return hardware.getMemory();
    }

    /**
     * 获取CPU（处理器）相关信息，比如CPU负载等
     *
     * @return CPU（处理器）相关信息
     */
    public static CentralProcessor getProcessor() {
        return hardware.getProcessor();
    }

    /**
     * 获取传感器相关信息，例如CPU温度、风扇转速等，传感器可能有多个
     *
     * @return 传感器相关信息
     */
    public static Sensors getSensors() {
        return hardware.getSensors();
    }

    /**
     * 获取磁盘相关信息，可能有多个磁盘（包括可移动磁盘等）
     *
     * @return 磁盘相关信息
     * @since 5.3.6
     */
    public static List<HWDiskStore> getDiskStores() {
        return hardware.getDiskStores();
    }

    /**
     * 获取网络相关信息，可能多块网卡
     *
     * @return 网络相关信息
     * @since 5.3.6
     */
    public static List<NetworkIF> getNetworkIFs() {
        return hardware.getNetworkIFs();
    }

}
