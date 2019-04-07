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
import com.creativeminds.opinion.models.Candidate;
import com.creativeminds.opinion.models.Poll;

import java.util.List;

public class VoteCandidateAdapter extends RecyclerView.Adapter<VoteCandidateAdapter.MyViewHolder> {

    private Context mContext;
    private List<Candidate> candidateList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,party;
        public Button vote;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.candidate_name);
            party = (TextView) view.findViewById(R.id.candidate_party);
            logo = (ImageView)view.findViewById(R.id.candidate_logo);
            vote = (Button)view.findViewById(R.id.vote_candidate_btn);
        }
    }


    public VoteCandidateAdapter(Context mContext, List<Candidate> candidates) {
        this.mContext = mContext;
        this.candidateList = candidates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_candidate_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Candidate candidate = candidateList.get(position);
        holder.name.setText(candidate.getName());
        holder.party.setText(candidate.getPartyName());
        holder.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return candidateList.size();
    }
}
