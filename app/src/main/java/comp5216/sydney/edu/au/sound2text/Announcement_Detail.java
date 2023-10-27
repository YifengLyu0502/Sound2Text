package comp5216.sydney.edu.au.sound2text;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
public class Announcement_Detail extends Activity {
    TextView announcementTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_detail);  //确保你有这个布局文件

        announcementTextView = findViewById(R.id.announcementDetailText);  //确保你的announcement_detail_layout.xml布局文件中有此ID的TextView

        String announcement = getIntent().getStringExtra("selectedAnnouncement");
        if (announcement != null) {
            announcementTextView.setText(announcement);
        }
    }

    //oncreate function

        //显示announcement的文字内容

}
