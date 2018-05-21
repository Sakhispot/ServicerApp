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
import com.amulyakhare.textdrawable.TextDrawable;
import  android.graphics.Color;
import android.widget.ImageView;

import com.example.sakhile.tasker.Class.Service;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Services> services;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, location, contact;
        public RelativeLayout serviceGround;
        public RelativeLayout serviceBackground;
        public ImageView initialsIcon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            location = (TextView) view.findViewById(R.id.location);
            contact = (TextView) view.findViewById(R.id.contact);
            serviceBackground= (RelativeLayout) view.findViewById(R.id.service_backgound);
            serviceGround= (RelativeLayout


                    ) view.findViewById(R.id.services_ground);
            initialsIcon = (ImageView) view.findViewById(R.id.initials_view);
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

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(service.name.substring(0, 1), Color.RED);

        holder.initialsIcon.setImageDrawable(drawable);
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
