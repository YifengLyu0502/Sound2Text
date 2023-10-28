package comp5216.sydney.edu.au.sound2text;

public class Record {
    private String text;
    private String comments;
    private String label;

    public Record() {
        // 默认构造函数（必需）
    }

    public Record(String text, String comments, String label) {
        this.text = text;
        this.comments = comments;
        this.label = label;
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