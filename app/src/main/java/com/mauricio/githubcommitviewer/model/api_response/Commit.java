package com.mauricio.githubcommitviewer.model.api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
    private String message;
    private Author author;
    private Tree tree;
    private Verification verification;

    public Commit(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public String getMessage() {
        return message;
    }

    public Author getAuthor() {
        return author;
    }

    public Tree getTree() {
        return tree;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public Verification getVerification() {
        return verification;
    }
}
