package rumstajn.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.exception.RateLimitExceededException;
import rumstajn.githubcommitviewer.model.api_response.CommitObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

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
        boolean usingToken = accessToken != null && accessToken.length() > 0;

        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            if (accessToken != null && accessToken.length() > 0){
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            connection.connect();

            if (connection.getHeaderField("x-ratelimit-remaining").equals("0")){
                long remainingMinutes = Util.getRateLimitExpirationTime(connection);
                listener.onFetchCommitsError(new RateLimitExceededException(remainingMinutes));
                return;
            }

            String response = Util.readHTTPInputStream(connection.getInputStream());
            CommitObject[] commitObjects = mapper.readValue(response, CommitObject[].class);
            listener.onFetchedCommits(commitObjects);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            listener.onFetchCommitsError(e);
        }
    }

    public void setListener(IFetchCommitTaskListener listener) {
        this.listener = listener;
    }
}
