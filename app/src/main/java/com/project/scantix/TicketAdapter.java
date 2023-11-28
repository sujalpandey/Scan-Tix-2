package com.project.scantix;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Ticket> ticketList;

    public TicketAdapter(Context context, ArrayList<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ticket_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket currentTicket = ticketList.get(position);

        holder.eventNameTextView.setText(currentTicket.getEventName());
        holder.eventDateTextView.setText(currentTicket.getEventDate());
        holder.ticketNoTextView.setText(currentTicket.getTicketNo());
        holder.userNameTextView.setText(currentTicket.getUserName());
        holder.eventVenueTextView.setText(currentTicket.getEventVenue());

        // You may load image (QR code) using an image loading library like Glide/Picasso into the ImageView (qrCodeImageView) if needed
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView ticketNoTextView;
        TextView userNameTextView;
        TextView eventVenueTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventName);
            eventDateTextView = itemView.findViewById(R.id.eventDate);
            ticketNoTextView = itemView.findViewById(R.id.rowNo);
            userNameTextView = itemView.findViewById(R.id.userName);
            eventVenueTextView = itemView.findViewById(R.id.eventVenue);


        }


    }
}
