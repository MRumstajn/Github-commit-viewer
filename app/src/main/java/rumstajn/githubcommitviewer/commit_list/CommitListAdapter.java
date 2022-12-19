package rumstajn.githubcommitviewer.commit_list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauricio.githubcommitviewer.R;

import rumstajn.githubcommitviewer.model.api_response.commit.Commit;
import rumstajn.githubcommitviewer.model.api_response.commit.CommitObject;

import java.util.ArrayList;
import java.util.List;

public class CommitListAdapter extends RecyclerView.Adapter<CommitViewHolder> {
    private List<CommitObject> commitObjects;
    private IListItemClickListener itemClickListener;

    public CommitListAdapter(){
        commitObjects = new ArrayList<>();
    }

    @NonNull
    @Override
    public CommitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commit_list_card, parent, false);

        return new CommitViewHolder(inflated);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitViewHolder holder, int position) {
        CommitObject commitObj = commitObjects.get(position);
        Commit commit = commitObjects.get(position).getCommit();

        holder.setDescription(commit.getMessage());
        holder.setAuthor(commit.getAuthor().getName());
        holder.setHash(commitObj.getSha().substring(0, 10));
        holder.setClickListener(() -> {
            itemClickListener.onListItemClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return commitObjects.size();
    }

    public void addCommitObject(CommitObject commitObj){
        commitObjects.add(commitObj);
        notifyItemChanged(commitObjects.size() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCommitObjects(List<CommitObject> commitObjects) {
        this.commitObjects = commitObjects;
        notifyDataSetChanged();
    }

    public List<CommitObject> getCommitObjects() {
        return commitObjects;
    }

    public CommitObject getCommitObjetAt(int position){
        return commitObjects.get(position);
    }

    public void setItemClickListener(IListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
