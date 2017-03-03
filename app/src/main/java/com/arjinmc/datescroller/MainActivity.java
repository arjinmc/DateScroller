package com.arjinmc.datescroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arjinmc.libdatescroller.DateScroller;
import com.arjinmc.libdatescroller.DateScrollerData;

public class MainActivity extends AppCompatActivity {

    private DateScroller dateScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateScroller = (DateScroller) findViewById(R.id.dateScroller);
        dateScroller.setOnItemClickListener(new DateScroller.OnItemClickListener() {
            @Override
            public void onItemClick(DateScrollerData dateData) {
                Log.e("date selected",dateData.getYear()+"/"+dateData.getMonth()+"/"+dateData.getDayOfMonth());
            }
        });

        DateScrollerData today = dateScroller.getCurrentDate();
        Log.e("today is",today.getYear()+"/"+today.getMonth()+"/"+today.getDayOfMonth());
    }
}
