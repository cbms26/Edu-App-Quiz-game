package com.nga26.example.ngawangeduapp1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nga26.example.ngawangeduapp1.R;
import com.nga26.example.ngawangeduapp1.model.User;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<User> userList;

    public ScoreAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    //create new view with row_item.xml
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return (new ScoreViewHolder(view));
    }

    @Override
    //replace the content of an existing view with new data (if applicable)
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScoreViewHolder scoreViewHolder = (ScoreViewHolder) holder;
        User userObj = userList.get(position);
        scoreViewHolder.dateView.setText(userObj.getDate());
        scoreViewHolder.levelView.setText(userObj.getLevel());
        scoreViewHolder.scoreView.setText(userObj.getScore());
        scoreViewHolder.usernameView.setText(userObj.getUsername());
        scoreViewHolder.durationView.setText(userObj.getDuration());
    }

    @Override

    public int getItemCount() {
        return userList.size();
    }
    //based on the row_item.xml, define the structure of the viewholder
    static class ScoreViewHolder extends RecyclerView.ViewHolder{
        TextView usernameView;
        TextView levelView;
        TextView scoreView;
        TextView durationView;
        TextView dateView;
        //given row_item, construct a SoreViewHolder object
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.usernameView);
            levelView = itemView.findViewById(R.id.levelView);
            scoreView = itemView.findViewById(R.id.scoreView);
            durationView = itemView.findViewById(R.id.durationView);
            dateView = itemView.findViewById(R.id.dateView);
        }
    }
}
