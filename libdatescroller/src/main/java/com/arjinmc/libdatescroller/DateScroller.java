package com.arjinmc.libdatescroller;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Eminem Lu on 24/2/17.
 * Email arjinmc@hotmail.com
 */

public class DateScroller extends LinearLayout {


    private Calendar calendar = Calendar.getInstance();

    private TextView tvTodayDayOfWeek;
    private LinearLayout contentLinearLayout;
    private DateScrollerView dateScollerView;

    private OnItemClickListener onItemClickListener;
    private DateScrollerData todayDate;
    private DateScrollerData currentDate;

    public DateScroller(Context context) {
        super(context);
        init(context);
    }

    public DateScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context){


        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.color.datescroller_check_border);

        //the content layout
        contentLinearLayout = new LinearLayout(getContext());
        LayoutParams contentParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = DateScrollerUitls.dp2px(getContext(),8);
        contentParams.topMargin = margin;
        contentParams.leftMargin = margin;
        contentParams.rightMargin = margin;
        contentParams.bottomMargin = margin;
        contentLinearLayout.setLayoutParams(contentParams);
        contentLinearLayout.setPadding(0,0,DateScrollerUitls.dp2px(getContext(),8),0);
        contentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLinearLayout.setBackgroundResource(R.drawable.shape_datescroller_bg);

        //today layout
        final LinearLayout todayLinearLayout = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.item_datescroller,null);
        LayoutParams todayParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        todayLinearLayout.setLayoutParams(todayParams);
        todayLinearLayout.setGravity(Gravity.CENTER);
        tvTodayDayOfWeek = (TextView) todayLinearLayout.findViewById(R.id.item_datescroller_dayOfWeek);
        tvTodayDayOfWeek.setText(getContext().getString(R.string.datescroller_today));
        tvTodayDayOfWeek.setPadding(DateScrollerUitls.dp2px(getContext(),8),0,0,0);


        //init today dayta
        todayDate = DateScrollerUitls.addDate(getContext(),calendar,0);
        currentDate = todayDate;
        todayLinearLayout.setBackgroundResource(R.drawable.shape_datescroller_check_today_bg);

        todayLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dateScollerView.setSelected(-1);
                todayLinearLayout.setBackgroundResource(R.drawable.shape_datescroller_check_today_bg);
                currentDate = todayDate;
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(currentDate);
            }
        });

        //diver
        View vDiver = new View(getContext());
        vDiver.setLayoutParams(new LayoutParams(DateScrollerUitls.dp2px(getContext(),1),LayoutParams.MATCH_PARENT));
        vDiver.setBackgroundColor(Color.BLACK);


        contentLinearLayout.addView(todayLinearLayout);
        contentLinearLayout.addView(vDiver);

        dateScollerView = new DateScrollerView(getContext(),calendar);
        dateScollerView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        dateScollerView.setOnItemCheckListener(new DateScrollerView.OnItemCheckListener() {
            @Override
            public void onItemSelected(DateScrollerData datedata) {
                currentDate = datedata;
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(currentDate);
                todayLinearLayout.setBackgroundResource(0);
            }
        });
        contentLinearLayout.addView(dateScollerView);

        addView(contentLinearLayout);


    }

    public DateScrollerData getCurrentDate(){
        return currentDate;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(DateScrollerData dateData);
    }


}
