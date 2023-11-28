package com.project.scantix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClEvents extends AppCompatActivity {


    FirebaseAuth fauth;
    FirebaseUser fuser;
    FirebaseFirestore fstore;
    ArrayList<event> eventlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cl_events);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        fuser = fauth.getCurrentUser();

        RecyclerView event_recycler = findViewById(R.id.eventCard);
        event_recycler.setLayoutManager(new LinearLayoutManager(this));

        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));
        eventlist.add(new event("HCL Bridge","Join for fully fledged DSA preparation","jsdlikdijenadf"));

        fetchDataFromFirestore();
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