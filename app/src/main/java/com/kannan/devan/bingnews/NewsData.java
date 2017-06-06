package com.kannan.devan.bingnews;

/**
 * Created by devan on 8/4/17.
 */

public class NewsData {
    public String getJsonData() {
        return JsonData;
    }

    public void setJsonData(String jsonData) {
        JsonData = jsonData;
    }

    String JsonData;

    public int getPageId() {
        return PageId;
    }

    public void setPageId(int pageId) {
        PageId = pageId;
    }

    int PageId;
}
