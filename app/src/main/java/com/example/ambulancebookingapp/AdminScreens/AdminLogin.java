package com.example.ambulancebookingapp.AdminScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ambulancebookingapp.R;
import com.example.ambulancebookingapp.models.AdminModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    AdminModel adminDetails;

    TextView loginAsUser;
    Button adminLoginButton;
    EditText emailEditText, passwordEditText;
    ProgressBar progressBar;

    //shared prefs variables
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "SharedPrefs";
    public static final String userType = "userType";
    public static final String userNodeKey = "userKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        firebaseFirestore = FirebaseFirestore.getInstance();

        loginAsUser = (TextView) findViewById(R.id.loginAsUser);
        adminLoginButton = (Button) findViewById(R.id.loginAdminButton);
        emailEditText = (EditText) findViewById(R.id.adminIdEditText);
        passwordEditText = (EditText) findViewById(R.id.adminPasswordEditText);
        progressBar = (ProgressBar) findViewById(R.id.adminLoginProgressBar);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        loginAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adminLoginButton.setVisibility(View.GONE);
                loginAsUser.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                //get values of admin details from the database
                firebaseFirestore.collection("Admin").document("Admin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        adminLoginButton.setVisibility(View.VISIBLE);
                        loginAsUser.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        AdminModel adminModel = documentSnapshot.toObject(AdminModel.class);
                        adminDetails = new AdminModel(adminModel.getEmail(), adminModel.getPassword(), 0, 0);

                        // get values from the edit texts
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        //validate the input and login
                        if(email.equals(adminDetails.getEmail()) && password.equals(adminDetails.getPassword()))
                        {
                            editor.putInt(userType, 3);
                            editor.putString(userNodeKey, "appambulance");
                            editor.apply();
                            Intent intent = new Intent(AdminLogin.this, AdminDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        adminLoginButton.setVisibility(View.VISIBLE);
                        loginAsUser.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}