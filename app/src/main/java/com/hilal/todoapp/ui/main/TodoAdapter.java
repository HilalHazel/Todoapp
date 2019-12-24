package com.hilal.todoapp.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hilal.todoapp.AlarmReceiver;
import com.hilal.todoapp.R;
import com.hilal.todoapp.db.Todo;
import com.hilal.todoapp.db.TodoAppDbHelper;

import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private LayoutInflater inflater;
    private List<Todo>     todos;

    public TodoAdapter(Context context, List<Todo> todos) {
        this.inflater = LayoutInflater.from(context);
        this.todos = todos;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.todo_card, parent, false);
        return new TodoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.setData(todo);

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }


    class TodoViewHolder extends RecyclerView.ViewHolder {

        private final TextView descView;
        private final TextView dateView;
        private final CheckBox checkBox;
        private  Todo     todo;

        public TodoViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.descView = itemView.findViewById(R.id.todoDescView);
            this.dateView = itemView.findViewById(R.id.todoDateView);
            this.checkBox = itemView.findViewById(R.id.checkBox);


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Context context = itemView.getContext();
                    TodoAppDbHelper dbHelper = new TodoAppDbHelper(context);
                    dbHelper.setCheckStatus(todo.getId(), isChecked);

                    if (!isOld(todo.getDueDate())) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(context, AlarmReceiver.class);
                        if (isChecked) {
                            PendingIntent pendingInteng = PendingIntent.getBroadcast(context, todo.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingInteng);
                        } else {
                            intent.putExtra("id", todo.getId());
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, todo.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, todo.getDueDate().getTime(), broadcast);
                        }
                    }
                }

                private boolean isOld(Date dueDate) {
                    Date now = new Date();
                    return now.compareTo(dueDate) > 0;
                }
            });
        }


        public void setData(Todo todo) {
            this.todo = todo;
            this.descView.setText(todo.getDesc());
            this.dateView.setText(todo.getDueDateStr());
            this.checkBox.setChecked(todo.isDone());
        }
    }
}
