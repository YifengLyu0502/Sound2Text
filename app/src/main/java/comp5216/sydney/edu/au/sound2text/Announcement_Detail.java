package comp5216.sydney.edu.au.sound2text;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;

public class Announcement_Detail extends Activity {
    TextView announcementTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_detail);

        announcementTextView = findViewById(R.id.announcementDetailText);

        String selectedDocID = getIntent().getStringExtra("selectedDocID");
        if (selectedDocID != null) {
            db.collection("Announcements").document(selectedDocID).get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            String title = document.getString("Title");
                            String content = document.getString("Content");
                            if (title != null && content != null) {
                                announcementTextView.setText(title + "\n\n" + content);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Announcement_Detail.this, "Error retrieving document from Firebase.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
