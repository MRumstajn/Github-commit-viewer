package com.mauricio.githubcommitviewer.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.githubcommitviewer.model.api_response.TreeObject;

import java.io.IOException;
import java.net.URL;

public class FetchTreeObjectTask implements Runnable{
    private String url;
    private IFetchTreeObjectTaskListener listener;

    public FetchTreeObjectTask(String url){
        this.url = url;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TreeObject treeObject = mapper.readValue(new URL(url), TreeObject.class);
            listener.onFetchedTreeObject(treeObject);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFetchTreeObjectError(e.getMessage());
        }
    }

    public void setListener(IFetchTreeObjectTaskListener listener) {
        this.listener = listener;
    }
}
