package com.example.pawtasks;
import static com.amplifyframework.auth.result.AuthSessionResult.Type.FAILURE;

import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.User;
import com.example.pawtasks.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.AmplifyException;
import android.view.MenuItem;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static int tokenCount = 2;

    // View Model
    private PawTasksViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PawTasksViewModel.class);
        setupAuthentication();
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        // Fragments
        TaskPage taskPage = new TaskPage();
        GachaPage gachaPage = new GachaPage();
        PetViewPage petViewPage = new PetViewPage();

        // Set default fragment
        replaceFragment(taskPage);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.tasks) {
                replaceFragment(taskPage);
            } else if (id == R.id.gacha) {
                replaceFragment(gachaPage);
            } else if (id == R.id.pet) {
                replaceFragment(petViewPage);
            }

            return true;
        });
    }

    // To change the fragment to different pages
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navigationMenu_FrameLayout, fragment);
        fragmentTransaction.commitNow();
    }

    private void setupAuthentication() {
        Amplify.Auth.fetchAuthSession(
                result -> runOnUiThread(() -> {
                    Log.i("AuthQuickStart", result.toString());
                    if (!result.isSignedIn()) {
                        // If the user is not signed in, start SignInActivity
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish(); // Call finish() if you want to remove MainActivity from the back stack
                    } else {
                        // If user is signed in, check if the User row exists in DynamoDB, if not, create it
                        checkOrCreateUser();

                        // Setup bottom navigation view since the user is signed in
                    }
                }),
                error -> Log.e("AuthQuickStart", error.toString())
        );
    }

    private void checkOrCreateUser() {
        Amplify.Auth.getCurrentUser(
                currentUser -> {
                    if (currentUser != null) {
                        String userId = currentUser.getUserId();
                        Amplify.API.query(
                                ModelQuery.get(User.class, userId),
                                response -> {
                                    if (response.getData() == null) {
                                        // User row does not exist, create it
                                        createUserRow(userId);
                                    }
                                },
                                error -> Log.e("MyAmplifyApp", "Query failure", error)
                        );
                    } else {
                        Log.e("MyAmplifyApp", "User is not signed in");
                    }
                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
    }


    private void createUserRow(String userId) {
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    String email = null;
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            email = attribute.getValue();
                            break;
                        }
                    }
                    if (email != null) {
                        User newUser = User.builder()
                                .email(email)
                                .id(userId)
                                .tokens(0)
                                .build();

                        Amplify.API.mutate(
                                ModelMutation.create(newUser),
                                response -> Log.i("MyAmplifyApp", "Added User with id: " + response.getData().getId()),
                                error -> Log.e("MyAmplifyApp", "Create failed", error)
                        );
                    } else {
                        Log.e("MyAmplifyApp", "Email not found");
                    }
                },
                error -> Log.e("MyAmplifyApp", "Failed to fetch user attributes", error)
        );
    }
}