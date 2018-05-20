package com.example.sakhile.tasker;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.*;

import com.example.sakhile.tasker.Class.Service;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Services> services;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, location, contact;
        public LinearLayout serviceGround;
        public RelativeLayout serviceBackground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            location = (TextView) view.findViewById(R.id.location);
            contact = (TextView) view.findViewById(R.id.contact);
            serviceBackground= (RelativeLayout) view.findViewById(R.id.service_backgound);
            serviceGround= (LinearLayout)view.findViewById(R.id.services_ground);
        }
    }


    public Adapter(List<Services> services) {
        this.services = services;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Services service = services.get(position);
        holder.name.setText(service.name);
        holder.location.setText(service.location);
        holder.contact.setText(service.contact);
    }

    @Override
    public int getItemCount() {
        return services.size();

    }


    public void removeItem(int position) {
        services.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
}
