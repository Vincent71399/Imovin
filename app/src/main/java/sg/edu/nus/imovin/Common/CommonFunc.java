package sg.edu.nus.imovin.Common;

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
import java.util.Date;
import java.util.concurrent.TimeUnit;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.ImovinApplication;

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
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date now = new Date();

        String result = "0d";

        try {
            Date date = simpleDateFormat.parse(dateString);
            long diffInMillies = now.getTime() - date.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(diffInDays>0)
                return String.format("%dd", diffInDays);
            else if(diffInHours>0)
                return String.format("%dh", diffInHours);
            else
                return String.format("%dm", diffInMinutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
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
        String dateFormat = addZero(day) + "/" + addZero(month + 1) + "/" + year + " 00:00:00";

        return dateFormat;
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

    private static String addZero(Integer value){
        if(value < 10){
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

    
}
