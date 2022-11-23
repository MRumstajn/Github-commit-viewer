package com.mauricio.githubcommitviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText repoNameField = findViewById(R.id.github_repo_name_field);
        EditText repoOwnerField = findViewById(R.id.github_repo_owner_field);
        Button repoLookupButton = findViewById(R.id.repo_lookup_button);

        repoLookupButton.setOnClickListener((view) -> {
            // validate data
            String repoName = repoNameField.getText().toString();
            String repoOwner = repoOwnerField.getText().toString();
            if (!repoName.isEmpty() && !repoOwner.isEmpty()){
                startActivity(new Intent(this, CommitListActivity.class));
            } else {
                Util.makeToast("Repository name and owner fields are required", getApplicationContext());
            }
        });
    }
}