package com.hilal.todoapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hilal.todoapp.R;
import com.hilal.todoapp.db.Todo;
import com.hilal.todoapp.db.TodoAppDbHelper;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private int index;

    public PlaceholderFragment(int index) {
        this.index = index;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            if (getFragmentManager() != null) {

                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTodos();
    }

    public void updateTodos() {
        if (getView() == null) {
            return;
        }

        final RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        TodoAppDbHelper dbHelper = new TodoAppDbHelper(getContext());

        List<Todo> todos;
        if (index == 0) {
            todos = dbHelper.selectAllNotDone();
        } else {
            todos = dbHelper.selectAllDone();
        }


        TodoAdapter todoAdapter = new TodoAdapter(getContext(), todos);
        recyclerView.setAdapter(todoAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}