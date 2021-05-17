package com.huawei.health.bloodsugar.util;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;


public class LogUtil {
    private static final String GLOBAL_TAG = "BleOperatorLog_";

    private LogUtil() {}

    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return log
     */
    public static int fatal(String tag, String msg) {
        return HiLog.fatal(getHiLogLabel(tag), msg);
    }

    private static HiLogLabel getHiLogLabel(String tag) {
        return new HiLogLabel(0, 0, GLOBAL_TAG + tag);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return log
     */
    public static int fatal(String tag, String msg, Throwable tr) {
        return HiLog.fatal(getHiLogLabel(tag), msg, tr);
    }

    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return log
     */
    public static int debug(String tag, String msg) {
        return HiLog.debug(getHiLogLabel(tag), msg);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return log
     */
    public static int debug(String tag, String msg, Throwable tr) {
        return HiLog.debug(getHiLogLabel(tag), msg, tr);
    }

    /**
     * Send an log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return log
     */
    public static int info(String tag, String msg) {
        // return HiLog.info(getHiLogLabel(tag), msg);
        System.out.println(tag + "->" + msg);
        return 0;
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return log
     */
    public static int info(String tag, String msg, Throwable tr) {
        return HiLog.info(getHiLogLabel(tag), msg, tr);
    }

    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return log
     */
    public static int warn(String tag, String msg) {
        return HiLog.warn(getHiLogLabel(tag), msg);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return log
     */
    public static int warn(String tag, String msg, Throwable tr) {
        return HiLog.warn(getHiLogLabel(tag), msg, tr);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param tr  An exception to log
     * @return log
     */
    public static int warn(String tag, Throwable tr) {
        return HiLog.warn(getHiLogLabel(tag), "", tr);
    }

    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return log
     */
    public static int error(String tag, String msg) {
        return HiLog.error(getHiLogLabel(tag), msg);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return log
     */
    public static int error(String tag, String msg, Throwable tr) {
        return HiLog.error(getHiLogLabel(tag), msg, tr);
    }
}
