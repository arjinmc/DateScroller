package com.arjinmc.libdatescroller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Eminem Lo on 24/2/17.
 * Email arjinmc@hotmail.com
 */

public class DateScrollerView extends ViewGroup {

    private final int NEW_COUNT = 60;
    private int offfseForAddDays = 300;

    private int parentHeight;
    private int parentWidth;
    private int itemWidth;
    private int toastPosition;
    private boolean isAddingDays;
    private int lastToastMonth = 0;
    private Calendar calendar;
    private int currentYear = 0;
    private String[] monthArray;
    private List<DateScrollerData> dateData;
    private int selectedPosition = -1;

    private PopUpView popUpView;

    private OnItemCheckListener onItemClechListener;

    private OverScroller scroller;
    private boolean isScrolling;
    private int touchSlop;
    private float downX;
    private float moveX;
    private int scrollDirection = -1;

    float leftBorder = 0;

    public DateScrollerView(Context context, Calendar calendar) {
        super(context);
        this.calendar = calendar;
        init();
    }

    public DateScrollerView(Context context) {
        super(context);
        init();
    }

    public DateScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateScrollerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childSize = getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = getChildAt(i);
            child.measure(0, 0);
            if (parentHeight == 0) {
                parentHeight = child.getMeasuredHeight();
                itemWidth = child.getMeasuredWidth() + 20;//add offset
            }
        }
        setMeasuredDimension(widthMeasureSpec, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (parentWidth == 0) {
            parentWidth = r;
            offfseForAddDays = parentWidth / 2;
            toastPosition = parentWidth / itemWidth - 1;
        }

        layoutChild();

    }

    private void layoutChild() {


        final int childSize = getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = getChildAt(i);

            child.layout(itemWidth * i, 0, itemWidth * (i + 1), parentHeight);

        }
    }

    private void init() {

        scroller = new OverScroller(getContext());
        //the touch distance for distinguish touch event between click and scroll
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        popUpView = new PopUpView();

        dateData = new ArrayList<>();
        currentYear = calendar.get(Calendar.YEAR);
        addMoreDays();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:

                moveX = event.getX();
                //if is not scrolling
                if (Math.abs(downX - moveX) >= touchSlop) {
                    isScrolling = true;
                    int alterMove = (int) (downX - moveX);

                    if (getScrollX() + alterMove <= leftBorder) {
                        scrollTo((int) leftBorder, 0);
                    } else {
                        scrollTo(getScrollX() + alterMove, 0);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isScrolling == true) {
                    scroller.startScroll(getScrollX(), 0, getScrollX() + (int) (moveX - downX), 0, 2000);
                    invalidate();
                }

                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {

            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();

        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);

        //toast the year and month
        int childPostion = getScrollX() / itemWidth + toastPosition;
        if (childPostion >= getChildCount()) {
            childPostion = getChildCount() - 1;
        } else if (childPostion < 0) {
            childPostion = 0;
        }
        if (dateData.get(childPostion).getDayOfMonth() <= 3 || dateData.get(childPostion).getDayOfMonth() >= 28) {
            int currentMonth = dateData.get(childPostion).getMonth();
            if (lastToastMonth != currentMonth)
                toastNextMonth(dateData.get(childPostion));
            lastToastMonth = currentMonth;
        }

        //add more days
        if (getChildAt(getChildCount() - 20).getLeft() <= getScrollX() && !isAddingDays) {
            isAddingDays = true;
            addMoreDays();
        }

        isScrolling = false;

    }

    //generate more days
    private void addMoreDays() {
        int currentDataSize = dateData.size();
        for (int i = 0; i < NEW_COUNT; i++) {
            DateScrollerData data = DateScrollerUitls.addDate(getContext(), calendar, 1);
            dateData.add(data);
            LinearLayout child = (LinearLayout) LayoutInflater
                    .from(getContext()).inflate(R.layout.item_datescroller, null);
            ((TextView) child.findViewById(R.id.item_datescroller_dayOfWeek)).setText(data.getDay());
            ((TextView) child.findViewById(R.id.item_datescroller_day))
                    .setText(data.getDayOfMonth() + "");
            final int postion = currentDataSize + i;

            child.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (!isScrolling)
                        setSelected(postion);
                    return false;
                }


            });

            addView(child);
            isAddingDays = false;
        }
    }

    /**
     * popup to show the month and year
     *
     * @param dateData
     */
    private void toastNextMonth(DateScrollerData dateData) {

        monthArray = getContext().getResources().getStringArray(R.array.datescroller_month_array);
        String toastMsg = "";
        //if it's not this year
        if (dateData.getYear() != currentYear) {
            toastMsg = String.format(getResources().getString(R.string.datescroller_year), dateData.getYear());
        }
        toastMsg += monthArray[dateData.getMonth() - 1];
        popUpView.show(toastMsg);
    }


    /**
     * popupwindow for show the month and year
     */
    public class PopUpView {

        private PopupWindow popupWindow;
        private TextView tvMsg;
        private CountDownTimer countDownTimer = new CountDownTimer(1000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisUntilFinished -= 1000;
            }

            @Override
            public void onFinish() {
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        };

        public PopUpView() {

            tvMsg = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvMsg.setLayoutParams(params);
            tvMsg.setTextColor(Color.WHITE);
            tvMsg.setBackgroundResource(R.drawable.shape_datescroller_toast);
            tvMsg.setGravity(Gravity.CENTER);
            int padding = DateScrollerUitls.dp2px(getContext(), 10);
            tvMsg.setPadding(padding * 2, padding, padding * 2, padding);
            popupWindow = new PopupWindow(tvMsg, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(true);
        }

        public void show(String msg) {
            tvMsg.setText(msg);
            popupWindow.showAtLocation((View) DateScrollerView.this
                    , Gravity.BOTTOM, 0, DateScrollerUitls.dp2px(getContext(), parentHeight));
            countDownTimer.start();

        }
    }


    //set the item selected
    public void setSelected(int postion) {

        if (selectedPosition != postion) {

            if (selectedPosition != -1)
                getChildAt(selectedPosition).setBackgroundResource(0);

            selectedPosition = postion;
            if (selectedPosition != -1) {
                View child = getChildAt(postion);
                child.setBackgroundResource(R.drawable.shape_datescroller_check_bg);

                if (onItemClechListener != null)
                    onItemClechListener.onItemSelected(dateData.get(postion));
            }

        }

    }

    public int getSelectedPostion() {
        return selectedPosition;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemClechListener = onItemCheckListener;
    }


    public interface OnItemCheckListener {

        public void onItemSelected(DateScrollerData datedata);
    }

}
