package com.mauricio.githubcommitviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.commit_list.CommitListActivity;
import com.mauricio.githubcommitviewer.model.api_response.CommitObject;
import com.mauricio.githubcommitviewer.task.FetchCommitsTask;
import com.mauricio.githubcommitviewer.task.IFetchCommitTaskListener;
import com.mauricio.githubcommitviewer.task.TaskManager;

public class MainActivity extends AppCompatActivity implements IFetchCommitTaskListener {
    private ObjectMapper mapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapper = new ObjectMapper();

        EditText repoNameField = findViewById(R.id.github_repo_name_field);
        EditText repoOwnerField = findViewById(R.id.github_repo_owner_field);
        Button repoLookupButton = findViewById(R.id.repo_lookup_button);

        repoLookupButton.setOnClickListener((view) -> {
            // validate data
            String repoName = repoNameField.getText().toString().trim();
            String repoOwner = repoOwnerField.getText().toString().trim();
            if (!repoName.isEmpty() && !repoOwner.isEmpty()){
                String url = GlobalConfig.API_BASE_URL + GlobalConfig.API_REPOS_ROUTE +
                        "/" + repoOwner + "/" + repoName + GlobalConfig.API_COMMITS_ROUTE;
                FetchCommitsTask fetchTask = new FetchCommitsTask(url);
                fetchTask.setListener(this);

                TaskManager.getInstance().runTaskLater(fetchTask);
            } else {
                Util.makeToast("Repository name and owner fields are required", getApplicationContext());
            }
        });
    }

    @Override
    public void onFetchedCommits(CommitObject[] commitObjects) {
        try {
            Intent intent = new Intent(this, CommitListActivity.class);
            intent.putExtra("commit_objects",
                    mapper.writeValueAsString(commitObjects));
            startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Util.makeToast("Error occurred while relaying data to the next activity",
                        getApplicationContext());
            });
        }
    }

    @Override
    public void onFetchCommitsError(String msg) {
        runOnUiThread(() -> {
            Util.makeToast("Error fetching commits", getApplicationContext());
        });
    }
}