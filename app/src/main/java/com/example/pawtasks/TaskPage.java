package com.example.pawtasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;
import com.amplifyframework.datastore.generated.model.User;

public class TaskPage extends Fragment {

    private RecyclerView tasksView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private int pointsValue = 0;
    private TextView pointsTextView;
    private TextView usernameTextView;
    private int userTokens = 0;
    private ImageView avatar;
    public TaskPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_page, container, false);
        pointsTextView = view.findViewById(R.id.pointsTextView);
        tasksView = view.findViewById(R.id.tasksView_recyclerView);
        taskList = new ArrayList<>();
        usernameTextView = view.findViewById(R.id.usernameTextView);
        avatar = view.findViewById(R.id.userImage);

        taskAdapter = new TaskAdapter(taskList, pointsTextView, this);
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

        ImageView refreshIcon = view.findViewById(R.id.refresh_icon);
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method that requeries tasks and points
                queryCurrentUser();
                queryTasks();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.inAppStatusBar));
        }

        int randomNumber = new Random().nextInt(30) + 1;
        String avatarResourceName = "avatar_" + randomNumber;
        int resourceId = getResources().getIdentifier(avatarResourceName, "drawable", getActivity().getPackageName());
        avatar.setImageResource(resourceId);

        queryCurrentUser();
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
                                taskResponse -> {
                                    if (taskResponse.getData() != null) {
                                        List<Task> newTasks = new ArrayList<>();
                                        if (taskResponse.getData() != null) {
                                            for (Task task : taskResponse.getData()) {
                                                newTasks.add(task);
                                            }
                                        }
                                        TaskDiffCallback diffCallback = new TaskDiffCallback(taskList, newTasks);
                                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

                                        // Update the task list and notify the adapter on the main thread
                                        if (isAdded()) {
                                            getActivity().runOnUiThread(() -> {
                                                taskList.clear();
                                                taskList.addAll(newTasks);
                                                diffResult.dispatchUpdatesTo(taskAdapter);
                                            });
                                        }
                                    }
                                    if (taskResponse.getErrors() != null && !taskResponse.getErrors().isEmpty()) {
                                        Log.e("MyAmplifyApp", "Error fetching tasks: " + taskResponse.getErrors());
                                    }
                                },
                                taskError -> Log.e("MyAmplifyApp", "Query for tasks failed", taskError)
                        );
                    } else {
                        Log.e("MyAmplifyApp", "Current user is null");
                    }
                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
    }

    private void queryCurrentUser() {
        Amplify.Auth.getCurrentUser(
                currentUser -> {
                    if (currentUser != null) {
                        String userId = currentUser.getUserId();
                        Amplify.API.query(
                                ModelQuery.get(User.class, userId),
                                response -> {
                                    if (response.getData() != null) {
                                        User user = response.getData();
                                        userTokens = user.getTokens() != null ? user.getTokens() : 0;
                                        if (isAdded()) { // Check if the fragment is currently added to its activity
                                            getActivity().runOnUiThread(() -> {
                                                pointsTextView.setText("Tokens: " + userTokens);
                                                String greeting = "Hello, " + user.getUsername() + "!";
                                                usernameTextView.setText(greeting);
                                            });
                                        }
                                    }
                                },
                                error -> Log.e("MyAmplifyApp", "Query failed", error)
                        );
                    }
                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
    }
    public class TaskDiffCallback extends DiffUtil.Callback {

        private final List<Task> oldTasks;
        private final List<Task> newTasks;

        public TaskDiffCallback(List<Task> oldTasks, List<Task> newTasks) {
            this.oldTasks = oldTasks;
            this.newTasks = newTasks;
        }

        @Override
        public int getOldListSize() {
            return oldTasks.size();
        }

        @Override
        public int getNewListSize() {
            return newTasks.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldTasks.get(oldItemPosition).getId().equals(newTasks.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldTasks.get(oldItemPosition).equals(newTasks.get(newItemPosition));
        }
    }
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;
        private TextView pointsTextView;
        private TaskPage taskPage;
        int pointsValue = 0; // variable to keep track of points

        public TaskAdapter(List<Task> tasks,TextView pointsTextView, TaskPage taskPage) {
            this.tasks = tasks;
            this.pointsTextView = pointsTextView;
            this.taskPage = taskPage;
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            Button deleteButton;
            CheckBox checkBox;
            View itemView;
            LinearLayout innerLayout;
            int checkboxGreen;
            int unFinishedTask;

            public TaskViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                title = itemView.findViewById(R.id.taskTitle_text);
                deleteButton = itemView.findViewById(R.id.deleteTaskButton);
                checkBox = itemView.findViewById(R.id.checkBox);
                innerLayout = itemView.findViewById(R.id.innerLayout);
                checkboxGreen = itemView.getContext().getResources().getColor(R.color.CheckboxGreen, null);
                unFinishedTask = itemView.getContext().getResources().getColor(R.color.UnfinishedTask);
            }
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task currentTask = tasks.get(position);
            holder.title.setText(tasks.get(position).getTitle());
            holder.checkBox.setChecked(currentTask.getChecked());
            if (currentTask.getChecked()) {
                holder.innerLayout.setBackgroundColor(holder.checkboxGreen);
                holder.checkBox.setClickable(false); // If task is already checked, make checkbox not clickable
                taskPage.pointsValue++;
                pointsTextView.setText("Tokens: " + pointsValue);// Assuming you want to increase points for already checked tasks when first loading
            } else {
                holder.innerLayout.setBackgroundColor(holder.unFinishedTask);
            }

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


            Task task = tasks.get(position);

            // Set the initial checked state of the checkbox
            holder.checkBox.setChecked(task.getChecked());

            // Set up the onCheckedChangeListener for the checkbox
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    if (currentTask != null) {
                        // Update the task's completed state in the backend
                        Task updatedTask = currentTask.copyOfBuilder()
                                .checked(isChecked)
                                .build();

                        Amplify.API.mutate(
                                ModelMutation.update(updatedTask),
                                response -> {
                                    Log.i("MyAmplifyApp", "Task successfully updated");
                                    // Here you could notify your adapter if necessary
                                },
                                error -> Log.e("MyAmplifyApp", "Failed to update task", error)
                        );

                        // Update User's tokens in backend
                        Amplify.Auth.getCurrentUser(
                                currentUser -> {
                                    if (currentUser != null) {
                                        String userId = currentUser.getUserId();
                                        Amplify.API.query(
                                                ModelQuery.get(User.class, userId),
                                                response -> {
                                                    if (response.getData() != null) {
                                                        User user = response.getData();
                                                        int newTokens = user.getTokens() != null ? user.getTokens() : 0;
                                                        if (isChecked) {
                                                            newTokens++;
                                                        } else if (newTokens > 0) {
                                                            newTokens--;
                                                        }
                                                        User updatedUser = user.copyOfBuilder()
                                                                .tokens(newTokens)
                                                                .build();
                                                        Amplify.API.mutate(
                                                                ModelMutation.update(updatedUser),
                                                                response1 -> Log.i("MyAmplifyApp", "User tokens updated"),
                                                                error -> Log.e("MyAmplifyApp", "Failed to update user tokens", error)
                                                        );
                                                    }
                                                },
                                                error -> Log.e("MyAmplifyApp", "Query failed", error)
                                        );
                                    }
                                },
                                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
                        );
                        // Update the task's checked state and points in the UI
                        if (isChecked) {
                            holder.innerLayout.setBackgroundColor(holder.checkboxGreen);
                            buttonView.setClickable(false);
                        } else {
                            holder.innerLayout.setBackgroundColor(holder.unFinishedTask);
                        }
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