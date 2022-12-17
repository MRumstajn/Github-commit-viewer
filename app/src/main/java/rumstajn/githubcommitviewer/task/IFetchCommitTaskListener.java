package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.CommitObject;

public interface IFetchCommitTaskListener {
    void onFetchedCommits(CommitObject[] commitObjects);
    void onFetchCommitsError(String msg);
}
