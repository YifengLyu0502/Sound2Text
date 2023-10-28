package comp5216.sydney.edu.au.sound2text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class New_announcement extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button submitButton, cancelButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_announcement);

        titleEditText = findViewById(R.id.announcementTitle);
        contentEditText = findViewById(R.id.announcementContent);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> announcementData = new HashMap<>();
            announcementData.put("Title", title);
            announcementData.put("Content", content);
            announcementData.put("PostedBy", mAuth.getCurrentUser().getUid());

            db.collection("Announcements").add(announcementData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Publish successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Return to the previous activity.
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error publishing the announcement", Toast.LENGTH_SHORT).show();
                    });
        });

        cancelButton.setOnClickListener(view -> finish());
    }
}
