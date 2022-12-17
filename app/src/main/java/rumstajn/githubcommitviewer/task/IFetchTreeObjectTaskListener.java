package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.TreeObject;

public interface IFetchTreeObjectTaskListener {
    void onFetchedTreeObject(TreeObject treeObject);
    void onFetchTreeObjectError(Exception e);
}
