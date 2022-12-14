package rumstajn.githubcommitviewer.commit_details;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.exception.RateLimitExceededException;
import rumstajn.githubcommitviewer.model.api_response.blob.BlobEncoding;
import rumstajn.githubcommitviewer.model.api_response.blob.BlobObject;
import rumstajn.githubcommitviewer.model.api_response.commit.Commit;
import rumstajn.githubcommitviewer.model.api_response.commit.CommitObject;
import rumstajn.githubcommitviewer.model.api_response.tree.TreeEntry;
import rumstajn.githubcommitviewer.model.api_response.tree.TreeEntryType;
import rumstajn.githubcommitviewer.model.api_response.tree.TreeObject;
import rumstajn.githubcommitviewer.task.FetchBlobTask;
import rumstajn.githubcommitviewer.task.FetchTreeObjectTask;
import rumstajn.githubcommitviewer.task.IFetchBlobTaskListener;
import rumstajn.githubcommitviewer.task.IFetchTreeObjectTaskListener;
import rumstajn.githubcommitviewer.task.TaskManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitDetailsActivity extends AppCompatActivity
        implements IFetchTreeObjectTaskListener, AdapterView.OnItemSelectedListener, IFetchBlobTaskListener {
    private Button backButton;
    private TextView authorLabel;
    private TextView dateLabel;
    private TextView messageLabel;
    private TextView verifiedLabel;
    private ObjectMapper mapper;
    private Spinner currentFileSpinner;
    private ArrayAdapter<String> fileSpinnerAdapter;
    private TextView fileContentDisplay;
    private List<CommitFile> commitFiles;
    private Map<String, String> treeEntryToPathMap;
    private String accessToken;
    private boolean loadingTree;
    private List<TreeEntry> treeEntryQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_details);

        initComponents();

        // load commit object
        Intent intent = getIntent();
        CommitObject commitObj = null;
        try {
            commitObj = mapper.readValue(
                    intent.getStringExtra("commit_obj"), CommitObject.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Util.makeToast("Could not display details for this commit", this);
            exitActivity();
            return;
        }
        accessToken = intent.getStringExtra("access_token");

        setAttributeLabels(commitObj);
        loadTreeObj(commitObj);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        mapper = new ObjectMapper();
        commitFiles = new ArrayList<>();
        treeEntryToPathMap = new HashMap<>();
        treeEntryQueue = new ArrayList<>();

        backButton = findViewById(R.id.commit_details_back_button);
        backButton.setOnClickListener((view) -> {
            exitActivity();
        });
        authorLabel = findViewById(R.id.author_label);
        dateLabel = findViewById(R.id.date_label);
        messageLabel = findViewById(R.id.message_label);
        verifiedLabel = findViewById(R.id.verified_label);
        currentFileSpinner = findViewById(R.id.current_file_spinner);
        fileSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        currentFileSpinner.setAdapter(fileSpinnerAdapter);
        currentFileSpinner.setOnItemSelectedListener(this);
        fileContentDisplay = findViewById(R.id.file_content_display);

    }

    private void setAttributeLabels(CommitObject commitObject) {
        Commit commit = commitObject.getCommit();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

        authorLabel.setText(commit.getAuthor().getName());
        dateLabel.setText(dateFormat.format(commit.getAuthor().getDate()));
        messageLabel.setText(commit.getMessage());
        verifiedLabel.setText(Boolean.toString(commit.getVerification().isVerified()));
    }

    private void loadTreeObj(CommitObject commitObject) {
        loadingTree = true;
        FetchTreeObjectTask task = new FetchTreeObjectTask(commitObject.getCommit()
                .getTree().getUrl(), accessToken);
        task.setListener(this);
        TaskManager.getInstance().runTaskLater(task);
    }

    /*
     * commit (tree url) -> tree_obj (entries)(entry_url) -> entry (block/tree url)
     *
     * */

    private void loadTree(TreeEntry treeEntry) {
        loadingTree = true;
        FetchTreeObjectTask task = new FetchTreeObjectTask(treeEntry.getUrl(), accessToken);
        task.setListener(this);
        TaskManager.getInstance().runTaskLater(task);
    }

    private void loadBlob(TreeEntry treeEntry) {
        String url = treeEntry.getUrl();
        FetchBlobTask task = new FetchBlobTask(url, accessToken);
        task.setListener(this);
        treeEntryToPathMap.put(treeEntry.getSha(), treeEntry.getPath());
        TaskManager.getInstance().runTaskLater(task);
    }

    private void exitActivity() {
        finish();
    }

    @Override
    public void onFetchedTreeObject(TreeObject treeObject) {
        treeEntryQueue.addAll(treeObject.getTree());
        loadNextTreeEntry();
    }

    private void loadNextTreeEntry() {
        if (treeEntryQueue.size() > 0 && loadingTree) {
            TreeEntry entry = treeEntryQueue.remove(0);
            if (entry.getType().equals(TreeEntryType.BLOB.getName())) {
                loadBlob(entry);
            } else if (entry.getType().equals(TreeEntryType.TREE.getName())) {
                loadTree(entry);
            }
        }
    }

    @Override
    public void onFetchTreeObjectError(Exception e) {
        loadingTree = false;
        if (e instanceof RateLimitExceededException) {
            displayRateLimitErrorMsg((RateLimitExceededException) e);
        }
        Util.makeToast("Failed to fetch files", this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        fileContentDisplay.setText(commitFiles.get(i).getContent());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        fileContentDisplay.setText(null);
    }

    @Override
    public void onFetchedBlob(BlobObject blobObject) {
        String encodedContent = blobObject.getContent();
        String content = null;
        if (blobObject.getEncoding().equals(BlobEncoding.BASE64.getName())) {
            byte[] contentBytes = Base64.decode(encodedContent, Base64.DEFAULT);
            content = new String(contentBytes);
        }
        if (content == null) {
            Util.makeToast("Encountered an unsupported file encoding \""
                    + blobObject.getEncoding() + "\"", this);
            return;
        }

        String path = treeEntryToPathMap.get(blobObject.getSha());
        assert path != null;
        CommitFile commitFile = new CommitFile(content, path);
        runOnUiThread(() -> {
            commitFiles.add(commitFile);
            fileSpinnerAdapter.add(path);
        });
        loadNextTreeEntry();
    }


    @Override
    public void onFetchBlobError(Exception e) {
        loadingTree = false;
        if (e instanceof RateLimitExceededException) {
            displayRateLimitErrorMsg((RateLimitExceededException) e);
        } else {
            Util.makeToast("Failed to fetch some files", this);
        }

    }

    private void displayRateLimitErrorMsg(RateLimitExceededException exception) {
        long minutes = exception.getMinutesRemaining();
        String timeUnit = exception.getMinutesRemaining() < 60 ? minutes + " minutes" : "1 hour";
        Util.makeToast("Rate limit exceeded, please wait " + timeUnit +
                " or use an access token if you " +
                "haven't supplied one", this);
    }
}