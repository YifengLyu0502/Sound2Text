package comp5216.sydney.edu.au.sound2text.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Records的数据模型类   为了对数据库进行读写
 */
public class Records implements Parcelable {
    private String key;
    private String RecordId;

    public Records() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    private String comment;
    private String content;
    private String label;
    private String owner;

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @NonNull
    @Override
    public String toString() {
        return key;
    }


    protected Records(Parcel in) {
        key = in.readString();
        RecordId = in.readString();
        comment = in.readString();
        content = in.readString();
        label = in.readString();
        owner = in.readString();
    }

    public static final Creator<Records> CREATOR = new Creator<Records>() {
        @Override
        public Records createFromParcel(Parcel in) {
            return new Records(in);
        }

        @Override
        public Records[] newArray(int size) {
            return new Records[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(RecordId);
        dest.writeString(comment);
        dest.writeString(content);
        dest.writeString(label);
        dest.writeString(owner);
    }
}
