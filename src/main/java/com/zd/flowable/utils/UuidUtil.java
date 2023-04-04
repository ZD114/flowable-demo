package com.zd.flowable.utils;

import com.fasterxml.uuid.impl.UUIDUtil;
import java.net.InetAddress;

/**
 * UUID生成
 * @author zhangda
 * @date: 2023/2/10
 **/
public class UuidUtil {
    private static String sep = "";

    private static final int IP;

    private static String formattedIP = "";

    private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );

    private static String formattedJVM = "";

    private static short counter = (short) 0;

    static {
        int ipadd;
        try {
            ipadd = toInt( InetAddress.getLocalHost().getAddress() );
        }
        catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
        formattedIP = format( getIP());
        formattedJVM = format( getJVM());
    }

    public static String get32UUID() {
        return formattedIP + sep
                + formattedJVM + sep
                + format( getHiTime() ) + sep
                + format( getLoTime() ) + sep
                + format( getCount() );
    }

    private static String format(int intValue) {
        String formatted = Integer.toHexString( intValue );
        StringBuilder buf = new StringBuilder( "00000000" );
        buf.replace( 8 - formatted.length(), 8, formatted );
        return buf.toString();
    }

    private static String format(short shortValue) {
        String formatted = Integer.toHexString( shortValue );
        StringBuilder buf = new StringBuilder( "0000" );
        buf.replace( 4 - formatted.length(), 4, formatted );
        return buf.toString();
    }

    private static int getJVM() {
        return JVM;
    }

    protected static short getCount() {
        synchronized(UUIDUtil.class) {
            if (counter<0) counter=0;
            return counter++;
        }
    }

    /**
     * Unique in a local network
     */
    private static int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    private static short getHiTime() {
        return (short) ( System.currentTimeMillis() >>> 32 );
    }
    private static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private static int toInt(byte[] bytes) {
        int result = 0;
        for ( int i = 0; i < 4; i++ ) {
            result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

}
