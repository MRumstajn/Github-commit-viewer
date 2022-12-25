package rumstajn.githubcommitviewer.task;

import android.hardware.lights.LightState;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import rumstajn.githubcommitviewer.Util;
import rumstajn.githubcommitviewer.exception.InvalidAccessTokenException;
import rumstajn.githubcommitviewer.exception.RateLimitExceededException;
import rumstajn.githubcommitviewer.model.api_response.blob.BlobObject;
import rumstajn.githubcommitviewer.model.api_response.tree.TreeEntry;

public class FetchTreeEntriesTask implements Runnable{
    private List<TreeEntry> treeEntries;
    private String accessToken;
    private boolean loadingEntries;

    public FetchTreeEntriesTask( String accessToken, List<TreeEntry> treeEntries){
        this.accessToken = accessToken;
        this.treeEntries = treeEntries;
    }

    @Override
    public void run() {
        treeEntries.forEach(treeEntry -> {
            if (!loadingEntries){
                return;
            }
            boolean usingToken = accessToken != null && accessToken.length() > 0;

            ObjectMapper mapper = new ObjectMapper();
            try {
                String url = treeEntry.getUrl();
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                if (usingToken){
                    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                }
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                    //listener.onFetchBlobError(new InvalidAccessTokenException());
                    return;
                }

                if (connection.getHeaderField("x-ratelimit-remaining").equals("0")){
                    long remainingMinutes = Util.getRateLimitExpirationTime(connection);
                    //listener.onFetchBlobError(new RateLimitExceededException(remainingMinutes));
                    return;
                }

                String response = Util.readHTTPInputStream(connection.getInputStream());
                TreeEntry blobObject = mapper.readValue(response, TreeEntry.class);
                //listener.onFetchedBlob(blobObject);

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                //listener.onFetchBlobError(e);
            }
        });
    }

    public void setLoadingEntries(boolean loadingEntries) {
        this.loadingEntries = loadingEntries;
    }

    public boolean isLoadingEntries() {
        return loadingEntries;
    }
}
