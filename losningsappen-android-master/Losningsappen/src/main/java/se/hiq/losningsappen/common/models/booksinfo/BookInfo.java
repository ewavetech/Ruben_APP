package se.hiq.losningsappen.common.models.booksinfo;

/**
 * Created by Naknut on 03/08/14.
 */
public class BookInfo {

    private String title;
    private String description;
    private ChapterTestFile chapterTestFile;
    private ContentFile contentFile;
    private String color;
    private String bookPath;
    private String productIdentifier;
    private boolean isFreeBook;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChapterTestFile getChapterTestFile() {
        return chapterTestFile;
    }

    public ContentFile getContentFile() {
        return contentFile;
    }

    public String getColor() {
        return color;
    }

    public String getBookPath() {
        return bookPath;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public boolean isFreeBook() {
        return isFreeBook;
    }
}
