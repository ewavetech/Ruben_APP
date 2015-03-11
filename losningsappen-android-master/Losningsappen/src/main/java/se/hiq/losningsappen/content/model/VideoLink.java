package se.hiq.losningsappen.content.model;

import java.io.Serializable;

/**
 * Created by Naknut on 30/07/14.
 */
public class VideoLink implements Serializable {
    private String title;
    private String url;
    public VideoLink(String title, String url){
        this.title=title;
        this.url=url;
    }
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
