package rumstajn.githubcommitviewer.task;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.model.api_response.TreeObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchTreeObjectTask implements Runnable{
    private String url;
    private String accessToken;
    private IFetchTreeObjectTaskListener listener;

    public FetchTreeObjectTask(String url){
        this.url = url;
    }

    public FetchTreeObjectTask(String url, String accessToken){
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
            TreeObject treeObject = mapper.readValue(response, TreeObject.class);
            listener.onFetchedTreeObject(treeObject);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFetchTreeObjectError(e);
        }
    }

    public void setListener(IFetchTreeObjectTaskListener listener) {
        this.listener = listener;
    }
}
