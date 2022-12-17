package rumstajn.githubcommitviewer.task;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.model.api_response.BlobObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchBlobTask implements Runnable{
    private String url;
    private String accessToken;
    private IFetchBlobTaskListener listener;

    public FetchBlobTask(String url){
        this.url = url;
    }

    public FetchBlobTask(String url, String accessToken){
        this(url);
        this.accessToken = accessToken;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            if (accessToken != null && accessToken.length() > 0){
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            connection.connect();
            String response = Util.readHTTPInputStream(connection.getInputStream());
            BlobObject blobObject = mapper.readValue(response, BlobObject.class);
            listener.onFetchedBlob(blobObject);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFetchBlobError(e);
        }
    }

    public void setListener(IFetchBlobTaskListener listener) {
        this.listener = listener;
    }
}
