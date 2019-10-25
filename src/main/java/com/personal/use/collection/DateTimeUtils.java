package com.personal.use.collection;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DateTimeUtils
 *
 * @author: shiyan
 * @version: 2019-10-11 11:47
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public enum DateTimeUtils {
    /**
     * 日期格式 <code>[yyyy-MM-dd]</code>
     */
    DATE("yyyy-MM-dd"),

    /**
     * 日期格式 <code>[yyyyMMdd]</code>
     */
    DATE_COMPACT("yyyyMMdd"),

    /**
     * 日期格式 <code>[yyyy_MM_dd]</code>
     */
    DATE_UNDERLINE("yyyy_MM_dd"),

    /**
     * 时间格式 <code>[HH:mm:ss]</code>
     */
    TIME("HH:mm:ss"),

    /**
     * 时间格式 <code>[HHmmss]</code>
     */
    TIME_COMPACT("HHmmss"),

    /**
     * 时间格式 <code>[HH_mm_ss]</code>
     */
    TIME_UNDERLINE("HH_mm_ss"),

    /**
     * 时间格式 <code>[HH:mm:ss.SSS]</code>
     */
    TIME_MILLI("HH:mm:ss.SSS"),

    /**
     * 时间格式 <code>[HHmmssSSS]</code>
     */
    TIME_MILLI_COMPACT("HHmmssSSS"),

    /**
     * 时间格式 <code>[HH_mm_ss_SSS]</code>
     */
    TIME_MILLI_UNDERLINE("HH_mm_ss_SSS"),

    /**
     * 日期时间格式 <code>[yyyy-MM-dd HH:mm:ss]</code>
     */
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * 日期时间格式 <code>[yyyyMMddHHmmss]</code>
     */
    DATE_TIME_COMPACT("yyyyMMddHHmmss"),

    /**
     * 日期时间格式 <code>[yyyy_MM_dd_HH_mm_ss]</code>
     */
    DATE_TIME_UNDERLINE("yyyy_MM_dd_HH_mm_ss"),

    /**
     * 日期时间格式 <code>[yyyy-MM-dd HH:mm:ss.SSS]</code>
     */
    DATE_TIME_MILLI("yyyy-MM-dd HH:mm:ss.SSS"),

    /**
     * 日期时间格式 <code>[yyyyMMddHHmmssSSS]</code>
     */
    DATE_TIME_MILLI_COMPACT("yyyyMMddHHmmssSSS"),

    /**
     * 日期时间格式 <code>[yyyy_MM_dd_HH_mm_ss_SSS]</code>
     */
    DATE_TIME_MILLI_UNDERLINE("yyyy_MM_dd_HH_mm_ss_SSS");


    // --------------------------------------------------------------------------------------------
    private final DateTimeFormatter formatter;

    private DateTimeUtils(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
    }

    private static class CacheHolder {
        private static final int CAPACITY = 8;
        private static final Map<String, DateTimeFormatter> CACHE =
                new LinkedHashMap<String, DateTimeFormatter>(CAPACITY, 1F, true) {
                    private static final long serialVersionUID = -2972235912618490882L;

                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, DateTimeFormatter> eldest) {
                        return size() >= (CAPACITY - 1);
                    }
                };
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String timeString = format.format(date);

        return timeString;
    }

    /**
     * 使用此格式化程序格式化日期时间对象.
     *
     * @param temporal 要格式化的时间对象，不为null
     * @return 格式化的字符串，不能为null
     * @throws DateTimeException 如果格式化期间发生错误
     */
    public String format(TemporalAccessor temporal) {
        return formatter.format(temporal);
    }

    /**
     * 使用此格式化程序格式化日期时间对象.
     *
     * @param date 要格式化的时间对象，不为null
     * @return 格式化的字符串，不能为null
     * @throws DateTimeException 如果格式化期间发生错误
     */
    public String formatDate(Date date) {
        return formatter.format(date.toInstant());
    }

    /**
     * 使用此格式化程序对日期时间对象进行格式化。
     *
     * @param epochMilli the temporal object to format, not null
     * @return the formatted string, not null
     * @throws DateTimeException 如果格式化期间发生错误
     */
    public String formatTimestamp(long epochMilli) {
        return formatter.format(Instant.ofEpochMilli(epochMilli));
    }

    /**
     * 使用此格式化程序格式化日期时间对象.
     *
     * @return the formatted string, not null
     * @throws DateTimeException 如果格式化期间发生错误
     */
    public String formatNow() {
        return formatter.format(Instant.now());
    }


    // --------------------------------------------------------------------------------------------


    /**
     * 完全解析产生指定类型对象的文本.
     *
     * @param date  the text to parse, not null
     * @param query 定义要解析的类型的查询，不为null
     * @return the parsed date-time, not null
     * @throws DateTimeParseException if unable to parse the requested result
     */
    public <R> R parse(String date, TemporalQuery<R> query) {
        return formatter.parse(date, query);
    }

    /**
     * 完全解析产生指定类型对象的文本.
     *
     * @param date  the text to parse, not null
     * @param query 定义要解析的类型的查询，不为null
     * @return the parsed date-time, not null
     * @throws DateTimeException if unable to parse the requested result
     */
    public <R> Instant parseInstant(String date, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).atZone(ZoneId.systemDefault()).toInstant();
        }
        if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (temporal instanceof LocalTime) {
            LocalDate epoch = LocalDate.of(1970, 1, 1); // or LocalDate.now() ???
            LocalDateTime datetime = LocalDateTime.of(epoch, (LocalTime) temporal);
            return datetime.atZone(ZoneId.systemDefault()).toInstant();
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate or LocalTime ");
    }

    /**
     * Fully parses the text producing an object of the specified type.
     *
     * @param date  the text to parse, not null
     * @param query 定义要解析的类型的查询，不为null
     * @return the parsed date-time, not null
     * @throws DateTimeException if unable to parse the requested result
     */
    public <R> Date parseDate(String date, TemporalQuery<R> query) {
        return Date.from(parseInstant(date, query));
    }

    /**
     * Fully parses the text producing an object of the specified type.
     *
     * @param date  the text to parse, not null
     * @param query 定义要解析的类型的查询，不为null
     * @return the parsed time-stamp
     * @throws DateTimeException if unable to parse the requested result
     */
    public <R> long parseTimestamp(String date, TemporalQuery<R> query) {
        return parseInstant(date, query).toEpochMilli();
    }


    // --------------------------------------------------------------------------------------------


    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的年限.
     *
     * @param date  字符串类型的日期
     * @param years the years to add, may be negative
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusYears(String date, long years, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusYears(years));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).plusYears(years));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * Returns a copy of this <tt>date</tt> with the specified number of months added.
     *
     * @param date   字符串类型的日期
     * @param months the months to add, may be negative
     * @param query  定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusMonths(String date, long months, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusMonths(months));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).plusMonths(months));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * Returns a copy of this <tt>date</tt> with the specified number of weeks added.
     *
     * @param date  字符串类型的日期
     * @param weeks 添加的周数，可能为负数
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusWeeks(String date, long weeks, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusWeeks(weeks));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).plusWeeks(weeks));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的天数.
     *
     * @param date  字符串类型的日期
     * @param days  添加的天数，可能为负数
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusDays(String date, long days, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusDays(days));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).plusDays(days));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的小时数.
     *
     * @param date  字符串类型的日期
     * @param hours 添加的小时数，可能为负数
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusHours(String date, long hours, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusHours(hours));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).plusHours(hours));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的分钟数.
     *
     * @param date    字符串类型的日期
     * @param minutes 要添加的分钟数，可能为负数
     * @param query   定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusMinutes(String date, long minutes, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusMinutes(minutes));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).plusMinutes(minutes));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的秒数.
     *
     * @param date    字符串类型的日期
     * @param seconds 添加的秒数，可能为负数
     * @param query   定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusSeconds(String date, long seconds, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusSeconds(seconds));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).plusSeconds(seconds));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中添加了指定的毫秒数.
     *
     * @param date         字符串类型的日期
     * @param milliseconds 要添加的毫秒数，可能为负数
     * @param query        定义要解析的类型的查询，不为null
     * @return 基于此日期时间加上年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String plusMilliseconds(String date, long milliseconds, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).plusNanos(milliseconds * 1000000L));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).plusNanos(milliseconds * 1000000L));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }


    // --------------------------------------------------------------------------------------------


    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的年份.
     *
     * @param date  字符串类型的日期
     * @param years 减去的年份，可能为负
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusYears(String date, long years, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusYears(years));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).minusYears(years));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的月数.
     *
     * @param date   字符串类型的日期
     * @param months the months to subtract, may be negative
     * @param query  定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusMonths(String date, long months, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusMonths(months));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).minusMonths(months));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的周数.
     *
     * @param date  字符串类型的日期
     * @param weeks the weeks to subtract, may be negative
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusWeeks(String date, long weeks, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusWeeks(weeks));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).minusWeeks(weeks));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的天数.
     *
     * @param date  字符串类型的日期
     * @param days  the days to subtract, may be negative
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusDays(String date, long days, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusDays(days));
        }
        if (temporal instanceof LocalDate) {
            return formatter.format(((LocalDate) temporal).minusDays(days));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalDate ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的小时数.
     *
     * @param date  字符串类型的日期
     * @param hours the hours to subtract, may be negative
     * @param query 定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusHours(String date, long hours, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusHours(hours));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).minusHours(hours));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的分钟数.
     *
     * @param date    字符串类型的日期
     * @param minutes 要减去的分钟数，可能为负数
     * @param query   定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusMinutes(String date, long minutes, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusMinutes(minutes));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).minusMinutes(minutes));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的秒数.
     *
     * @param date    字符串类型的日期
     * @param seconds 要减去的秒数，可能为负数
     * @param query   定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusSeconds(String date, long seconds, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusSeconds(seconds));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).minusSeconds(seconds));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }

    /**
     * 返回此<tt> date </ tt>的副本，其中减去指定的毫秒数.
     *
     * @param date         字符串类型的日期
     * @param milliseconds 要减去的毫秒数，可以为负数
     * @param query        定义要解析的类型的查询，不为null
     * @return 基于此日期时间减去年份，不为null
     * @throws DateTimeException 如果结果超出了支持的日期范围
     */
    public <R> String minusMilliseconds(String date, long milliseconds, TemporalQuery<R> query) {
        R temporal = parse(date, query);
        if (temporal instanceof LocalDateTime) {
            return formatter.format(((LocalDateTime) temporal).minusNanos(milliseconds * 1000000L));
        }
        if (temporal instanceof LocalTime) {
            return formatter.format(((LocalTime) temporal).minusNanos(milliseconds * 1000000L));
        }
        throw new DateTimeException(" Type parameter must be LocalDateTime or LocalTime ");
    }


    // --------------------------------------------------------------------------------------------


    /**
     * 更改日期显示的格式
     *
     * @param date    字符串类型的日期
     * @param pattern 日期显示的格式
     * @param query   定义要解析的类型的查询，不为null
     * @return
     * @throws DateTimeException 如果无法解析请求的结果
     */
    public String transform(String date, String pattern, TemporalQuery<?> query) {
        synchronized (CacheHolder.CACHE) {
            if (CacheHolder.CACHE.containsKey(pattern)) {
                return CacheHolder.CACHE.get(pattern).format((TemporalAccessor) formatter.parse(date, query));
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
            String result = dtf.format((TemporalAccessor) formatter.parse(date, query));
            if (pattern.length() == result.length()) {
                CacheHolder.CACHE.putIfAbsent(pattern, dtf);
            }
            return result;
        }
    }
}
