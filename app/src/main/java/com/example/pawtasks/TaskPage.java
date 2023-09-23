package com.example.pawtasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class TaskPage extends Fragment {

    private RecyclerView tasksView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    public TaskPage() {
        // Required empty public constructor
    }

    public static class Task {
        private String title;

        public Task(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_page, container, false);
        tasksView = view.findViewById(R.id.tasksView_recyclerView);
        taskList = new ArrayList<>();

        taskAdapter = new TaskAdapter(taskList);
        tasksView.setAdapter(taskAdapter);


        final EditText taskInputEditText = view.findViewById(R.id.taskInputEditText);
        Button addTaskButton = view.findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTaskTitle = taskInputEditText.getText().toString();
                if (!newTaskTitle.isEmpty()) {
                    taskList.add(new Task(newTaskTitle));
                    taskAdapter.notifyDataSetChanged();
                    taskInputEditText.setText("");
                }
            }
        });

        return view;
    }
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            Button deleteButton;

            public TaskViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.taskTitle_text);
                deleteButton = itemView.findViewById(R.id.deleteTaskButton);
            }
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.title.setText(tasks.get(position).getTitle());

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the item from the list
                    int currentPosition = holder.getBindingAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        // Remove the item from the list
                        tasks.remove(currentPosition);

                        // Notify the adapter that an item is removed
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, tasks.size());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}