package com.mauricio.githubcommitviewer.commit_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;
import com.mauricio.githubcommitviewer.Util;
import com.mauricio.githubcommitviewer.commit_details.CommitDetailsActivity;
import com.mauricio.githubcommitviewer.model.api_response.Commit;
import com.mauricio.githubcommitviewer.model.api_response.CommitObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommitListActivity extends AppCompatActivity {
    private RecyclerView commitList;
    private CommitListAdapter commitListAdapter;
    private ObjectMapper mapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_list);

        mapper = new ObjectMapper();

        initComponents();

        // get commits fetched by previous activity
        Intent intent = getIntent();
        String rawCommits = intent.getStringExtra("commit_objects");
        CommitObject[] commitObjects = new CommitObject[0];
        try {
            commitObjects = mapper.readValue(rawCommits, CommitObject[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Util.makeToast("Failed to load commit data", getApplicationContext());
            finish();
        }
        loadCommits(Arrays.stream(commitObjects).collect(Collectors.toList()));
    }

    private void initComponents(){
        commitListAdapter = new CommitListAdapter();
        commitListAdapter.setItemClickListener(position -> {
                startDetailsActivityFor(commitListAdapter.getCommitObjetAt(position));
        });

        commitList = findViewById(R.id.commits_recycler_view);
        commitList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commitList.setAdapter(commitListAdapter);
    }

    private void loadCommits(List<CommitObject> commitObjs){
        if (commitObjs.size() > 0) {
            commitListAdapter.setCommitObjects(commitObjs);
        } else {
            Util.makeToast("No commits in this repository", getApplicationContext());
        }
    }

    private void startDetailsActivityFor(CommitObject commitObject){
        try {
            Intent intent = new Intent(this, CommitDetailsActivity.class);
            intent.putExtra("commit_obj", mapper.writeValueAsString(commitObject));
            startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Util.makeToast("Could not show details for that commit", getApplicationContext());
        }
    }
}