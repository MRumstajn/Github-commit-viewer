package com.mauricio.githubcommitviewer.commit_details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;
import com.mauricio.githubcommitviewer.Util;
import com.mauricio.githubcommitviewer.model.api_response.Commit;
import com.mauricio.githubcommitviewer.model.api_response.CommitObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

public class CommitDetailsActivity extends AppCompatActivity {
    private Button backButton;
    private TextView authorLabel;
    private TextView dateLabel;
    private TextView messageLabel;
    private TextView verifiedLabel;
    private ObjectMapper mapper;

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
    }

    private void initComponents(){
        mapper = new ObjectMapper();

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener((view) -> {
            exitActivity();
        });
        authorLabel = findViewById(R.id.author_label);
        dateLabel = findViewById(R.id.date_label);
        messageLabel = findViewById(R.id.message_label);
        verifiedLabel = findViewById(R.id.verified_label);
    }

    private void setAttributeLabels(CommitObject commitObject){
        Commit commit = commitObject.getCommit();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

        authorLabel.setText(commit.getAuthor().getName());
        dateLabel.setText(dateFormat.format(commit.getAuthor().getDate()));
        messageLabel.setText(commit.getMessage());
        verifiedLabel.setText(Boolean.toString(commit.getVerification().isVerified()));
    }

    private void exitActivity(){
        finish();
    }
}