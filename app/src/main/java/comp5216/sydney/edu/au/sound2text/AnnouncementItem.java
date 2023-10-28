package comp5216.sydney.edu.au.sound2text;

public class AnnouncementItem {
    private String title;
    private String docID;

    public AnnouncementItem(String title, String docID) {
        this.title = title;
        this.docID = docID;
    }

    public String getTitle() {
        return title;
    }

    public String getDocID() {
        return docID;
    }

    @Override
    public String toString() {
        return title;
    }
}
