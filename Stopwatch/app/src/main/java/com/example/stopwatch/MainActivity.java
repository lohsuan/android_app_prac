package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView timer;
    Button reset_btn, start_btn;
    String time_str;
    long milliSecondTime, startTime, timeBuff=0L, updateTime = 0L;
    private Handler mHandler;
    int seconds, minutes, milliSeconds;
    ListView listView;
    ArrayList<String> myList = new ArrayList<String>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        timer = (TextView) findViewById(R.id.timer);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        start_btn = (Button) findViewById(R.id.start_btn);

        mHandler = new Handler();

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            milliSecondTime = SystemClock.uptimeMillis() -startTime;
            updateTime = timeBuff + milliSecondTime;
            seconds = (int)(updateTime / 1000); // long type transform into int
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int)(updateTime % 1000);
            milliSeconds = (int)(milliSeconds * 60 / 1000);

            time_str = String.format("%d:%02d:%02d", minutes, seconds, milliSeconds);
//            time_str = String.format("%d:%02d:%03d", minutes, seconds, milliSeconds);
            timer.setText(time_str);
            mHandler.post(this);
        }
    };

    public void startEvent(View view) {
        if(start_btn.getText().equals("start")){
            reset_btn.setEnabled(false);
            start_btn.setText("record");
            startTime = SystemClock.uptimeMillis();
            mHandler.post(runnable);
        }
        else{
            startTime = SystemClock.uptimeMillis();
            mHandler.post(runnable);
            myList.add(timer.getText().toString());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, myList);
            listView.setAdapter(adapter);
            timeBuff += milliSecondTime; // save paused time to timeBuff
        }
    }

    public void pauseEvent(View view) {
        start_btn.setText("start");
        timeBuff += milliSecondTime; // save paused time to timeBuff
        mHandler.removeCallbacks(runnable); // stop runnable
        reset_btn.setEnabled(true);

    }

    public void resetEvent(View view) {
        start_btn.setText("start");
//        myList.clear();
        adapter.clear();
        milliSecondTime = 0L;
        startTime = 0L;
        timeBuff = 0L;
        updateTime = 0L;
        seconds = 0;
        minutes = 0;
        milliSeconds = 0;
        timer.setText("0:00:00");
    }

}