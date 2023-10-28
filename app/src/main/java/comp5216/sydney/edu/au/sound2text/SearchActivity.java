package comp5216.sydney.edu.au.sound2text;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    EditText ed_input;

    ArrayList<ModeBean> modeBeanArrayList = new ArrayList<>();

    RecyclerView recyclerView;

    ArrayList<ModeBean> current = new ArrayList<>();
    MyAdapter adapter;
    int num = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ed_input = findViewById(R.id.ed_input);
        recyclerView = findViewById(R.id.recycler);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);


        ed_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.isEmpty()) {
                    adapter.bindData(current);
                }
                ArrayList<ModeBean> list = new ArrayList<>();

                for (ModeBean modeBean : current) {
                    if (modeBean.id.contains(s)) {
                        list.add(modeBean);
                    }
                }
                adapter.bindData(list);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化 Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef = db.collection("User profile");

        Handler handler = new Handler(message -> {
            if (message.what == 0) {
                current.clear();
                current.addAll(modeBeanArrayList);
                adapter.bindData(current);
            }
            return true;
        });

        docRef.whereEqualTo("email", "123456@gmail.com")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    modeBeanArrayList.clear();
                    for (QueryDocumentSnapshot userDocument : queryDocumentSnapshots) {
                        // 获取用户文档中的 "recordsList" 数组
                        try {
                            List<Object> recordsList = (List<Object>) userDocument.get("recordsList");

                            if (recordsList != null) {
                                // 迭代 recordsList 并处理其中的值
                                num = recordsList.size();
                                for (Object item : recordsList) {
                                    System.out.println("Value: " + item.toString());
                                    db.collection("records").document(item.toString())
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    String str1 = (String) documentSnapshot.get("Text");
                                                    String str2 = (String) documentSnapshot.get("comments");
                                                    String str3 = (String) documentSnapshot.get("label");

                                                    modeBeanArrayList.add(new ModeBean(str1, str2, str3, item.toString()));
                                                    num = num - 1;
                                                    handler.sendEmptyMessage(num);
                                                }
                                            });

                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                })
                .addOnFailureListener(e -> {
                    // 处理错误
                    System.out.println("Error: " + e.getMessage());
                });
    }
}
