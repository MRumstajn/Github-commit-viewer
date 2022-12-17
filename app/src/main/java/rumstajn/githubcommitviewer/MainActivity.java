package rumstajn.githubcommitviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;

import java.io.FileNotFoundException;

import rumstajn.githubcommitviewer.commit_list.CommitListActivity;
import rumstajn.githubcommitviewer.model.api_response.CommitObject;
import rumstajn.githubcommitviewer.task.FetchCommitsTask;
import rumstajn.githubcommitviewer.task.IFetchCommitTaskListener;
import rumstajn.githubcommitviewer.task.TaskManager;

public class MainActivity extends AppCompatActivity implements IFetchCommitTaskListener {
    private ObjectMapper mapper;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapper = new ObjectMapper();

        EditText repoNameField = findViewById(R.id.github_repo_name_field);
        EditText repoOwnerField = findViewById(R.id.github_repo_owner_field);
        EditText accessTokenField = findViewById(R.id.github_personal_access_token_field);
        Button repoLookupButton = findViewById(R.id.repo_lookup_button);

        repoLookupButton.setOnClickListener((view) -> {
            // validate data
            String repoName = repoNameField.getText().toString().trim();
            String repoOwner = repoOwnerField.getText().toString().trim();
            String accessToken = accessTokenField.getText().toString().trim();
            this.accessToken = accessToken.length() > 0 ? accessToken : "";
            if (!repoName.isEmpty() && !repoOwner.isEmpty()){
                String url = GlobalConfig.API_BASE_URL + GlobalConfig.API_REPOS_ROUTE +
                        "/" + repoOwner + "/" + repoName + GlobalConfig.API_COMMITS_ROUTE;
                FetchCommitsTask fetchTask = new FetchCommitsTask(url, accessToken);
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
            intent.putExtra("access_token", accessToken);
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
    public void onFetchCommitsError(Exception e) {
        runOnUiThread(() -> {
            if (e instanceof FileNotFoundException){
                Util.makeToast("Repository not found or rate limit reached", getApplicationContext());
            } else {
                Util.makeToast("Error fetching commits", getApplicationContext());
            }
        });
    }
}