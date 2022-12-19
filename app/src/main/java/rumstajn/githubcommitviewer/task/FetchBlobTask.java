package rumstajn.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.exception.RateLimitExceededException;
import rumstajn.githubcommitviewer.model.api_response.BlobObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

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
                listener.onFetchBlobError(new RateLimitExceededException(remainingMinutes));
                return;
            }

            String response = Util.readHTTPInputStream(connection.getInputStream());
            BlobObject blobObject = mapper.readValue(response, BlobObject.class);
            listener.onFetchedBlob(blobObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            listener.onFetchBlobError(e);
        }
    }

    public void setListener(IFetchBlobTaskListener listener) {
        this.listener = listener;
    }
}
