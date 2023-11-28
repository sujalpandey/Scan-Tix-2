package com.project.scantix;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Ticket concertTicket = new Ticket(
                "Concert",
                "Awesome concert with your favorite band!",
                new Date());

        displayTicket(concertTicket);
    }

    private void displayTicket(Ticket ticket) {
        TextView eventNameTextView = findViewById(R.id.eventNameTextView);
        TextView eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
        TextView eventDateTextView = findViewById(R.id.eventDateTextView);
        TextView ticketPriceTextView = findViewById(R.id.ticketPriceTextView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        eventNameTextView.setText(ticket.getEventName());
        eventDescriptionTextView.setText(ticket.getEventDescription());
        eventDateTextView.setText("Date: " + dateFormat.format(ticket.getEventDate()));

    }
}
