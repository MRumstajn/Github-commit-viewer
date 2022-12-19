package rumstajn.githubcommitviewer.model.api_response.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitObject {
    private String sha;
    private Commit commit;

    public CommitObject(){}

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getSha() {
        return sha;
    }

    public Commit getCommit() {
        return commit;
    }
}
