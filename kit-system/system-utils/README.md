# system-utils 模块说明

> 系统 utils 模块

## 功能说明

* 读取系统相关资源
  * OS
  * CPU
  * Disk
  * JDK
  * JVM
  * Mem

## 使用

```java

public class SystemResourceUtilsTest {

  public static void main(String[] args) {
    SystemResource resource = SystemResourceUtils.read();
    System.out.println(JSON.toJSONString(resource));

    System.out.println();
  }

}

```

效果如下：

```log

{
    "cpu": {
        "ioWaitUsage": "0%",
        "logicalProcessorCount": 12,
        "microArchitecture": "Coffee Lake",
        "model": "Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz",
        "physicalProcessorCount": 6,
        "systemUsage": "2.81%",
        "totalUsage": "6.04%",
        "userUsage": "3.23%"
    },
    "disks": [
        {
            "capacity": "465.63GB",
            "freeCapacity": "193.22GB",
            "mount": "/",
            "name": "Macintosh HD",
            "type": "apfs",
            "usage": "58.5%",
            "usedCapacity": "272.41GB"
        },
        {
            "capacity": "465.63GB",
            "freeCapacity": "193.22GB",
            "mount": "/System/Volumes/VM",
            "name": "VM",
            "type": "apfs",
            "usage": "58.5%",
            "usedCapacity": "272.41GB"
        },
        {
            "capacity": "465.63GB",
            "freeCapacity": "193.22GB",
            "mount": "/System/Volumes/Preboot",
            "name": "Preboot",
            "type": "apfs",
            "usage": "58.5%",
            "usedCapacity": "272.41GB"
        },
        {
            "capacity": "465.63GB",
            "freeCapacity": "193.22GB",
            "mount": "/System/Volumes/Update",
            "name": "Update",
            "type": "apfs",
            "usage": "58.5%",
            "usedCapacity": "272.41GB"
        },
        {
            "capacity": "465.63GB",
            "freeCapacity": "193.22GB",
            "mount": "/System/Volumes/Data",
            "name": "Macintosh HD - 数据",
            "type": "apfs",
            "usage": "58.5%",
            "usedCapacity": "272.41GB"
        }
    ],
    "jdk": {
        "home": "/Library/Java/JavaVirtualMachines/jdk1.8.0_271.jdk/Contents/Home/jre",
        "name": "Java HotSpot(TM) 64-Bit Server VM",
        "runHome": "/Users/pengsenhao/IdeaProjects/PONGSKY/java-enhancement-kit",
        "version": "1.8.0_271"
    },
    "jvm": {
        "capacity": "245.5MB",
        "freeCapacity": "232.7MB",
        "usage": "5.21%",
        "usedCapacity": "12.8MB"
    },
    "mem": {
        "capacity": "16GB",
        "freeCapacity": "5.92GB",
        "usage": "63.03%",
        "usedCapacity": "10.08GB"
    },
    "os": {
        "arch": "x86_64",
        "hostAddress": "127.0.0.1",
        "hostName": "PONGSKYdeMacBook-Pro.local",
        "name": "Mac OS X"
    }
}

```