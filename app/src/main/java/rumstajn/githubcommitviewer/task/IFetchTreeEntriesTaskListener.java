package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.tree.TreeEntry;

public interface IFetchTreeEntriesTaskListener {
    void onFetchTreeEntry(TreeEntry treeEntry);
    void onFetchTreeEntryError(Exception e);
}
