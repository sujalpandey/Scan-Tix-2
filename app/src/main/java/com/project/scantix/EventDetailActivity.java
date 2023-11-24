package com.project.scantix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class EventDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String eventUid;

    Button rsvp_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        rsvp_button = findViewById(R.id.RsvpButton);

        firestore = FirebaseFirestore.getInstance();

        // Get event UID from intent
        eventUid = getIntent().getStringExtra("eventUid");

        // Fetch event details from Firestore
        fetchEventDetails();

        rsvp_button.setOnClickListener(view -> {
            Intent intent = new Intent(EventDetailActivity.this, BookingActivity.class);
            intent.putExtra("eventUid", eventUid);
            startActivity(intent);
        });
    }

    private void fetchEventDetails() {
        DocumentReference eventRef = firestore.collection("Events").document(eventUid);

        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document found, retrieve fields and update UI
                        String eventName = document.getString("Event_Name");
                        String eventDescription = document.getString("Event_Des");
                        String eventDate = document.getString("Event_Date");
                        String regStart = document.getString("Reg_Start");
                        String regEnd = document.getString("Reg_End");
                        String totalSeats = document.getString("Total_Seats");
                        String availableSeats = document.getString("Available_Seats");

                        // Set values to TextViews
                        TextView eventNameTextView = findViewById(R.id.eventName);
                        eventNameTextView.setText(eventName);

                        TextView eventDescriptionTextView = findViewById(R.id.eventDescription);
                        eventDescriptionTextView.setText(eventDescription);

                        // Set values to other TextViews as needed
                        TextView eventDateTextView = findViewById(R.id.eventDate);
                        eventDateTextView.setText(eventDate);

                        TextView regStartTextView = findViewById(R.id.regStart);
                        regStartTextView.setText(regStart);

                        TextView regEndTextView = findViewById(R.id.regEnd);
                        regEndTextView.setText(regEnd);

                        TextView totalSeatsTextView = findViewById(R.id.totalSeats);
                        totalSeatsTextView.setText(totalSeats);

                        TextView availableSeatsTextView = findViewById(R.id.availableSeats);
                        availableSeatsTextView.setText(availableSeats);
                    } else {

                    }
                } else {
                    // Error fetching document
                    // Handle accordingly, e.g., show an error message
                }
            }
        });


    }
}