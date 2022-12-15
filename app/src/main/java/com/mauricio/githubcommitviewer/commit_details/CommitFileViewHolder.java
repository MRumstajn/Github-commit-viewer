package com.mauricio.githubcommitviewer.commit_details;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;
import com.mauricio.githubcommitviewer.Util;

public class CommitFileViewHolder extends RecyclerView.ViewHolder {
    private static final int CONTENT_VISIBLE_LINE_COUNT = 100;

    private TextView path;
    //private TextView content;
    private TextView content;

    @SuppressLint("ClickableViewAccessibility")
    public CommitFileViewHolder(@NonNull View itemView) {
        super(itemView);

        path = itemView.findViewById(R.id.file_name_label);
        content = itemView.findViewById(R.id.file_content);

        // FIXME files too far apart and last file not shown fully
        // MIND THE RATE LIMIT
        // try to add child views to activity instead of recycler view
    }

    public void setPath(String path) {
        this.path.setText(path);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void setLineCount(int lineCount){
        content.setLines(Util.clampValueToMax(lineCount, CONTENT_VISIBLE_LINE_COUNT));
    }
}
