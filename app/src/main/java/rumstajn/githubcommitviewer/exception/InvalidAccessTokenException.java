package rumstajn.githubcommitviewer.exception;

public class InvalidAccessTokenException extends Exception{

    public InvalidAccessTokenException(){
        super("Invalid personal access token");
    }
}
