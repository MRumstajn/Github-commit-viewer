package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.BlobObject;

public interface IFetchBlobTaskListener {
    void onFetchedBlob(BlobObject blobObject);
    void onFetchBlobError(String msg);
}
