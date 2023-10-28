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

import comp5216.sydney.edu.au.sound2text.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, usernameEditTExt, courseEditText;
    Button registerBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView toLogin;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        usernameEditTExt = findViewById(R.id.username);
        courseEditText = findViewById(R.id.course);

        registerBtn = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        toLogin = findViewById(R.id.toLogin);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, username, course;
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                username = usernameEditTExt.getText().toString();
                course = courseEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registration successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    String userID = user.getUid();
                                    createUserProfile(userID, username, email, course);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("RegisterActivity", "Authentication failed: ", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Registration failed, account already exists.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void createUserProfile(String userID, String username, String email, String course) {
        DocumentReference newUserRef = db.collection("User profile").document(userID);
        Map<String, Object> userData = new HashMap<>();
        ArrayList<String> RecordList = new ArrayList<>();

        userData.put("name", username);
        userData.put("email", email);
        userData.put("Course", course);
        userData.put("Admin",0);
        userData.put("recordsList", RecordList);


        newUserRef.set(userData).addOnSuccessListener(aVoid -> {
            Toast.makeText(RegisterActivity.this, "New profile created.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, "Error creating profile.", Toast.LENGTH_SHORT).show();
        });
    }
}