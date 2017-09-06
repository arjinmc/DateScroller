package com.arjinmc.libdatescroller;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by Eminem Lo on 24/2/17.
 * Email arjinmc@hotmail.com
 */

public final class DateScrollerUitls {

    public static String formateDay(Context context,int dayOfWeak){
        switch (dayOfWeak){
            case Calendar.SUNDAY:
            default:
                return  context.getString(R.string.datescroller_sunday);
            case Calendar.MONDAY:
                return context.getString(R.string.datescroller_monday);
            case Calendar.TUESDAY:
                return context.getString(R.string.datescroller_tuseday);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.datescroller_wednesday);
            case Calendar.THURSDAY:
                return context.getString(R.string.datescroller_thursday);
            case Calendar.FRIDAY:
                return context.getString(R.string.datescroller_friday);
            case Calendar.SATURDAY:
                return context.getString(R.string.datescroller_saturday);
        }
    }

    //create DateScrollerData
    public static DateScrollerData addDate(Context context,Calendar calendar,int offset){

        DateScrollerData data = new DateScrollerData();
        calendar.add(Calendar.DATE, offset);
        data.setTime(calendar.getTimeInMillis());
        data.setDay(DateScrollerUitls.formateDay(context,calendar.get(Calendar.DAY_OF_WEEK)));
        data.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        data.setMonth(calendar.get(Calendar.MONTH)+1);
        data.setYear(calendar.get(Calendar.YEAR));
        return data;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
