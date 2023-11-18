package com.project.scantix;

import android.content.Context;
import android.media.metrics.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firestore.v1.Precondition;

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

        holder.event_name.setText(eventlist.get(position).eventName);
        holder.event_description.setText(eventlist.get(position).eventDescription);
    }

    @Override
    public int getItemCount() {

        return eventlist.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView event_name;
        TextView event_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name =  itemView.findViewById(R.id.eventName);
            event_description =  itemView.findViewById(R.id.eventDescription);


        }
    }


}
