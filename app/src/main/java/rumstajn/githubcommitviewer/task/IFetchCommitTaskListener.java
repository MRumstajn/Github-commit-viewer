package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.commit.CommitObject;

public interface IFetchCommitTaskListener {
    void onFetchedCommits(CommitObject[] commitObjects);
    void onFetchCommitsError(Exception e);
}
