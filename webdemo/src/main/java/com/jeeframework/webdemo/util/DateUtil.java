/**
 * @project: com.hyfaycrawler
 * @Title: RuleAction.java
 * @Package: com.hyfaycrawler.web.action
 * @author tag
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 * 
 */
package com.jeeframework.webdemo.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeeframework.util.string.StringUtils;
import com.jeeframework.util.validate.Validate;

/**
 * @project: com.hyfaycrawler
 * @Title: RuleAction.java
 * @Package: com.hyfaycrawler.web.action
 * @author tag Copyright (c) 2014-2014 Transing Limited, Inc. All rights reserved.
 * 
 */
public class DateUtil {

    private final static String dateRegx = "\\d{4}(年|-|\\/|\\.)\\d{1,2}(月|-|\\/|\\.)\\d{1,2}(日|-|\\/)?(\\s+\\d{1,2}:\\d{1,2}(:\\d{1,2})*)*";

    /**
     * 简单描述：传入一个日期字符串，将其转换成日期格式<br>
     * 
     * @param strDate strDate
     * @return Date
     */
    public static Date parseDate(String strDate) {
        String strDateStr = RegexUtil.matchRegx(strDate, dateRegx, false);
        if (Validate.isEmpty(strDateStr)) {
            strDateStr = strDate.replaceAll("([\\r\\t\\n]+)", "");
        }

        strDateStr = strDateStr.replaceAll(" ", " ");
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        try {
            strDateStr = strDateStr.replaceAll("－", "-");
            if (strDateStr.indexOf("AM") != -1) {
                strDateStr = strDateStr.replaceAll("AM", "");
            }
            if (strDateStr.indexOf("PM") != -1) {
                strDateStr = strDateStr.replaceAll("PM", "");
            }
            strDateStr = strDateStr.trim();
        } catch (Exception e) {
        }
        try {
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yy-MM-dd HH:mm");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yy-MM-dd");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy-MM-dd HH:mm");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy/MM/dd HH:mm");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy.MM.dd HH:mm:ss");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy.MM.dd");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy-MM-dd");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("yyyy/MM/dd");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("M/d/yyyy HH:mm:ss");
            return dateFormat.parse(strDateStr);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("MM/dd HH:mm");
            Date parseDate = dateFormat.parse(strDateStr);
            return caculateYear(parseDate);
        } catch (ParseException e) {
        }
        try {
            dateFormat.applyPattern("MM-dd HH:mm");
            Date parseDate = dateFormat.parse(strDateStr);
            return caculateYear(parseDate);
        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("MM月dd日 HH:mm");
            Date parseDate = dateFormat.parse(strDateStr);
            return caculateYear(parseDate);

        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("yyyy年MM月dd日HH:mm:ss");
            return dateFormat.parse(strDateStr);

        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("yyyy年MM月dd日 HH:mm");
            return dateFormat.parse(strDateStr);

        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("yyyy年MM月dd日");
            return dateFormat.parse(strDateStr);

        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("yyyy年");
            Date parseDate = dateFormat.parse(strDateStr);
            return caculateYearDay(parseDate);
        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("MM月dd日");
            Date parseDate = dateFormat.parse(strDateStr);
            return caculateYear(parseDate);

        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        String[] dateArr = strDateStr.split("-");

        if (dateArr.length > 1) {

            String firstWd = dateArr[0];

            if (firstWd.length() == 4) {

                try {
                    dateFormat.applyPattern("yyyy-MM");
                    Date parseDate = dateFormat.parse(strDateStr);
                    return caculateYear(parseDate);

                } catch (ParseException e) {
                    // 没有匹配到，忽略掉
                }
            } else if (firstWd.length() == 2) {
                try {
                    dateFormat.applyPattern("MM-dd");
                    Date parseDate = dateFormat.parse(strDateStr);
                    return caculateYear(parseDate);

                } catch (ParseException e) {
                    // 没有匹配到，忽略掉
                }
            }
        }
        // 只有小时的需要进一步判断是否是当前的
        try {
            dateFormat.applyPattern("HH:mm:ss");
            Date parseDate = dateFormat.parse(strDateStr);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(parseDate);

            int hour = c1.get(Calendar.HOUR_OF_DAY);
            int minute = c1.get(Calendar.MINUTE);
            int second = c1.get(Calendar.SECOND);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int curYear = c2.get(Calendar.YEAR);
            int curMonth = c2.get(Calendar.MONTH);
            int curDay = c2.get(Calendar.DAY_OF_MONTH);
            int curHour = c2.get(Calendar.HOUR_OF_DAY);
            int curMinute = c2.get(Calendar.MINUTE);
            int curSecond = c2.get(Calendar.SECOND);

            Calendar g1 = Calendar.getInstance();
            g1.set(curYear, curMonth, curDay, hour, minute, second);
            Date d1 = g1.getTime();

            Calendar g2 = Calendar.getInstance();
            g2.set(curYear, curMonth, curDay, curHour, curMinute, curSecond);
            Date d2 = g2.getTime();

            if (d1.compareTo(d2) >= 0) {
                g1.set(curYear, curMonth, curDay - 1, hour, minute, second);
                return g1.getTime();
            }

            return caculateYearDay(parseDate);
        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }
        try {
            dateFormat.applyPattern("HH:mm");
            Date parseDate = dateFormat.parse(strDateStr);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(parseDate);

            int hour = c1.get(Calendar.HOUR_OF_DAY);
            int minute = c1.get(Calendar.MINUTE);
            int second = c1.get(Calendar.SECOND);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int curYear = c2.get(Calendar.YEAR);
            int curMonth = c2.get(Calendar.MONTH);
            int curDay = c2.get(Calendar.DAY_OF_MONTH);
            int curHour = c2.get(Calendar.HOUR_OF_DAY);
            int curMinute = c2.get(Calendar.MINUTE);
            int curSecond = c2.get(Calendar.SECOND);

            Calendar g1 = Calendar.getInstance();
            g1.set(curYear, curMonth, curDay, hour, minute, second);
            Date d1 = g1.getTime();

            Calendar g2 = Calendar.getInstance();
            g2.set(curYear, curMonth, curDay, curHour, curMinute, curSecond);
            Date d2 = g2.getTime();

            if (d1.compareTo(d2) >= 0) {
                g1.set(curYear, curMonth, curDay - 1, hour, minute, second);
                return g1.getTime();
            }

            return caculateYearDay(parseDate);
        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        try {
            dateFormat.applyPattern("HH : mm");
            Date parseDate = dateFormat.parse(strDateStr);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(parseDate);

            int hour = c1.get(Calendar.HOUR_OF_DAY);
            int minute = c1.get(Calendar.MINUTE);
            int second = c1.get(Calendar.SECOND);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int curYear = c2.get(Calendar.YEAR);
            int curMonth = c2.get(Calendar.MONTH);
            int curDay = c2.get(Calendar.DAY_OF_MONTH);
            int curHour = c2.get(Calendar.HOUR_OF_DAY);
            int curMinute = c2.get(Calendar.MINUTE);
            int curSecond = c2.get(Calendar.SECOND);

            Calendar g1 = Calendar.getInstance();
            g1.set(curYear, curMonth, curDay, hour, minute, second);
            Date d1 = g1.getTime();

            Calendar g2 = Calendar.getInstance();
            g2.set(curYear, curMonth, curDay, curHour, curMinute, curSecond);
            Date d2 = g2.getTime();

            if (d1.compareTo(d2) >= 0) {
                g1.set(curYear, curMonth, curDay - 1, hour, minute, second);
                return g1.getTime();
            }

            return caculateYearDay(parseDate);
        } catch (ParseException e) {
            // 没有匹配到，忽略掉
        }

        Date date = transformDate(strDateStr);
        if (null != date) {
            return date;
        }

        if (Validate.isNumeric(strDateStr)) {
            // 1357701898000
            // 1357701898
            Timestamp unixTime = null;
            if (strDateStr.length() == 13) {
                unixTime = new Timestamp(Long.valueOf(strDateStr));
            } else {
                unixTime = new Timestamp(Long.valueOf(strDateStr) * 1000);
            }
            try {
                dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                return dateFormat.parse(unixTime.toString());
            } catch (ParseException e) {
                // 没有匹配到，忽略掉
            }
        }
        return null;
    }

    /**
     * 简单描述：34 秒前 2 分钟前 6 小时前 昨天 20:48 前天 10:26 3 天前 17:40
     * 
     * @param weiboTime weiboTime
     * @return weiboTime
     */
    public static Date transformDate(String weiboTime) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            String weiboTimeStr = weiboTime.replaceAll("[\\s|\\\\?]", "");
            String[] $infoCreateTime = weiboTimeStr.split(" ");
            if ($infoCreateTime.length == 1) {
                $infoCreateTime[0] = $infoCreateTime[0].replaceAll("[\\s|\\\\?]", "");
                if ($infoCreateTime[0].indexOf("年") != -1) {
                    int year;
                    try {
                        year = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("年")));
                    } catch (Exception e) {
                        year = 0;
                    }
                    c.add(Calendar.YEAR, -year);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("个月") != -1) {
                    int month;
                    try {
                        month = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("个月")));
                    } catch (Exception e) {
                        month = 0;
                    }
                    c.add(Calendar.MONTH, -month);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("月") != -1) {
                    int month;
                    try {
                        month = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("月")));
                    } catch (Exception e) {
                        month = 0;
                    }
                    c.add(Calendar.MONTH, -month);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("周") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("周")));
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("个星期") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("个星期")));
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("星期") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("星期")));
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("今天") != -1) {
                    String hour = org.apache.commons.lang3.StringUtils.substringAfter($infoCreateTime[0], "今天");
                    hour = StringUtils.toDBC(hour.trim());
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + hour + ":00";
                    ;
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
                if ($infoCreateTime[0].indexOf("昨天") != -1) {
                    String hour = org.apache.commons.lang3.StringUtils.substringAfter($infoCreateTime[0], "昨天");
                    hour = StringUtils.toDBC(hour.trim());
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + hour + ":00";
                    ;
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
                if ($infoCreateTime[0].indexOf("前天") != -1) {
                    String hour = org.apache.commons.lang3.StringUtils.substringAfter($infoCreateTime[0], "前天");
                    hour = StringUtils.toDBC(hour.trim());
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + hour + ":00";
                    ;
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
                if ($infoCreateTime[0].indexOf("天") != -1) {
                    int day;
                    try {
                        day = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("天")));
                    } catch (Exception e) {
                        day = 0;
                    }
                    c.add(Calendar.DAY_OF_MONTH, -day);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("个小时") != -1) {
                    int hour;
                    try {
                        hour = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("个小时")));
                    } catch (Exception e) {
                        hour = 0;
                    }
                    c.add(Calendar.HOUR_OF_DAY, -hour);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("小时") != -1) {
                    int hour;
                    try {
                        hour = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("小时")));
                    } catch (Exception e) {
                        hour = 0;
                    }
                    c.add(Calendar.HOUR_OF_DAY, -hour);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("分钟") != -1) {
                    int minute;
                    try {
                        minute = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("分钟")));
                    } catch (Exception e) {
                        minute = 0;
                    }
                    c.add(Calendar.MINUTE, -minute);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("秒") != -1) {
                    int second;
                    try {
                        second = Integer.valueOf($infoCreateTime[0].substring(0, $infoCreateTime[0].indexOf("秒")));
                    } catch (Exception e) {
                        second = 0;
                    }
                    c.add(Calendar.SECOND, -second);
                    return c.getTime();
                }
            }
            if ($infoCreateTime.length == 2) {
                $infoCreateTime[0] = $infoCreateTime[0].replaceAll("\\s", "");
                $infoCreateTime[1] = $infoCreateTime[1].replaceAll("\\s", "");
                if ($infoCreateTime[1].indexOf("年") != -1) {
                    int year;
                    try {
                        year = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        year = 0;
                    }
                    c.add(Calendar.YEAR, -year);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("个月") != -1) {
                    int month;
                    try {
                        month = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        month = 0;
                    }
                    c.add(Calendar.MONTH, -month);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("月") != -1) {
                    int month;
                    try {
                        month = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        month = 0;
                    }
                    c.add(Calendar.MONTH, -month);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("周") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("个星期") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("星期") != -1) {
                    int week;
                    try {
                        week = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        week = 0;
                    }
                    c.add(Calendar.WEEK_OF_YEAR, -week);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("天") != -1) {
                    int day;
                    try {
                        day = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        day = 0;
                    }
                    c.add(Calendar.DAY_OF_MONTH, -day);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("秒") != -1) {
                    int second;
                    try {
                        second = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        second = 0;
                    }
                    c.add(Calendar.SECOND, -second);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("分") != -1) {
                    int minute;
                    try {
                        minute = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        minute = 0;
                    }
                    c.add(Calendar.MINUTE, -minute);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("个小时") != -1) {
                    int hour;
                    try {
                        hour = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        hour = 0;
                    }
                    c.add(Calendar.HOUR_OF_DAY, -hour);
                    return c.getTime();
                }
                if ($infoCreateTime[1].indexOf("小时") != -1) {
                    int hour;
                    try {
                        hour = Integer.valueOf($infoCreateTime[0]);
                    } catch (Exception e) {
                        hour = 0;
                    }
                    c.add(Calendar.HOUR_OF_DAY, -hour);
                    return c.getTime();
                }
                if ($infoCreateTime[0].indexOf("今天") != -1) {
                    String hour = org.apache.commons.lang3.StringUtils.substringAfter($infoCreateTime[0], "今天");
                    hour = StringUtils.toDBC(hour.trim());
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + hour + ":00";
                    ;
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
                if ($infoCreateTime[0].indexOf("昨天") != -1) {
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + $infoCreateTime[1] + ":00";
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
                if ($infoCreateTime[0].indexOf("前天") != -1) {
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    format.applyPattern("yyyy-MM-dd");
                    String date = format.format(c.getTime()) + " " + $infoCreateTime[1] + ":00";
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    return format.parse(date);
                }
            }
            if ($infoCreateTime.length == 3) {
                int day = Integer.valueOf($infoCreateTime[0]);
                c.add(Calendar.DAY_OF_MONTH, -day);
                format.applyPattern("yyyy-MM-dd");
                String date = format.format(c.getTime()) + " " + $infoCreateTime[2] + ":00";
                format.applyPattern("yyyy-MM-dd HH:mm:ss");
                return format.parse(date);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 简单描述：转换微博时间，格式列举如下： 1秒前 1分钟前 今天 13:57 1月3日 11:34 2010年12月27日 21:58 2009-12-24 23:38
     * 
     * @param weiboTime weiboTime
     * @return String
     */
    public static String transformWeiBoDate(String weiboTime) {

        Date curDate = new Date();
        String date = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            String[] $infoCreateTime = weiboTime.split(" ");
            int pos;
            if ((pos = $infoCreateTime[0].indexOf("秒")) != -1) {
                // 1秒前
                int second;
                try {
                    second = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    second = 0;
                }
                c.add(Calendar.SECOND, -second);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ((pos = $infoCreateTime[0].indexOf("分")) != -1) {
                // 1分钟前
                int minute;
                try {
                    minute = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    minute = 0;
                }
                c.add(Calendar.MINUTE, -minute);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ((pos = $infoCreateTime[0].indexOf("小时")) != -1) {
                // 1小时前
                int hour;
                try {
                    hour = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    hour = 0;
                }
                c.add(Calendar.HOUR_OF_DAY, -hour);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ($infoCreateTime[0].indexOf("今天") != -1) {
                // 今天 13:57
                format.applyPattern("yyyy-MM-dd");
                date = format.format(c.getTime()) + " " + $infoCreateTime[1] + ":00";

                Date weiboDate = DateUtil.parseDate(date);
                if (weiboDate == null) {
                    weiboDate = new Date();
                }
                c.setTime(weiboDate);

                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                format.applyPattern("yyyy-MM-dd HH:mm:ss");
                date = format.format(c.getTime());

                return date;
            }
            Date d = DateUtil.parseDate(weiboTime);
            if (null != d) {
                return format.format(d);
            }
            date = format.format(c.getTime());
        } catch (Exception e) {
            date = format.format(c.getTime());
        }

        return date;
    }

    /**
     * 简单描述：转换微博时间，格式列举如下： 1秒前 1分钟前 今天 13:57 1月3日 11:34 2010年12月27日 21:58 2009-12-24 23:38
     * <p>
     * 详细描述：
     * 
     * @param weiboTime weiboTime
     * @return String
     */
    public static String transformWeiBoDateForHexunTest(String weiboTime) {

        Date curDate = new Date();
        String date = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            String[] $infoCreateTime = weiboTime.split(" ");
            int pos;
            if ((pos = $infoCreateTime[0].indexOf("秒")) != -1) {
                // 1秒前
                int second;
                try {
                    second = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    second = 0;
                }
                c.add(Calendar.SECOND, -second);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ((pos = $infoCreateTime[0].indexOf("分")) != -1) {
                // 1分钟前
                int minute;
                try {
                    minute = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    minute = 0;
                }
                c.add(Calendar.MINUTE, -minute);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ((pos = $infoCreateTime[0].indexOf("小时")) != -1) {
                // 1小时前
                int hour;
                try {
                    hour = Integer.valueOf($infoCreateTime[0].substring(0, pos));
                } catch (Exception e) {
                    hour = 0;
                }
                c.add(Calendar.HOUR_OF_DAY, -hour);

                Date weiboDate = c.getTime();
                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                date = format.format(c.getTime());
                return date;
            }
            if ($infoCreateTime[0].indexOf("今天") != -1) {
                // 今天 13:57
                format.applyPattern("yyyy-MM-dd");
                date = format.format(c.getTime()) + " " + $infoCreateTime[1] + ":00";

                Date weiboDate = DateUtil.parseDate(date);
                if (weiboDate == null) {
                    weiboDate = new Date();
                }
                c.setTime(weiboDate);

                if (weiboDate.after(curDate)) {
                    // 微博时间比当前时间晚，就应该是前一天的时间
                    c.add(Calendar.DATE, -1);
                }

                format.applyPattern("yyyy-MM-dd HH:mm:ss");
                date = format.format(c.getTime());

                return date;
            }
            Date d = DateUtil.parseDate(weiboTime);
            if (null != d) {
                return format.format(d);
            }

            // 不会返回值，因为可能解析为空

            // date = format.format(c.getTime());
        } catch (Exception e) {
            // date = format.format(c.getTime());
        }

        return date;
    }

    /**
     * 简单描述：得到星期几
     * 
     * @param curDate curDate
     * @return int
     */
    public static int getWeekChineseIndex(Date curDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        int i = c.get(Calendar.DAY_OF_WEEK);
        int s = 0;
        if (i == 1) {
            s = 7;
        } else {
            s = i - 1;
        }
        return s;
    }

    /**
     * 简单描述：得到星期几
     * 
     * @param curDate curDate
     * @return String
     */
    public static String getWeekChinese(Date curDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        int i = c.get(Calendar.DAY_OF_WEEK);
        String s = "星期";
        switch (i) {
            case 1:
                s = s + "日";
                break;
            case 2:
                s = s + "一";
                break;
            case 3:
                s = s + "二";
                break;
            case 4:
                s = s + "三";
                break;
            case 5:
                s = s + "四";
                break;
            case 6:
                s = s + "五";
                break;
            case 7:
                s = s + "六";
                break;
            default:
                break;
        }
        return s;
    }

    /**
     * <p>
     * 
     * @param parseDate p
     * @return tag
     */
    private static Date caculateYearDay(Date parseDate) {
        int curYear = 0;
        int curMonth = 0;
        int curDay = 0;
        try {

            // 判断是否带年,不带年份的，月份大于当前月份，默认为去年
            Calendar g = Calendar.getInstance();
            curYear = g.get(Calendar.YEAR);
            curMonth = g.get(Calendar.MONTH);
            curDay = g.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            // 没有匹配到，忽略掉
        }

        Calendar g = Calendar.getInstance();
        g.setTime(parseDate);
        g.set(Calendar.YEAR, curYear);
        g.set(Calendar.MONTH, curMonth);
        g.set(Calendar.DAY_OF_MONTH, curDay);

        return g.getTime();
    }

    /**
     * 简单描述：计算两个日期间的小时数
     * 
     * @param parseDate parseDate
     * @param compareDate compareDate
     * @return int
     */
    public static int caculateHour(Date parseDate, Date compareDate) {
        int curHour = 0;
        try {

            // 判断是否带年,不带年份的，月份大于当前月份，默认为去年
            Calendar g = Calendar.getInstance();
            g.setTime(parseDate);
            curHour = g.get(Calendar.HOUR_OF_DAY);
        } catch (Exception e) {
            // 没有匹配到，忽略掉
        }

        Calendar g = Calendar.getInstance();
        g.setTime(compareDate);
        int compareHour = g.get(Calendar.HOUR_OF_DAY);

        return compareHour - curHour;
    }

    /**
     * 简单描述：比较时间，返回两者时间相隔的小时
     * 
     * @param nowDate nowDate
     * @param compareDate compareDate
     * @return long
     */
    public static long compareHour(Date nowDate, Date compareDate) {
        long curHour = 0;
        try {
            long diff = nowDate.getTime() - compareDate.getTime();
            curHour = diff / (1000 * 60 * 60);
        } catch (Exception e) {
        }

        return curHour;
    }

    /**
     * 简单描述：比较时间，返回两者时间相隔的天数
     * <p>
     * 详细描述：
     * 
     * @param nowDate nowDate
     * @param compareDate compareDate
     * @return long
     */
    public static long compareDay(Date nowDate, Date compareDate) {
        long curDay = 0;
        try {
            long diff = nowDate.getTime() - compareDate.getTime();
            curDay = diff / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
        }

        return curDay;
    }

    /**
     * 简单描述：返回两者时间相隔的秒数
     * 
     * @param curDate curDate
     * @param compareDate compareDate
     * @return long
     */
    public static long getDiffSecond(Date curDate, Date compareDate) {
        long curSecond = 0;
        try {
            long diff = curDate.getTime() - compareDate.getTime();
            curSecond = diff / 1000;
        } catch (Exception e) {
        }

        return curSecond;
    }

    /**
     * <p>
     * 
     * @param parseDate p
     * @return tag
     */
    private static Date caculateYear(Date parseDate) {
        int curYear = 0;
        int curMonth = 0;
        int curDay = 0;
        try {

            // 判断是否带年,不带年份的，月份大于当前月份，默认为去年
            Calendar g = Calendar.getInstance();
            curYear = g.get(Calendar.YEAR);
            curMonth = g.get(Calendar.MONTH);
            curDay = g.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            // 没有匹配到，忽略掉
        }

        Calendar g = Calendar.getInstance();
        g.setTime(parseDate);
        int parseMonth = g.get(Calendar.MONTH);

        if (curYear > 0 && curMonth >= 0 && parseMonth >= 0) {
            // 月份大于当前月份，默认为去年
            if (parseMonth > curMonth) {
                g.set(Calendar.YEAR, curYear - 1);
            } else if (parseMonth == curMonth) {
                int parseDay = g.get(Calendar.DAY_OF_MONTH);
                if (parseDay > curDay) {
                    g.set(Calendar.YEAR, curYear - 1);
                }
            }
        }

        int parseYear = g.get(Calendar.YEAR);
        if (parseYear == 1970) {
            g.set(Calendar.YEAR, curYear);
        }

        return g.getTime();
    }

    /**
     * 简单描述：传入一个日期字符串，将其转换成日期格式<br>
     * 
     * @param boolean 是否是日期格式
     * @return Date
     */
    public static boolean isDate(String strDate) {

        boolean isFind = false;
        Pattern p = Pattern.compile(dateRegx);

        Matcher m = p.matcher(strDate);// 开始编译
        while (m.find()) {
            isFind = true;
            break;
        }
        return isFind;
    }

    public static void main(String[] args) {
//        System.out.println(" date = " + DateUtil.parseDate("tieba.baidu.com/a?fr=ala0 2014-07-10 "));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1970);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();

        System.out.println(time.getTime());


    }
}
