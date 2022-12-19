package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.model.api_response.blob.BlobObject;

public interface IFetchBlobTaskListener {
    void onFetchedBlob(BlobObject blobObject);
    void onFetchBlobError(Exception e);
}
