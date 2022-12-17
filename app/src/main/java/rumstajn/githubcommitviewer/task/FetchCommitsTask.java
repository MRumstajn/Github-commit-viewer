package rumstajn.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import rumstajn.githubcommitviewer.model.api_response.CommitObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchCommitsTask implements Runnable {
    private String url;
    private IFetchCommitTaskListener listener;

    public FetchCommitsTask(String url){
        this.url = url;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            CommitObject[] commitObjects = mapper.readValue(new URL(url), CommitObject[].class);
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
