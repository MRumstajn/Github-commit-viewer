package com.mauricio.githubcommitviewer.commit_details;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;
import com.mauricio.githubcommitviewer.Util;
import com.mauricio.githubcommitviewer.model.api_response.BlobEncoding;
import com.mauricio.githubcommitviewer.model.api_response.BlobObject;
import com.mauricio.githubcommitviewer.model.api_response.Commit;
import com.mauricio.githubcommitviewer.model.api_response.CommitObject;
import com.mauricio.githubcommitviewer.model.api_response.TreeEntry;
import com.mauricio.githubcommitviewer.model.api_response.TreeEntryType;
import com.mauricio.githubcommitviewer.model.api_response.TreeObject;
import com.mauricio.githubcommitviewer.task.FetchBlobTask;
import com.mauricio.githubcommitviewer.task.FetchTreeObjectTask;
import com.mauricio.githubcommitviewer.task.IFetchBlobTaskListener;
import com.mauricio.githubcommitviewer.task.IFetchTreeObjectTaskListener;
import com.mauricio.githubcommitviewer.task.TaskManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommitDetailsActivity extends AppCompatActivity
        implements IFetchTreeObjectTaskListener, AdapterView.OnItemSelectedListener {
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
            Util.makeToast("Could not display details for this commit", getApplicationContext());
            exitActivity();
            return;
        }

        setAttributeLabels(commitObj);
        loadTreeObj(commitObj);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        mapper = new ObjectMapper();
        commitFiles = new ArrayList<>();

        backButton = findViewById(R.id.back_button);
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

    private void loadTreeObj(CommitObject commitObject){
        FetchTreeObjectTask task = new FetchTreeObjectTask(commitObject.getCommit().getTree().getUrl());
        task.setListener(this);
        TaskManager.getInstance().runTaskLater(task);
    }

    /*
    * commit (tree url) -> tree_obj (entries)(entry_url) -> entry (block/tree url)
    *
    * */

    private void loadTree(TreeEntry treeEntry) {
        FetchTreeObjectTask task = new FetchTreeObjectTask(treeEntry.getUrl());
        task.setListener(this);
        TaskManager.getInstance().runTaskLater(task);
    }

    private void loadBlob(TreeEntry treeEntry) {
        String url = treeEntry.getUrl();
        FetchBlobTask task = new FetchBlobTask(url);
        task.setListener(new IFetchBlobTaskListener() {
            @Override
            public void onFetchedBlob(BlobObject blobObject) {
                String encodedContent = blobObject.getContent();
                String content = null;
                if (blobObject.getEncoding().equals(BlobEncoding.BASE64.getName())){
                    byte[] contentBytes = Base64.decode(encodedContent, Base64.DEFAULT);
                    content = new String(contentBytes);
                }
                if (content == null){
                    Util.makeToast("Encountered an unsupported file encoding \""
                            + blobObject.getEncoding() + "\"", getApplicationContext());
                    return;
                }
                CommitFile commitFile = new CommitFile(content, treeEntry.getPath());
                runOnUiThread(() -> {
                    commitFiles.add(commitFile);
                    fileSpinnerAdapter.add(commitFile.getPath());
                });
            }

            @Override
            public void onFetchBlobError(String msg) {
                runOnUiThread(() -> {
                    Util.makeToast("Failed to fetch some files", getApplicationContext());
                });
            }
        });
        TaskManager.getInstance().runTaskLater(task);
    }

    private void exitActivity() {
        finish();
    }

    @Override
    public void onFetchedTreeObject(TreeObject treeObject) {
        treeObject.getTree().forEach(entry -> {
            if (entry.getType().equals(TreeEntryType.BLOB.getName())){
                loadBlob(entry);
            } else if (entry.getType().equals(TreeEntryType.TREE.getName())){
                loadTree(entry);
            }
        });
    }

    @Override
    public void onFetchTreeObjectError(String msg) {
        runOnUiThread(() -> {
            Util.makeToast("Failed to fetch files", getApplicationContext());
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        fileContentDisplay.setText(commitFiles.get(i).getContent());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        fileContentDisplay.setText(null);
    }
}