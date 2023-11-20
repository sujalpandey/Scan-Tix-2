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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        fetchDataFromFirestore();


        String id = fuser.getUid();


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

    private void fetchDataFromFirestore() {
        CollectionReference eventsCollection = fstore.collection("Events");

        eventsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                        event Event = new event(document.getString("Event_Name"),document.getString("Event_Des"), document.getId());
                        if (Event != null) {
                            eventlist.add(Event);
                        }
                    }

                    // Now eventList contains all the events from the "Events" collection
                    // You can use it as needed
                    for (event Event : eventlist) {
                        Log.d("Event", "Event UID: " + Event.getUid());
                        Log.d("Event", "Event Name: " + Event.getEventName());
                        Log.d("Event", "Event Description: " + Event.getEventDescription());
                    }
                    Log.d("Event", "Number of events: " + eventlist.size());

                    EventAdapter adapter = new EventAdapter(this, eventlist);
                    RecyclerView event_recycler = findViewById(R.id.eventCard);
                    event_recycler.setAdapter(adapter);
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
    }
}

