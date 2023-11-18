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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.auth.FirebaseUser;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText useremail, userpassword, userpasswordcnf, username, userphone;
    Button register;
    String userId;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        username = findViewById(R.id.userName);
        useremail = findViewById(R.id.userEmail);
        userphone = findViewById(R.id.userPhone);
        userpassword = findViewById(R.id.userPassword);
        userpasswordcnf = findViewById(R.id.userPasswordCnf);

        register = findViewById(R.id.signUp);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, passwordcnf, name, contact;

                name = username.getText().toString();
                contact = userphone.getText().toString();
                email = useremail.getText().toString();
                password = userpassword.getText().toString().trim();
                passwordcnf = userpasswordcnf.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    showToast("Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("Enter Password");
                    return;
                }
                if (TextUtils.isEmpty(passwordcnf)) {
                    showToast("Enter  Confirm Password");
                    return;
                }

                if (!TextUtils.equals(password, passwordcnf)) {
                    showToast("Confirm Password do not match");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser checkuser = mAuth.getCurrentUser();
                                    if (checkuser != null) {
                                        userId = checkuser.getUid();
                                    } else {
                                        showToast("User is null. Registration Failed!");
                                        return;
                                    }

                                    showToast("Registration is successful");


                                    Map<String, Object> user = new HashMap<>();
                                    user.put("fName", name);
                                    user.put("phoneNo", contact);
                                    user.put("email", email);


                                    fStore.collection("users").document(userId)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                    showToast("Data Uploaded Failed");
                                                }
                                            });

                                    Intent intent = new Intent(getApplicationContext(), dashboard.class);
                                    startActivity(intent);
                                    finish();

                                } else {


                                    showToast("Registration Failed!");
                                }
                            }
                        });

            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}