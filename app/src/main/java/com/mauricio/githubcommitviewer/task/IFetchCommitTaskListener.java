package com.mauricio.githubcommitviewer.task;

import com.mauricio.githubcommitviewer.model.api_response.CommitObject;

public interface IFetchCommitTaskListener {
    void onFetchedCommits(CommitObject[] commitObjects);
    void onFetchCommitsError(String msg);
}
