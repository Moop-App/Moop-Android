package soup.movie.util;

import java.util.Calendar;

import androidx.annotation.NonNull;

public class TimeUtil {

    private TimeUtil() {
    }

    public static String today() {
        return TimeUtil.YYYYHHMM(Calendar.getInstance());
    }

    public static String yesterday() {
        return TimeUtil.YYYYHHMM(beforeDay(Calendar.getInstance()));
    }

    private static Calendar beforeDay(Calendar calendar) {
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    public static String YYYYHHMM(Calendar calendar) {
        return String.format("%04d%02d%02d", year(calendar), month(calendar), day(calendar));
    }

    public static int year(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int month(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int day(@NonNull Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int hour(@NonNull Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int minute(@NonNull Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    private static String str(int integer) {
        return Integer.toString(integer);
    }
}
