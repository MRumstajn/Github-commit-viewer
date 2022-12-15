package com.mauricio.githubcommitviewer.task;

import com.mauricio.githubcommitviewer.model.api_response.TreeObject;

public interface IFetchTreeObjectTaskListener {
    void onFetchedTreeObject(TreeObject treeObject);
    void onFetchTreeObjectError(String msg);
}
