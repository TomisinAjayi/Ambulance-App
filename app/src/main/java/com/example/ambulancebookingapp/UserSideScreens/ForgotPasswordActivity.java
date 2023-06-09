package com.example.ambulancebookingapp.UserSideScreens;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ambulancebookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText forgotEmailEditText;
    Button sendLinkButton;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmailEditText = findViewById(R.id.forgotEmailEditText);
        sendLinkButton = findViewById(R.id.sendLinkButton);
        progressBar = findViewById(R.id.forgotPasswordProgressBar);
        auth = FirebaseAuth.getInstance();

        sendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgotEmailEditText.getText().toString().trim();

                if(email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(getApplicationContext(), "Enter a correct email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                forgotEmailEditText.setVisibility(View.INVISIBLE);
                sendLinkButton.setVisibility(View.INVISIBLE);

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        forgotEmailEditText.setVisibility(View.VISIBLE);
                        sendLinkButton.setVisibility(View.VISIBLE);
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Check your email to reset password", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Connection failed, try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        forgotEmailEditText.setVisibility(View.VISIBLE);
                        sendLinkButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}