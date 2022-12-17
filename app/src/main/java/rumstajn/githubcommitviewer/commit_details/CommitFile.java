package rumstajn.githubcommitviewer.commit_details;

public class CommitFile {
    private String content;
    private String path;

    public CommitFile(String content, String path){
        this.content = content;
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }
}
