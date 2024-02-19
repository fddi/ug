package top.ulug.base.util;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * Created by liujf on 2019/9/15.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class PhysicalUtils {

    /**
     * @return 服务器空闲内存值
     */
    public static long getFreeMemory() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 总的物理内存+虚拟内存
        //long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        return freePhysicalMemorySize;
    }
}
