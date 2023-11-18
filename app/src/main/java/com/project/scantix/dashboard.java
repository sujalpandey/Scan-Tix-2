package com.project.scantix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class dashboard extends AppCompatActivity {

    Button logout;

    FirebaseAuth fauth;
    FirebaseUser fuser;
    FirebaseFirestore fstore;
    String name,email,phone;
    TextView username;

    ArrayList<event> eventlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        logout = findViewById(R.id.signoutbtn);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        fuser = fauth.getCurrentUser();

        username = findViewById(R.id.userName);

        RecyclerView event_recycler = findViewById(R.id.eventCard);

        event_recycler.setLayoutManager(new LinearLayoutManager(this));



        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));
        eventlist.add(new event("Deloite Bridge","Join to get superb learning series"));

        EventAdapter adapter  = new EventAdapter(this,eventlist);

        String id = fuser.getUid();
        event_recycler.setAdapter(adapter);




        DocumentReference docRef = fstore.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        name = document.getString("fName");

                        username.setText(name);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }
}