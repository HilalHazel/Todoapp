package com.hilal.todoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.hilal.todoapp.ui.alarm.AlarmActivity;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Context applicationContext = context.getApplicationContext();
        Long id = intent.getExtras().getLong("id");

        Intent alarmIntent = new Intent(applicationContext, AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("id", id);
        applicationContext.startActivity(alarmIntent);



    }
}
