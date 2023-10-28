package comp5216.sydney.edu.au.sound2text;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class Announcement_list extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<AnnouncementItem> itemsAdapter;
    private ArrayList<AnnouncementItem> announcementItems = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_list);

        listView = findViewById(R.id.listView);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, announcementItems);
        listView.setAdapter(itemsAdapter);

        loadDataFromFirebase();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AnnouncementItem selectedItem = announcementItems.get(position);
            Intent intent = new Intent(Announcement_list.this, Announcement_Detail.class);
            intent.putExtra("selectedDocID", selectedItem.getDocID());
            startActivity(intent);
        });
    }

    private void loadDataFromFirebase() {
        db.collection("Announcements").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("Title");
                    if (title != null) {
                        AnnouncementItem item = new AnnouncementItem(title, document.getId());
                        announcementItems.add(item);
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Announcement_list.this, "Error loading data from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
