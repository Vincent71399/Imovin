package sg.edu.nus.imovin2.Common;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.System.ImovinApplication;

public class CommonFunc {
    public static String Base64Encode(String inputString){
        return Base64.encodeToString(inputString.getBytes(), Base64.DEFAULT);
    }

    public static JSONObject ConvertObjectToJson(Object object){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return new JSONObject(mapper.writeValueAsString(object));
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String ConvertDuration2TimeFormat(int duration){
        int hour = duration / 3600;
        duration = duration % 60;
        int minute = duration / 60;
        duration = duration % 60;
        int second = duration;

        if(hour > 0){
            return addZero(hour) + ":" + addZero(minute) + ":" + addZero(second);
        }else{
            return addZero(minute) + ":" + addZero(second);
        }
    }

    public static String ConvertDateString2DisplayFormat(String dateString){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");

        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        String dateText = "";
        try {
            calendar.setTime(df.parse(dateString));
            int offset = calendar.getTimeZone().getRawOffset();
            calendar.add(Calendar.MILLISECOND, offset);

            long diffInMillies = now.getTimeInMillis() - calendar.getTimeInMillis();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(diffInDays>0)
                return String.format("%dd", diffInDays);
            else if(diffInHours>0)
                return String.format("%dh", diffInHours);
            else if(diffInMinutes>0)
                return String.format("%dm", diffInMinutes);
            else
                return "Just now";

        }catch (ParseException e){
            e.printStackTrace();
        }

        return dateText;
    }

    public static String GetCurrentMonthString(Calendar calendar){
        return convertInt2Month(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }

    public static String GetFullDateString(long millisecond){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millisecond);
        String dateFormat = date.get(Calendar.YEAR) + "-" + convertInt2Month(date.get(Calendar.MONTH)) + "-" + addZero(date.get(Calendar.DAY_OF_MONTH)) + " " +
                addZero(date.get(Calendar.HOUR_OF_DAY))+":"+addZero(date.get(Calendar.MINUTE))+":"+addZero(date.get(Calendar.SECOND));

        return dateFormat;
    }

    public static String GetFullDateStringRevert(Calendar date){
        String dateFormat = addZero(date.get(Calendar.DAY_OF_MONTH)) + "-" + convertInt2Month(date.get(Calendar.MONTH)) + "-" + date.get(Calendar.YEAR) + " " +
                addZero(date.get(Calendar.HOUR_OF_DAY))+":"+addZero(date.get(Calendar.MINUTE))+":"+addZero(date.get(Calendar.SECOND));

        return dateFormat;
    }

    public static String GetQueryDateStringRevert(Calendar date){
        String dateFormat = addZero(date.get(Calendar.DAY_OF_MONTH)) + "/" + addZero(date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.YEAR) + " " +
                addZero(date.get(Calendar.HOUR_OF_DAY))+":"+addZero(date.get(Calendar.MINUTE))+":"+addZero(date.get(Calendar.SECOND));

        return dateFormat;
    }

    public static String GetDisplayDate(Calendar date){
        String dateFormat = ordinal(date.get(Calendar.DAY_OF_MONTH)) + "-" + convertInt2Month(date.get(Calendar.MONTH)) + "-" + date.get(Calendar.YEAR);

        return dateFormat;
    }

    public static String GetDisplayDate(int year, int month, int day){
        String dateFormat = addZero(day) + "/" + addZero(month) + "/" + year + " 00:00:00";

        return dateFormat;
    }

    public static String GetDisplayMonth(Calendar date){
        String dateFormat = convertInt2Month(date.get(Calendar.MONTH)) + " " + date.get(Calendar.YEAR);
        return dateFormat;
    }

    public static String GetDisplayDateDetail(Calendar date){
        String dateFormat = ordinal(date.get(Calendar.DAY_OF_MONTH)) + " " + convertInt2Month(date.get(Calendar.MONTH)) + " " + date.get(Calendar.YEAR);
        return dateFormat;
    }

    public static String GetDisplayDateDetail(String dateString){
        return GetDisplayDateDetail(RevertFullDateStringRevert(dateString));
    }

    public static String GetTimeDetail(Calendar date){
        String timeFormat = convertInt2DAY_OF_WEEK(date.get(Calendar.DAY_OF_WEEK)) + " at " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
        return timeFormat;
    }

    public static String GetTimeDetail(String dateString){
        return GetTimeDetail(RevertFullDateStringRevert(dateString));
    }

    public static Calendar RevertFullDateStringRevert(String dateString){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");

        Calendar calendar  = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(dateString));
            return calendar;
        }catch (ParseException e){
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2){
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameDay(String cal1, String cal2){
        return isSameDay(RevertFullDateStringRevert(cal1), RevertFullDateStringRevert(cal2));
    }

    private static String convertInt2Month(Integer monthInt){
        String monthString = "";
        switch (monthInt){
            case 0:
                monthString = ImovinApplication.getInstance().getString(R.string.january);
                break;
            case 1:
                monthString = ImovinApplication.getInstance().getString(R.string.february);
                break;
            case 2:
                monthString = ImovinApplication.getInstance().getString(R.string.march);
                break;
            case 3:
                monthString = ImovinApplication.getInstance().getString(R.string.april);
                break;
            case 4:
                monthString = ImovinApplication.getInstance().getString(R.string.may);
                break;
            case 5:
                monthString = ImovinApplication.getInstance().getString(R.string.june);
                break;
            case 6:
                monthString = ImovinApplication.getInstance().getString(R.string.july);
                break;
            case 7:
                monthString = ImovinApplication.getInstance().getString(R.string.augest);
                break;
            case 8:
                monthString = ImovinApplication.getInstance().getString(R.string.september);
                break;
            case 9:
                monthString = ImovinApplication.getInstance().getString(R.string.october);
                break;
            case 10:
                monthString = ImovinApplication.getInstance().getString(R.string.november);
                break;
            case 11:
                monthString = ImovinApplication.getInstance().getString(R.string.december);
                break;
        }
        return monthString;
    }

    private static String convertInt2DAY_OF_WEEK(Integer day_of_week_int){
        String dayString = "";
        switch (day_of_week_int){
            case 1:
                dayString = ImovinApplication.getInstance().getString(R.string.sun);
                break;
            case 2:
                dayString = ImovinApplication.getInstance().getString(R.string.mon);
                break;
            case 3:
                dayString = ImovinApplication.getInstance().getString(R.string.tue);
                break;
            case 4:
                dayString = ImovinApplication.getInstance().getString(R.string.wed);
                break;
            case 5:
                dayString = ImovinApplication.getInstance().getString(R.string.thu);
                break;
            case 6:
                dayString = ImovinApplication.getInstance().getString(R.string.fri);
                break;
            case 7:
                dayString = ImovinApplication.getInstance().getString(R.string.sat);
                break;
        }
        return dayString;
    }


    private static String addZero(Integer value){
        if(value < 10){
            return "0" + value;
        }else{
            return String.valueOf(value);
        }
    }

    private static String addZeroThousand(Integer value){
        if(value < 10){
            return "00" + value;
        }else if(value < 100){
            return "0" + value;
        }else{
            return String.valueOf(value);
        }
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static int dayDiffBetweenCalendar(Calendar startDate, Calendar endDate) {
        startDate.set(Calendar.MILLISECOND, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.HOUR_OF_DAY, 0);

        endDate.set(Calendar.MILLISECOND, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR,0);
        endDate.set(Calendar.HOUR_OF_DAY, 0);

        long endDateTimeStamp = endDate.getTimeInMillis();
        long startDateTimeStamp = startDate.getTimeInMillis();

        Long dayDiffLong = (endDateTimeStamp - startDateTimeStamp) / (24 * 60 * 60 * 1000);

        return dayDiffLong.intValue();
    }

    public static boolean isSameMonth(Calendar targetCalendar, Calendar comparisonCalendar){
        return targetCalendar.get(Calendar.MONTH) == comparisonCalendar.get(Calendar.MONTH) && targetCalendar.get(Calendar.YEAR) == comparisonCalendar.get(Calendar.YEAR);
    }

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"signature.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static File loadImageFromStorage(String path)
    {
        File file=new File(path, "signature.png");
        return file;
    }

    public static String Integer2String(int value){
        if(value/1000 > 0) {
            return Integer2String(value / 1000) + "," + addZeroThousand(value % 1000);
        }
        return String.valueOf(value);
    }

    public static String RemoveVideoPrefix(String videoUrl){
        return videoUrl.replace("https://www.youtube.com/watch?v=", "");
    }
}
