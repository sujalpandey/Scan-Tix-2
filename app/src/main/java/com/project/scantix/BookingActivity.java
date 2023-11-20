package com.project.scantix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class BookingActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String eventUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        firestore = FirebaseFirestore.getInstance();

        // Get event UID from intent
        eventUid = getIntent().getStringExtra("eventUid");

        // Fetch event details from Firestore
        fetchEventDetails();
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
                        TextView eventNameTextView = findViewById(R.id.eventNameTextView);
                        eventNameTextView.setText(eventName);

                        TextView eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
                        eventDescriptionTextView.setText(eventDescription);

                        // Set values to other TextViews as needed
                        TextView eventDateTextView = findViewById(R.id.eventDateTextView);
                        eventDateTextView.setText(eventDate);

                        TextView regStartTextView = findViewById(R.id.regStartTextView);
                        regStartTextView.setText(regStart);

                        TextView regEndTextView = findViewById(R.id.regEndTextView);
                        regEndTextView.setText(regEnd);

                        TextView totalSeatsTextView = findViewById(R.id.totalSeatsTextView);
                        totalSeatsTextView.setText(totalSeats);

                        TextView availableSeatsTextView = findViewById(R.id.availableSeatsTextView);
                        availableSeatsTextView.setText(availableSeats);
                    } else {
                        // Document not found
                        // Handle accordingly, e.g., show an error message
                    }
                } else {
                    // Error fetching document
                    // Handle accordingly, e.g., show an error message
                }
            }
        });
    }
}