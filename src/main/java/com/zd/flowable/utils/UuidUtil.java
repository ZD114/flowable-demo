package com.zd.flowable.utils;

import java.util.UUID;

/**
 * UUID生成
 * @author zhangda
 * @date: 2023/2/10
 **/
public class UuidUtil {

    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
