package com.project.scantix;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    Button login;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        Button registerBtn = findViewById(R.id.userRegister);
        registerBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent1);

        });

        useremail = findViewById(R.id.userEmail);
        userpassword = findViewById(R.id.userPassword);
        login = findViewById(R.id.userLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email = useremail.getText().toString();
                password = userpassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    showToast("Enter Email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    showToast("Enter Password");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    showToast("Login successful");
                                    Intent intent = new Intent(LoginActivity.this,
                                            dashboard.class);
                                    startActivity(intent);
                                } else {

                                    showToast("Authentication Failed!");

                                }
                            }
                        });
              }
            });

            }

        private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); // Added .show() at the end
         }


}