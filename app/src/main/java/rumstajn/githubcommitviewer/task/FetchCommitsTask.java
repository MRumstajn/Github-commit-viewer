package rumstajn.githubcommitviewer.task;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.model.api_response.CommitObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchCommitsTask implements Runnable {
    private String url;
    private String accessToken;
    private IFetchCommitTaskListener listener;

    public FetchCommitsTask(String url){
        this.url = url;
    }

    public FetchCommitsTask(String url, String accessToken){
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
            CommitObject[] commitObjects = mapper.readValue(response, CommitObject[].class);
            listener.onFetchedCommits(commitObjects);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFetchCommitsError(e.getMessage());
        }
    }

    public void setListener(IFetchCommitTaskListener listener) {
        this.listener = listener;
    }
}
