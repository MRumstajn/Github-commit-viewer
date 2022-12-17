package rumstajn.githubcommitviewer.model.api_response;

public enum BlobEncoding {
    BASE64("base64");

    private final String name;

    BlobEncoding(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
