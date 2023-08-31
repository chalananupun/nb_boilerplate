package com.fidenz.android_boilerplate.utility;

import android.app.Activity;

import com.fidenz.android_boilerplate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtility {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat monthSDF = new SimpleDateFormat("MM");
    private final static SimpleDateFormat yearSDF = new SimpleDateFormat("yy");
    private final static SimpleDateFormat daySDF = new SimpleDateFormat("dd");
    private final static SimpleDateFormat hourSDF = new SimpleDateFormat("hh");
    private final static SimpleDateFormat minsSDF = new SimpleDateFormat("mm");
    private final static SimpleDateFormat amSDF = new SimpleDateFormat("aa");

    private final static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    private final static SimpleDateFormat serverDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
    private final static SimpleDateFormat serverDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
    private final static SimpleDateFormat serverDateFormat3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
    private final static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.ENGLISH);
    private final static SimpleDateFormat sdf4 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private final static SimpleDateFormat sdf5 = new SimpleDateFormat("dd MMM yyyy | HH:mm a", Locale.ENGLISH);
    private static SimpleDateFormat sdf6 = new SimpleDateFormat("E, dd MMM",Locale.ENGLISH);
    private final static SimpleDateFormat sdf7 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    private final static SimpleDateFormat timeWithAM_PM = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);


    public static String getStringDateFormatted(String date, String outputFormat,Locale locale) {

        String day = "";
        if (date != null) {
            try {
                Date date1 = serverDateFormat2.parse(date);
                day = new SimpleDateFormat(outputFormat, locale).format(date1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return day;
    }

    public static String getStringDateFormattedWithZ(String date, String outputFormat,Locale locale) {

        String day = "";
        if (date != null) {
            try {
                Date date1 = serverDateFormat3.parse(date);
                day = new SimpleDateFormat(outputFormat, locale).format(date1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return day;
    }


    public static String getUTCDateTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static String getSimpleDate(String date) {
        String simpleDate = "";
        try {
            Date current = sdf.parse(date);
            simpleDate = sdf.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String convertStringDate(String date, String fromFormat, String toFormat) {
        String simpleDate = "";
        try {
            Date current = new SimpleDateFormat(fromFormat).parse(date);
            simpleDate = new SimpleDateFormat(toFormat).format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getMonth(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = monthSDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }


    public static Date getStringToDate(String date) {
        Date date1 = null;
        try {
            date1 = sdf2.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }


    public static String getYear(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = yearSDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getDay(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = daySDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getHour(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = hourSDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }


    public static String getMins(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = minsSDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }


    public static String getAmPm(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = amSDF.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }


    public static String getDateToUTCDate(Date date) {
        return sdf2.format(date);
    }




    public static String getSimpleDateFromDateTime(String date) {
        String simpleDate = "";
        try {
            Date current = serverDateFormat.parse(date);
            simpleDate = sdf.format(current);
            return simpleDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getSimpleDateTimeAsMMMFormatForSchedule(Date date) {
        String simpleDate = "";
        try {
            simpleDate = sdf5.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getSimpleDateTime(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = sdf3.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getServerDateToSimpleDate(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = sdf3.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String geServer2DateToSimpleDate(String date) {
        String simpleDate = "";
        try {
            Date current = serverDateFormat2.parse(date);
            simpleDate = sdf3.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }


    public static String getSimpleDateTimeAsMMMFormat(String date) {
        String simpleDate = "";
        try {
            Date current = sdf2.parse(date);
            simpleDate = sdf4.format(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDate;
    }

    public static String getSimpleDateTime(Date date) {
        return sdf2.format(date);
    }

    public static Date getDate() {
        return new Date();
    }

    public static String getMorningOrEvening(Activity activity) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            return activity.getString(R.string.good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return activity.getString(R.string.good_afternoon);
        } else {
            return activity.getString(R.string.good_evening);
        }
    }

    public static String getDateWithoutYear(Locale locale) {
        sdf6 = new SimpleDateFormat("E, dd MMM", locale);
        return sdf6.format(getDate());
    }

    public static String addMinutesToTime(int minutesToAdd) {
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, minutesToAdd);
        return sdf7.format(cal.getTime());
    }


    public static String splitMinutesToHorusAndMins(int mins, Activity activity) {
        if (mins == 0) {
            return 0 + " " + activity.getString(R.string.min);
        }
        int hoursI = (mins / 60);
        int minsI = (mins % 60);

        String hours = (hoursI + " " + (hoursI > 1 ? activity.getString(R.string.hour) : activity.getString(R.string.hours)));
        String minutes = (minsI + " " + (minsI > 1 ? activity.getString(R.string.min) : activity.getString(R.string.min)));


        if (hoursI > 0 && minsI > 0) {
            return hours + " " + minutes;
        } else if (hoursI > 0) {
            return hours;
        } else if ((minsI > 0)) {
            return minutes;
        } else {
            return null;
        }


    }

    public static long getSystemTimeAsLong() {
        return System.currentTimeMillis();
    }

}
