package com.jfk.common.utils;

/**
 * Created by liubin on 2016/4/20.
 */
public class ZkUtils {

    public static final String ZK_ROOT = "/lepluslife";

    public static String createZkSchedulerLeaderPath(String applicationName) {
        return String.format("%s/%s/schedulers", ZK_ROOT, applicationName);
    }

}
