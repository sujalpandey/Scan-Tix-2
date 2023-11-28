package com.project.scantix;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TicketActivity extends AppCompatActivity {

    private FirebaseUser fuser;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        String eventUid = getIntent().getStringExtra("eventUid");
        fetchEventDetails(eventUid);
    }

    private void fetchEventDetails(String eventUid) {
        DocumentReference eventRef = fstore.collection("Events").document(eventUid);

        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int availableTickets = document.getLong("availableTickets") != null ?
                            document.getLong("availableTickets").intValue() : 0;

                    if (availableTickets > 0) {
                        Ticket concertTicket = new Ticket(
                                document.getString("Event_Name"),
                                document.getString("Event_Des"),
                                document.getString("Event_Date"),
                                "1", // Assuming some default ticket number
                                "sujal", // Assuming some default user name
                                eventUid // Event UID retrieved from Intent
                        );

                        // Generate and display the ticket
                        generateAndDisplayTicket(concertTicket);
                    } else {
                        // No available tickets
                        // Handle situation when no tickets are available
                    }
                } else {
                    // Event document does not exist
                    // Handle situation when event details are not found
                }
            } else {
                // Task failed with an exception
                // Handle exception
            }
        });
    }

    private void generateAndDisplayTicket(Ticket ticket) {
        // Display the ticket details in the UI
        displayTicket(ticket);

        // Store the ticket in Firestore
        storeTicketInFirestore(ticket);
    }

    private void displayTicket(Ticket ticket) {
        // Your implementation to display ticket details in UI
    }

    private void storeTicketInFirestore(Ticket ticket) {
        String userId = fuser.getUid();

        DocumentReference userRef = fstore.collection("users").document(userId);
        CollectionReference ticketsRef = userRef.collection("Tickets");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ticket.getQrCodeBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("eventUid", ticket.getEventUid());
        ticketData.put("qrCodeBitmap", bitmapData);

        ticketsRef.add(ticketData)
                .addOnSuccessListener(documentReference -> {
                    // Ticket data added successfully
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}

