package rumstajn.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import rumstajn.githubcommitviewer.model.api_response.BlobObject;

import java.io.IOException;
import java.net.URL;

public class FetchBlobTask implements Runnable{
    private String url;
    private IFetchBlobTaskListener listener;

    public FetchBlobTask(String url){
        this.url = url;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            BlobObject blobObject = mapper.readValue(new URL(url), BlobObject.class);
            listener.onFetchedBlob(blobObject);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFetchBlobError(e.getMessage());
        }
    }

    public void setListener(IFetchBlobTaskListener listener) {
        this.listener = listener;
    }
}
