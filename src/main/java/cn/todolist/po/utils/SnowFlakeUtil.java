package cn.todolist.po.utils;

import org.springframework.stereotype.Component;

@Component
public class SnowFlakeUtil {

    // 起始时间戳
    private final static long START_STAMP = 1480166465631L;

    // 每部分的位数
    private final static long SEQUENCE_BIT = 12; // 序列号占用位数
    private final static long MACHINE_BIT = 5; // 机器id占用位数
    private final static long DATACENTER_BIT = 5; // 机房id占用位数

    // 每部分最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    // 每部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private final long datacenterId = 5; // 机房id
    private final long machineId = 5;// 机器id
    private long sequence = 0L; // 序列号
    private long lastStmp = -1L; // 上次的时间戳

    // 产生下一个ID
    public synchronized long getNextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.Refusing to generate id");
        }
        if (currStamp == lastStmp) {
            // 若在相同毫秒内 序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 若在不同毫秒内 则序列号置为0
            sequence = 0L;
        }
        lastStmp = currStamp;

        return (currStamp - START_STAMP) << TIMESTMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT // 机房id部分
                | machineId << MACHINE_LEFT // 机器id部分
                | sequence; // 序列号部分
    }

    // 获取新的毫秒数
    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStmp) {
            mill = getNewStamp();
        }
        return mill;
    }

    // 获取当前的毫秒数
    private long getNewStamp() {
        return System.currentTimeMillis();
    }
}