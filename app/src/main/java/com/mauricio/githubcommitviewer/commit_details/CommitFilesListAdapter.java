package com.mauricio.githubcommitviewer.commit_details;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;

import java.util.ArrayList;
import java.util.List;

public class CommitFilesListAdapter extends RecyclerView.Adapter<CommitFileViewHolder> {
    private List<CommitFile> commitFiles;

    public CommitFilesListAdapter(){
        this.commitFiles = new ArrayList<>();
    }

    @NonNull
    @Override
    public CommitFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commit_file_card, parent, false);
        return new CommitFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitFileViewHolder holder, int position) {
        CommitFile commitFile = commitFiles.get(position);
        holder.setPath(commitFile.getPath());
        holder.setContent(commitFile.getContent());
        holder.setLineCount(commitFile.getContent().split("\r\n|\r|\n").length+1);
    }

    @Override
    public int getItemCount() {
        return commitFiles.size();
    }

    public void addCommitFile(CommitFile commitFile){
        commitFiles.add(commitFile);
        notifyItemChanged(commitFiles.size() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCommitFiles(List<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
        notifyDataSetChanged();
    }
}
