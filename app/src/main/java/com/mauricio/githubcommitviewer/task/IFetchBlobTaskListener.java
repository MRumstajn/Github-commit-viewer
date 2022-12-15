package com.mauricio.githubcommitviewer.task;

import com.mauricio.githubcommitviewer.model.api_response.BlobObject;

public interface IFetchBlobTaskListener {
    void onFetchedBlob(BlobObject blobObject);
    void onFetchBlobError(String msg);
}
