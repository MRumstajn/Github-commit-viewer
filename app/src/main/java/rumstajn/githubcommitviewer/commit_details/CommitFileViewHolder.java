package rumstajn.githubcommitviewer.commit_details;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;
import rumstajn.githubcommitviewer.Util;

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
