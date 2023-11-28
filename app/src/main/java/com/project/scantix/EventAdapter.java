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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Context context;
    ArrayList<event> eventlist;
    EventAdapter(Context context, ArrayList<event> eventlist){
        this.context = context;
        this.eventlist = eventlist;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.event_card,parent,false);
        ViewHolder viewholder = new ViewHolder(v);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.event_name.setText(eventlist.get(position).getEventName());
        holder.event_description.setText(eventlist.get(position).getEventDescription());
    }

    @Override
    public int getItemCount() {

        return eventlist.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView event_name;
        TextView event_description;
         Button rsvp_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.eventName);
            event_description = itemView.findViewById(R.id.eventDescription);
            rsvp_button = itemView.findViewById(R.id.rsvpButton);

            itemView.setOnClickListener(view -> {
                // Handle item click
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    event curr_event = eventlist.get(position);
                    navigateToDestination(curr_event);
                }
            });

            rsvp_button.setOnClickListener(view -> {
                // Handle RSVP button click
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    event curr_event = eventlist.get(position);
                    navigateToRSVP(curr_event);
                }
            });
        }



        private void navigateToDestination(event curr_event) {
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("eventUid", curr_event.getUid());
            context.startActivity(intent);
        }

        private void navigateToRSVP(event curr_event) {
            Intent intent = new Intent(context, TicketActivity.class);
            intent.putExtra("eventUid", curr_event.getUid());
            context.startActivity(intent);
        }
    }


}
