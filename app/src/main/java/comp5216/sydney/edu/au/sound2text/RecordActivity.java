package comp5216.sydney.edu.au.sound2text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {

    TextView textView1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);

        String label = getIntent().getStringExtra("label");
        String id = getIntent().getStringExtra("id");
        String text = getIntent().getStringExtra("Text");
        String comments = getIntent().getStringExtra("comments");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("records").document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        textView1.setText("Text:"+documentSnapshot.get("Text"));
                        textView2.setText("comments:"+documentSnapshot.get("comments"));
                        textView3.setText("label:"+documentSnapshot.get("label"));
                    }
                });





        MyDialogFragment dialog = new MyDialogFragment(this,id,label,text,comments);
        findViewById(R.id.button).setOnClickListener(view -> {
            dialog.show();
        });
        dialog.setOnDismissListener(dialogInterface -> {
            notifyView();
        });
    }

    private void notifyView() {
        String id = getIntent().getStringExtra("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("records").document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String str3 = (String) documentSnapshot.get("label");
                        textView3.setText("label:"+str3);
                    }
                });
    }
}