package rumstajn.githubcommitviewer;

public class GlobalConfig {
    public static final String API_BASE_URL = "https://api.github.com";
    public static final String API_REPOS_ROUTE = "/repos";
    public static final String API_COMMITS_ROUTE = "/commits";
    public static final int EXECUTOR_SERVICE_THREAD_COUNT = 4;
    public static final int RATE_LIMIT_REQUESTS = 60;
    public static final int RATE_LIMIT_REQUESTS_TOKEN = 1000;
}
