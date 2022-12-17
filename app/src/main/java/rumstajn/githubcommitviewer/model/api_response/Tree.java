package rumstajn.githubcommitviewer.model.api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tree {
    private String url;
    private String sha;

    public  Tree(){}

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public String getSha() {
        return sha;
    }
}
