package com.example.pawtasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;

public class TaskPage extends Fragment {

    private RecyclerView tasksView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    public TaskPage() {
        // Required empty public constructor
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

        addTaskButton.setOnClickListener(v -> {
            String newTaskTitle = taskInputEditText.getText().toString();
            if (!newTaskTitle.isEmpty()) {
                Amplify.Auth.getCurrentUser(
                        currentUser -> {
                            if (currentUser != null) {
                                String userId = currentUser.getUserId();
                                Task newTask = Task.builder()
                                        .userId(userId)
                                        .title(newTaskTitle)
                                        .checked(false)
                                        .finishByTime("someTime")  // Set this based on your requirements
                                        .date("someDate")          // Set this based on your requirements
                                        .build();

                                GraphQLRequest<Task> request = ModelMutation.create(newTask);

                                Amplify.API.mutate(
                                        request,
                                        response -> {
                                            Log.i("MyAmplifyApp", "Task created with id: " + response.getData().getId());
                                            if (isAdded()) { // Check if the fragment is currently added to its activity
                                                getActivity().runOnUiThread(() -> {
                                                    taskList.add(newTask);
                                                    taskAdapter.notifyDataSetChanged();
                                                    taskInputEditText.setText("");
                                                });
                                            }
                                        },
                                        error -> Log.e("MyAmplifyApp", "Save failed", error)
                                );
                            } else {
                                Log.e("MyAmplifyApp", "User not signed in");
                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Get current user failed", error)
                );
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
        ImageView signOutIcon = view.findViewById(R.id.signout_icon);
        signOutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amplify.Auth.signOut(
                        // Success callback
                        result -> {
                            Log.i("AuthQuickstart", "Signed out successfully: " + result.toString());
                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                );
            }
        });

        queryTasks();
        return view;
    }
    private void queryTasks() {
        Amplify.Auth.getCurrentUser(
                currentUser -> {
                    if (currentUser != null) {
                        String userId = currentUser.getUserId();
                        Amplify.API.query(
                                ModelQuery.list(Task.class, Task.USER_ID.eq(userId)),
                                response -> {
                                    if (response.getData() != null) {
                                        if (isAdded()) {  // Check if the fragment is currently added to its activity
                                            getActivity().runOnUiThread(() -> {
                                                taskList.clear();
                                                for (Task task : response.getData()) {
                                                    taskList.add(task);
                                                }
                                                taskAdapter.notifyDataSetChanged();
                                            });
                                        } else {
                                            Log.e("MyAmplifyApp", "Fragment is not attached to an Activity. Skipping UI update.");
                                        }
                                    }
                                    if (response.getErrors() != null && !response.getErrors().isEmpty()) {
                                        Log.e("MyAmplifyApp", "Error fetching tasks: " + response.getErrors());
                                    }
                                },
                                error -> Log.e("MyAmplifyApp", "Query failed", error)
                        );
                    } else {
                        Log.e("MyAmplifyApp", "Current user is null");
                    }
                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
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

            public TaskViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                title = itemView.findViewById(R.id.taskTitle_text);
                deleteButton = itemView.findViewById(R.id.deleteTaskButton);
                checkBox = itemView.findViewById(R.id.checkBox);
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
                        builder.setTitle("Delete Task");  // Set the dialog title
                        builder.setMessage("Are you sure you want to delete this task?");  // Set the dialog message

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Perform the delete operation with Amplify
                                Task taskToDelete = tasks.get(currentPosition);
                                Amplify.API.mutate(ModelMutation.delete(taskToDelete),
                                        response -> {
                                            Log.i("MyAmplifyApp", "Delete succeeded: " + taskToDelete);

                                            // Update UI on the main thread
                                            getActivity().runOnUiThread(() -> {
                                                tasks.remove(currentPosition);
                                                notifyItemRemoved(currentPosition);
                                            });
                                        },
                                        error -> {
                                            Log.e("MyAmplifyApp", "Delete failed", error);
                                        }
                                );
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog, just dismiss it
                                dialog.dismiss();
                            }
                        });

                        builder.show();  // Show the dialog
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
                    } else {
                        // Reset background color when unchecked
                        innerLayout.setBackgroundColor(unFinishedTask);
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