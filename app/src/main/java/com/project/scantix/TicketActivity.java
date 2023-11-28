package com.project.scantix;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
                                "sujal" // Assuming some default user name

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
        // Assuming you have TextViews in your ticket_card layout
        // Assume you have already inflated the main layout (activity_ticket.xml)

// Obtain a reference to the included layout
        View includedLayout = findViewById(R.id.ticket_card);

// Obtain references to TextViews inside the included layout
        TextView eventNameTextView = includedLayout.findViewById(R.id.eventName);
        TextView eventVenueTextView = includedLayout.findViewById(R.id.eventVenue);
        TextView eventDateTextView = includedLayout.findViewById(R.id.eventDate);
        TextView ticketNoTextView = includedLayout.findViewById(R.id.rowNo);
        TextView userNameTextView = includedLayout.findViewById(R.id.userName);

// Create a Ticket object with your data (replace this with your actual data)
        Ticket ticket1 = new Ticket("HCL", "22/1123", "1", "Sujal", "C1");

// Set the text values based on the Ticket object
        eventNameTextView.setText(ticket.getEventName());
        eventVenueTextView.setText(ticket.getEventVenue());
        eventDateTextView.setText(ticket.getEventDate());
        ticketNoTextView.setText(ticket.getTicketNo());
        userNameTextView.setText(ticket.getUserName());

    }


    private void storeTicketInFirestore(Ticket ticket) {
        String userId = fuser.getUid();
        String eventUid = getIntent().getStringExtra("eventUid");

        String ticketDocId = userId + "_" + eventUid;

        DocumentReference userRef = fstore.collection("users").document(userId);
        CollectionReference ticketsRef = userRef.collection("Tickets");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ticket.generateQrCodeBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("Event_Name", ticket.getEventName());
        ticketData.put("Event_Date", ticket.getEventDate());
        ticketData.put("Ticket_No", ticket.getTicketNo());
        ticketData.put("User_Name", ticket.getUserName());
        ticketData.put("Event_Venue", ticket.getEventVenue());
        ticketData.put("qrCodeBitmap", bitmapData);

        ticketsRef.document(ticketDocId)
                .set(ticketData)
                .addOnSuccessListener(aVoid -> {
                    // Ticket data added successfully
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }


}

