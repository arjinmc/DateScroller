# DateScroller
A android custom view for date pick which can be scrolled with horizonal direction.
The start date is today,the last date is unlimited.

This view is one of my pass work,I rewrite it and change a little bit.Now it looks like this.

![image](https://github.com/arjinmc/DateScroller/blob/master/image/sample.gif)

## How to use
in xml
``` java

    <com.arjinmc.libdatescroller.DateScroller
        android:id="@+id/dateScroller"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>


```
in java
``` java

    dateScroller = (DateScroller) findViewById(R.id.dateScroller);
    dateScroller.setOnItemClickListener(
        new DateScroller.OnItemClickListener() {
        @Override
        public void onItemClick(DateScrollerData dateData) {
            Log.e("date selected",dateData.getYear()+"/"
                +dateData.getMonth()+"/"+dateData.getDayOfMonth());
        }
    });

    DateScrollerData today = dateScroller.getCurrentDate();
    Log.e("today is",today.getYear()+"/"
        +today.getMonth()+"/"+today.getDayOfMonth());


```