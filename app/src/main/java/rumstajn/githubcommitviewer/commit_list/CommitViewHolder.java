package rumstajn.githubcommitviewer.commit_list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;
import rumstajn.githubcommitviewer.Util;

public class CommitViewHolder extends RecyclerView.ViewHolder {
    private static final int MAX_MESSAGE_CHARS = 20;
    private static final int MAX_AUTHOR_CHARS = 15;
    private static final int MAX_SHA_CHARS = 10;

    private TextView commitDescription;
    private TextView commitAuthor;
    private TextView commitHash;
    private ICommitViewClickListener clickListener;

    public CommitViewHolder(@NonNull View itemView) {
        super(itemView);

        initComponents(itemView);

        itemView.setOnClickListener((view) -> {
            clickListener.onCommitViewClicked();
        });
    }

    private void initComponents(View itemView){
        commitDescription = itemView.findViewById(R.id.commit_card_description);
        commitAuthor = itemView.findViewById(R.id.commit_card_author);
        commitHash = itemView.findViewById(R.id.commit_card_hash);
    }

    public void setDescription(String description) {
        commitDescription.setText(Util.shrinkString(description, MAX_MESSAGE_CHARS));
    }

    public void setAuthor(String author){
        commitAuthor.setText(Util.shrinkString(author, MAX_AUTHOR_CHARS));
    }

    public void setHash(String hash){
        commitHash.setText(Util.shrinkString(hash, MAX_SHA_CHARS));
    }

    public void setClickListener(ICommitViewClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
