package rumstajn.githubcommitviewer.commit_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.R;
import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.commit_details.CommitDetailsActivity;
import rumstajn.githubcommitviewer.model.api_response.commit.CommitObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommitListActivity extends AppCompatActivity {
    private RecyclerView commitList;
    private CommitListAdapter commitListAdapter;
    private Button backButton;
    private ObjectMapper mapper;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_list);

        mapper = new ObjectMapper();

        initComponents();

        // get commits fetched by previous activity
        Intent intent = getIntent();
        String rawCommits = intent.getStringExtra("commit_objects");
        accessToken = intent.getStringExtra("access_token");
        CommitObject[] commitObjects = new CommitObject[0];
        try {
            commitObjects = mapper.readValue(rawCommits, CommitObject[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Util.makeToast("Failed to load commit data", getApplicationContext());
            finish();
        }
        if (commitObjects.length > 0) {
            loadCommits(Arrays.stream(commitObjects).collect(Collectors.toList()));
        }
    }

    private void initComponents(){
        commitListAdapter = new CommitListAdapter();
        commitListAdapter.setItemClickListener(position -> {
                startDetailsActivityFor(commitListAdapter.getCommitObjetAt(position));
        });

        commitList = findViewById(R.id.commits_recycler_view);
        commitList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commitList.setAdapter(commitListAdapter);
        backButton = findViewById(R.id.commit_list_back_button);
        backButton.setOnClickListener((view) -> finish());
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
            intent.putExtra("access_token", accessToken);
            startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Util.makeToast("Could not show details for that commit", getApplicationContext());
        }
    }
}