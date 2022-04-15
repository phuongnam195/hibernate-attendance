package util;

import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formatWeekday(int weekday) {
        if (weekday == 1) return "Chủ nhật";
        if (weekday >= 2 && weekday <= 7) return "Thứ " + String.valueOf(weekday);
        return "";
    }

    public static String formatTime(Time time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(time);
    }

    public static DefaultFormatterFactory getFormatterFactory(String pattern) {
        DateFormat timeFormat = new SimpleDateFormat(pattern);
        DateFormatter timeFormatter = new DateFormatter(timeFormat);
        DefaultFormatterFactory factory = new DefaultFormatterFactory(timeFormatter, timeFormatter, timeFormatter);
        return factory;
    }

    public static java.sql.Date utilDateToSqlDate(Date utilDate) {
        return new java.sql.Date(utilDate.getTime());
    }

    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        return weekday;
    }
}
