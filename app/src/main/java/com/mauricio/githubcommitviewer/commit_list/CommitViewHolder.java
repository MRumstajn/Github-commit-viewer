package com.mauricio.githubcommitviewer.commit_list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;

public class CommitViewHolder extends RecyclerView.ViewHolder {
    private TextView commitDescription;
    private TextView commitAuthor;
    private TextView commitHash;

    public CommitViewHolder(@NonNull View itemView) {
        super(itemView);

        initComponents(itemView);
    }

    private void initComponents(View itemView){
        commitDescription = itemView.findViewById(R.id.commit_card_description);
        commitAuthor = itemView.findViewById(R.id.commit_card_author);
        commitHash = itemView.findViewById(R.id.commit_card_hash);
    }

    public void setDescription(String description){
        commitDescription.setText(description);
    }

    public void setAuthor(String author){
        commitAuthor.setText(author);
    }

    public void setHash(String hash){
        commitHash.setText(hash);
    }
}
