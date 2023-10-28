package comp5216.sydney.edu.au.sound2text;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import comp5216.sydney.edu.au.sound2text.bean.Records;
public class RecordsFragment extends Fragment {

    EditText ed_input;

    ArrayList<ModeBean> modeBeanArrayList = new ArrayList<>();
    ListView RecordsList;
    private ArrayList<Records> items;
    private ArrayAdapter<Records> adapter;
    RecyclerView recyclerView;

    ArrayList<ModeBean> current = new ArrayList<>();
    MyAdapter adapter1;
    Button button;
    int num = 0;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        ed_input = view.findViewById(R.id.ed_input);
        recyclerView = view.findViewById(R.id.recycler);
        adapter1 = new MyAdapter();
        recyclerView.setAdapter(adapter1);
        RecordsList = view.findViewById(R.id.Records_List);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(view1 -> {
            String s = ed_input.getText().toString();
            if (s.isEmpty()) {
                adapter1.bindData(current);
            }
            ArrayList<ModeBean> list = new ArrayList<>();

            for (ModeBean modeBean : current) {
                if (modeBean.id.contains(s)||modeBean.label.contains(s)) {
                    list.add(modeBean);
                }
            }
            adapter1.bindData(list);
        });



// Initialize data
        items = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, items);
        // 获取数据库的实例
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        // 获取records集合
        instance.collection("records")
                .whereEqualTo("owner", FirebaseAuth.getInstance().getUid())
                .get()
                //对任务进行监听 判断成功或者失败
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    // 成功后对数据库的数据进行解析
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        items.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Records records = document.toObject(Records.class);
                            // 对id进行获取
                            records.setKey(document.getId());
                            items.add(records);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    // 监听失败将进行提示
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        //String receivedString = getArguments().getString("key_string");
        //连接数据库进行读写

        // Set adapter to the ListView
        RecordsList.setAdapter(adapter);
        RecordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //对列表进行点击的时候进行监听，当点数据的时候，进行也面跳转 并传递数据
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(requireContext(),TranslateActivity.class).putExtra("records", items.get(position)));
            }
        });
        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        // 初始化 Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef = db.collection("User profile");

        Handler handler = new Handler(message -> {
            if (message.what == 0) {
                current.clear();
                current.addAll(modeBeanArrayList);
                adapter1.bindData(current);
            }
            return true;
        });

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        docRef.whereEqualTo("email", email)
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
                            Log.d("TAG", "onResume: "+e);
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    // 处理错误
                    System.out.println("Error: " + e.getMessage());
                });
    }





}
