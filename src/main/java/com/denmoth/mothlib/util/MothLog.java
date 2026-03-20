package com.denmoth.mothlib.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class MothLog {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static String prefix = "[MothLib] ";

    public static void setPrefix(String modId) {
        prefix = "[" + modId + "] ";
    }

    public static void info(String message, Object... params) {
        LOGGER.info(prefix + message, params);
    }

    public static void warn(String message, Object... params) {
        LOGGER.warn(prefix + message, params);
    }

    public static void error(String message, Object... params) {
        LOGGER.error(prefix + message, params);
    }

    public static void debug(String message, Object... params) {
        LOGGER.debug(prefix + message, params);
    }
}