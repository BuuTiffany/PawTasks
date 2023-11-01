package com.example.pawtasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.core.Amplify;
import com.example.pawtasks.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void signIn() {
        String username = binding.usernameEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            // You might want to show an error message to the user
            Log.e("SignInActivity", "Username or password is empty");
            return;
        }

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    Log.i("AuthQuickStart", "Sign in succeeded: " + result.toString());
                    startActivity(new Intent(this, MainActivity.class));
                    finish(); // Call finish() if you want to remove SignInActivity from the back stack
                },
                error -> Log.e("AuthQuickStart", "Sign in failed", error)
        );
    }
}
