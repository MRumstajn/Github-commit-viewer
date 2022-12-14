package rumstajn.githubcommitviewer.model.api_response.tree;

public enum TreeEntryType {
    BLOB("blob"),
    TREE("tree");

    private final String name;

    TreeEntryType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
