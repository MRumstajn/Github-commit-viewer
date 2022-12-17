package rumstajn.githubcommitviewer.model.api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    private String name;
    private String email;
    private Date date;

    public  Author(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }
}
