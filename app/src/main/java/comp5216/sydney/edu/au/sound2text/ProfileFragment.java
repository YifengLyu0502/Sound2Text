package comp5216.sydney.edu.au.sound2text;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    Button logoutBtn;
    Button feedbackBtn;
    public ArrayList<String> items;
    ListView announcementList;
    public ArrayAdapter<String> adapter;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        logoutBtn = view.findViewById(R.id.Logout);
        feedbackBtn = view.findViewById(R.id.Feedback_btn);
        announcementList = view.findViewById(R.id.AnnouncementList);
        items = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, items);

        //读取数据库里的announcements，替换掉下面的Item 1,Item 2 ...
        items.add("Item 1");
        items.add("Item 2");

        // Set adapter to the ListView
        announcementList.setAdapter(adapter);

        //items的点击器，实现界面跳转





        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // feedback 按钮点击事件
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换到提交反馈的界面
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, feedbackFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }

    // You can add other lifecycle methods if needed.
}
