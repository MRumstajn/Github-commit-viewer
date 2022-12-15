package com.mauricio.githubcommitviewer.model.api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeObject {
    private String sha;
    private String url;
    private List<TreeEntry> tree;

    public TreeObject(){}

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTree(List<TreeEntry> tree) {
        this.tree = tree;
    }

    public String getSha() {
        return sha;
    }

    public String getUrl() {
        return url;
    }

    public List<TreeEntry> getTree() {
        return tree;
    }
}
