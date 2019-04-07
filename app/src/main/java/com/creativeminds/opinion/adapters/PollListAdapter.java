package com.creativeminds.opinion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.Poll;

import java.util.ArrayList;
import java.util.List;

public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Poll> pollList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,description;
        public Button details;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.poll_title);
            description = (TextView) view.findViewById(R.id.poll_desc);
            logo = (ImageView)view.findViewById(R.id.poll_logo);
            details = (Button)view.findViewById(R.id.poll_details_fab);
        }
    }


    public PollListAdapter(Context mContext, List<Poll> _websiteList) {
        this.mContext = mContext;
        this.pollList = _websiteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_poll_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Poll poll = pollList.get(position);
        holder.title.setText(poll.getTitle());
        holder.description.setText(poll.getDescription());
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return pollList.size();
    }
}
