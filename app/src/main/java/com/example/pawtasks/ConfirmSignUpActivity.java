package com.example.pawtasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.core.Amplify;
import com.example.pawtasks.databinding.ActivityConfirmSignUpBinding;

public class ConfirmSignUpActivity extends AppCompatActivity {
    private ActivityConfirmSignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String email = getIntent().getStringExtra("Email");
        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSignUp(email, binding.confirmationCodeEditText.getText().toString());
            }
        });
    }

    public void confirmSignUp(String username, String confirmationCode) {
        Amplify.Auth.confirmSignUp(
                username,
                confirmationCode,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    if (result.isSignUpComplete()) {
                        // Navigate back to sign in page
                        runOnUiThread(() -> {
                            // Assuming you are using an Activity, replace with appropriate context or navigation logic for your app
                            Intent intent = new Intent(this, SignInActivity.class);
                            startActivity(intent);
                        });
                    }
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}