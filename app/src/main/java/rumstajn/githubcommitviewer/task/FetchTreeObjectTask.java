package rumstajn.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.exception.RateLimitExceededException;
import rumstajn.githubcommitviewer.model.api_response.TreeObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

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
        boolean usingToken = accessToken != null && accessToken.length() > 0;

        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            if (usingToken){
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            connection.connect();

            if (connection.getHeaderField("x-ratelimit-remaining").equals("0")){
                long remainingMinutes = Util.getRateLimitExpirationTime(connection);
                listener.onFetchTreeObjectError(new RateLimitExceededException(remainingMinutes));
                return;
            }

            String response = Util.readHTTPInputStream(connection.getInputStream());
            TreeObject treeObject = mapper.readValue(response, TreeObject.class);
            listener.onFetchedTreeObject(treeObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            listener.onFetchTreeObjectError(e);
        }
    }

    public void setListener(IFetchTreeObjectTaskListener listener) {
        this.listener = listener;
    }
}
