package rumstajn.githubcommitviewer.exception;

public class RateLimitExceededException extends Exception{
    private long minutesRemaining;

    public RateLimitExceededException(){
        super("Rate limit has been exceeded!");
    }

    public RateLimitExceededException(String msg){
        super(msg);
    }

    public RateLimitExceededException(long minutesRemaining){
        this.minutesRemaining = minutesRemaining;
    }

    public long getMinutesRemaining() {
        return minutesRemaining;
    }

    public void setMinutesRemaining(long minutesRemaining) {
        this.minutesRemaining = minutesRemaining;
    }
}
