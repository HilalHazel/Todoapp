package com.hilal.todoapp.ui.alarm;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.hilal.todoapp.R;
import com.hilal.todoapp.db.Todo;
import com.hilal.todoapp.db.TodoAppDbHelper;


public class AlarmActivity  extends Activity {
    private boolean isSnoozeClicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_main);

        Long id = getIntent().getExtras().getLong("id");

        Context context = getApplicationContext();
        TodoAppDbHelper dbHelper = new TodoAppDbHelper(context);
        Todo todo = dbHelper.select(id);

        TextView textView = findViewById(R.id.alarm_description);
        textView.setText(todo.getDesc());
        dbHelper.setPassed(id);

    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (alarmUri == null) {
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);

                int i = 0;
                while(i < 5) {
                    if (!ringtone.isPlaying()) {
                        ringtone.play();
                        i++;
                    }
                    if (isSnoozeClicked) {
                        ringtone.stop();
                    }
                }
                finish();
            }
        }).start();


    }

    public void snoozeClicked(View view) {
        if (!isSnoozeClicked) {
            isSnoozeClicked = true;
        }
    }
}
