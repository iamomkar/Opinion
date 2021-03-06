package com.creativeminds.opinion.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.activities.CandidateDetailsActivity;
import com.creativeminds.opinion.activities.CandidateStatisticsActivity;
import com.creativeminds.opinion.models.Candidate;
import com.google.gson.Gson;

import java.util.List;

public class CandidateListAdapter extends RecyclerView.Adapter<CandidateListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Candidate> candidateList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,party;
        public Button vote;
        public ImageView logo;
        public CardView candidateCard;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.candidate_name);
            party = (TextView) view.findViewById(R.id.candidate_party);
            logo = (ImageView)view.findViewById(R.id.candidate_logo);
            vote = (Button)view.findViewById(R.id.vote_candidate_btn);
            candidateCard = (CardView)view.findViewById(R.id.candidate_card);
        }
    }


    public CandidateListAdapter(Context mContext, List<Candidate> candidates) {
        this.mContext = mContext;
        this.candidateList = candidates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_candidate_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Candidate candidate = candidateList.get(position);
        holder.name.setText(candidate.getName());
        holder.party.setText(candidate.getPartyName());
        //holder.vote.setVisibility(View.INVISIBLE);
        holder.vote.setText(candidate.getVotes());
        holder.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, CandidateStatisticsActivity.class).putExtra("pid",candidate.getPollId()).putExtra("cid",candidate.getCid()));
            }
        });
        holder.candidateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                mContext.startActivity(new Intent(mContext, CandidateDetailsActivity.class).putExtra("candidate",gson.toJson(candidate)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return candidateList.size();
    }
}
