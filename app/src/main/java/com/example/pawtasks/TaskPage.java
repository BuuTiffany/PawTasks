package com.example.pawtasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskPage extends Fragment {

    private RecyclerView tasksView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    public TaskPage() {
        // Required empty public constructor
    }

    public static class Task {
        private String title;
        private long timeRemaining;
        public CountDownTimer timer;

        public Task(String title) {
            this.title = title;
            this.timeRemaining = timeUntilEndOfDay();
            this.timer = new CountDownTimer(this.timeRemaining, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeRemaining = millisUntilFinished;
                    // Update UI or do something else if necessary
                }

                @Override
                public void onFinish() {
                    // Handle timer finish if necessary
                }
            }.start();
        }

        public String getTitle() {
            return title;
        }
        public long getTimeRemaining() {
            return timeRemaining;
        }

        public void setTimeRemaining(long timeRemaining) {
            this.timeRemaining = timeRemaining;
        }

        private long timeUntilEndOfDay() {
            Calendar current = Calendar.getInstance();
            Calendar endOfDay = (Calendar) current.clone();
            endOfDay.set(Calendar.HOUR_OF_DAY, 23);
            endOfDay.set(Calendar.MINUTE, 59);
            endOfDay.set(Calendar.SECOND, 59);
            endOfDay.set(Calendar.MILLISECOND, 999);
            return endOfDay.getTimeInMillis() - current.getTimeInMillis();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_page, container, false);
        tasksView = view.findViewById(R.id.tasksView_recyclerView);
        taskList = new ArrayList<>();
        TextView pointsTextView = view.findViewById(R.id.pointsTextView);

        taskAdapter = new TaskAdapter(taskList, pointsTextView);
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

        ImageView calendarButton = view.findViewById(R.id.menu_icon);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize the DatePickerDialog and show it
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            // You can handle the selected date here.
                            // For instance, you can set it to a TextView or use it elsewhere.
                            String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            // yourTextView.setText(selectedDate);
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });

        return view;
    }
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;
        private TextView pointsTextView;
        int pointsValue = 0; // variable to keep track of points

        public TaskAdapter(List<Task> tasks,TextView pointsTextView) {
            this.tasks = tasks;
            this.pointsTextView = pointsTextView;
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            Button deleteButton;
            CheckBox checkBox;
            View itemView;
            TextView timerText;

            public TaskViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                title = itemView.findViewById(R.id.taskTitle_text);
                deleteButton = itemView.findViewById(R.id.deleteTaskButton);
                checkBox = itemView.findViewById(R.id.checkBox);
                timerText = itemView.findViewById(R.id.timer_text);
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
                    int currentPosition = holder.getBindingAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View view = inflater.inflate(R.layout.custom_alert, null);
                        builder.setView(view);

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Delete button
                                tasks.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                notifyItemRangeChanged(currentPosition, tasks.size());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog, do nothing
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        positiveButton.setTextColor(getResources().getColor(R.color.custom_red));
                        negativeButton.setTextColor(getResources().getColor(R.color.custom_green));
                    }
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                int checkboxGreen = getResources().getColor(R.color.CheckboxGreen, null);
                int unFinishedTask = getResources().getColor(R.color.UnfinishedTask);
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    LinearLayout innerLayout = holder.itemView.findViewById(R.id.innerLayout);
                    if (isChecked) {
                        // Change background color when checked
                        innerLayout.setBackgroundColor(checkboxGreen);
                        buttonView.setClickable(false);
                        pointsValue++;
                        pointsTextView.setText("Points: " + pointsValue);
                        if (tasks.get(position).timer != null) {
                            tasks.get(position).timer.cancel();
                        }
                    } else {
                        // Reset background color when unchecked
                        innerLayout.setBackgroundColor(unFinishedTask);
                    }
                }
            });
            long timeRemaining = tasks.get(position).getTimeRemaining();
            holder.timerText.setText(formatTime(timeRemaining));
        }

        private String formatTime(long millis) {
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}