package com.hilal.todoapp.ui.alarm;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.hilal.todoapp.AlarmReceiver;
import com.hilal.todoapp.R;
import com.hilal.todoapp.db.TodoAppDbHelper;
import com.hilal.todoapp.ui.main.PlaceholderFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewAlarmActivity extends Activity {

    private Calendar         date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm_main);

        date = Calendar.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        View root = findViewById(R.id.date_text).getRootView();
        final TextView dateText = root.findViewById(R.id.date);

        dateText.setText(dateFormat.format(date.getTime()));
    }

    private void datePicker(final View view) {
        int mYear = date.get(Calendar.YEAR);
        int mMonth = date.get(Calendar.MONTH);
        int mDay = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TextView dateText = view.findViewById(R.id.date);
                        dateText.setText(dateFormat.format(date.getTime()));

                        timePicker(view);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(final View view) {
        int mHour = date.get(Calendar.HOUR_OF_DAY);
        int mMinute = date.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date.set(Calendar.SECOND, 0);
                        date.set(Calendar.MILLISECOND, 0);

                        TextView dateText = view.findViewById(R.id.date);
                        dateText.setText(dateFormat.format(date.getTime()));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void addClicked(View view) {
        TodoAppDbHelper dbHelper = new TodoAppDbHelper(getApplicationContext());

        final EditText editText = findViewById(R.id.new_alarm_description);
        Long id = dbHelper.addTodo(editText.getText().toString(), date.getTime());

        AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("id", id);
        PendingIntent broadcast = PendingIntent.getBroadcast(getBaseContext(), id.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), broadcast);

        Toast.makeText(getApplicationContext(), "Todo Created", Toast.LENGTH_LONG).show();

        finish();
    }

    public void setDate(View view) {
        datePicker(view);
    }
}
