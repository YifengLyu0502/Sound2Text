package comp5216.sydney.edu.au.sound2text;

public class ModeBean {
    String text;
    String comments;
    String label;
    String id;


    public ModeBean(String text, String comments, String label, String id) {
        this.text = text;
        this.comments = comments;
        this.label = label;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
