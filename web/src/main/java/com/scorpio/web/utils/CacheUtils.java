package com.scorpio.web.utils;

import java.text.MessageFormat;

/**
 * @Author: Scorpio
 */
public class CacheUtils {
    public static String formatKey(String format, Object... args) {
        return MessageFormat.format(format, args);
    }
}
