package se.hiq.losningsappen.content.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naknut on 30/07/14.
 */
public class Chapter implements Serializable {
    private String meta;
    private String name;
    private int number;
    private List<SubChapter> subChapters;

    public String getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public List<SubChapter> getSubChapters() {
        return subChapters;
    }

    public SubChapter getSubChapter(int position) {
        return subChapters.get(position);
    }
}
