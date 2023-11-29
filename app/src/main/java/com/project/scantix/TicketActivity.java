package com.project.scantix;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    String name;
    String eventUid;
    String eventName;
    String eventDate;
    String regStart;
    String regEnd;
    String totalSeats;
    String availableSeats;
    String eventVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();


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
                        eventUid = getIntent().getStringExtra("eventUid");
                        fetchEventData(eventUid);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





//        Ticket ticket = new Ticket(eventName, eventDate, availableSeats, name, "C1");
//        displayTicket(ticket);
    }

    private void fetchEventData(String eventUid) {
        DocumentReference eventRef = fstore.collection("Events").document(eventUid);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        eventName = document.getString("Event_Name");
                        eventDate = document.getString("Event_Date");
                        regStart = document.getString("Reg_Start");
                        regEnd = document.getString("Reg_End");
                        totalSeats = document.getString("Total_Seats");
                        availableSeats = document.getString("Available_Seats");
                        eventVenue = document.getString("Event_Venue");

                        // Parse totalSeats and availableSeats as integers
                        int totalSeatsInt = Integer.parseInt(totalSeats);
                        int availableSeatsInt = Integer.parseInt(availableSeats);

                        // Decrement availableSeats by 1
                        availableSeatsInt--;

                        // Calculate the current ticket number
                        int ticketNumber = totalSeatsInt - availableSeatsInt;

                        // Update the available seats in the database
                        updateAvailableSeats(eventUid, availableSeatsInt);

                        // Create and display the Ticket object
                        Ticket ticket = new Ticket(eventName, eventDate,String.valueOf(ticketNumber) , name, eventVenue);
                        displayTicket(ticket);
                        storeTicketInFirestore(ticket);
                    } else {
                        // Handle the case where the document doesn't exist
                    }
                } else {
                    // Handle the error fetching document
                    // Handle accordingly, e.g., show an error message
                }
            }
        });
    }

    private void storeTicketInFirestore(Ticket ticket) {
        String userId = fuser.getUid();
        String eventUid = getIntent().getStringExtra("eventUid");

        // Concatenate event UID and user UID to create a unique document ID
        String ticketDocId = eventUid + "_" + userId;

        DocumentReference userRef = fstore.collection("users").document(userId);
        CollectionReference ticketsRef = userRef.collection("Tickets");

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ticket.generateQrCodeBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("Event_Name", ticket.getEventName());
        ticketData.put("Event_Date", ticket.getEventDate());
        ticketData.put("Ticket_No", ticket.getTicketNo());
        ticketData.put("User_Name", ticket.getUserName());
        ticketData.put("Event_Venue", ticket.getEventVenue());
//        ticketData.put("qrCodeBitmap", bitmapData);

        // Create the document with the concatenated ID
        ticketsRef.document(ticketDocId)
                .set(ticketData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Ticket data added successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }



    private void updateAvailableSeats(String eventUid, int newAvailableSeats) {
        // Update the "Available_Seats" in the database
        DocumentReference eventRef = fstore.collection("Events").document(eventUid);
        eventRef.update("Available_Seats", String.valueOf(newAvailableSeats))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Available seats updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating available seats", e);
                    }
                });
    }


    private void displayTicket(Ticket ticket) {

        TextView eventNameTextView = findViewById(R.id.eventName);
        TextView eventVenueTextView = findViewById(R.id.eventVenue);
        TextView eventDateTextView = findViewById(R.id.eventDate);
        TextView ticketNoTextView = findViewById(R.id.rowNo);
        TextView userNameTextView = findViewById(R.id.userName);
        ImageView qrCodeImage = findViewById(R.id.qrCode);


        // Ticket ticket1 = new Ticket(eventName, eventDate, String.valueOf(ticketnumber), "Sujal", "C1");


        eventNameTextView.setText("Event: " + ticket.getEventName());
        eventVenueTextView.setText("Venue: " + ticket.getEventVenue());
        eventDateTextView.setText("Date: " + ticket.getEventDate());
        ticketNoTextView.setText("Ticket No.: " + ticket.getTicketNo());
        userNameTextView.setText("Name: " + ticket.getUserName());


    }

}

//    private void storeTicketInFirestore(Ticket ticket) {
//        String userId = fuser.getUid();
//        String eventUid = getIntent().getStringExtra("eventUid");
//
//        String ticketDocId = userId + "_" + eventUid;
//
//        DocumentReference userRef = fstore.collection("users").document(userId);
//        CollectionReference ticketsRef = userRef.collection("Tickets");
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ticket.generateQrCodeBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] bitmapData = byteArrayOutputStream.toByteArray();
//
//        Map<String, Object> ticketData = new HashMap<>();
//        ticketData.put("Event_Name", ticket.getEventName());
//        ticketData.put("Event_Date", ticket.getEventDate());
//        ticketData.put("Ticket_No", ticket.getTicketNo());
//        ticketData.put("User_Name", ticket.getUserName());
//        ticketData.put("Event_Venue", ticket.getEventVenue());
//        ticketData.put("qrCodeBitmap", bitmapData);
//
//        ticketsRef.document(ticketDocId)
//                .set(ticketData)
//                .addOnSuccessListener(aVoid -> {
//                    // Ticket data added successfully
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                });
//    }
//
//
//}

