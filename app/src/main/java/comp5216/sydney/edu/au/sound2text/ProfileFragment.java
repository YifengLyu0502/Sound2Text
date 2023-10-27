//package comp5216.sydney.edu.au.sound2text;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.ArrayList;
//
//public class ProfileFragment extends Fragment {
//    Button logoutBtn;
//    public ArrayList<String> items;
//    ListView announcementList;
//    public ArrayAdapter<String> adapter;
//    @SuppressLint("MissingInflatedId")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.profile_fragment, container, false);
//        logoutBtn = view.findViewById(R.id.Logout);
//        announcementList = view.findViewById(R.id.AnnouncementList);
//        items = new ArrayList<>();
//
//        // Initialize adapter
//        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, items);
//
//        //读取数据库里的announcements，替换掉下面的Item 1,Item 2 ...
//        items.add("Item 1");
//        items.add("Item 2");
//
//        // Set adapter to the ListView
//        announcementList.setAdapter(adapter);
//
//        //items的点击器，实现界面跳转
//        announcementList.setOnItemClickListener((parent, v, position, id) -> {
//            String selectedAnnouncement = items.get(position);
//
//            Intent intent = new Intent(getActivity(), Announcement_Detail.class);
//            intent.putExtra("selectedAnnouncement", selectedAnnouncement);
//            startActivity(intent);
//        });
//
//
//
//
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        return view;
//    }
//
//    // You can add other lifecycle methods if needed.
//}
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    Button logoutBtn;
    public ArrayList<String> items;
    ListView announcementList;
    public ArrayAdapter<String> adapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView userInfoTextView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        logoutBtn = view.findViewById(R.id.Logout);
        announcementList = view.findViewById(R.id.AnnouncementList);
        userInfoTextView = view.findViewById(R.id.userInfoTextView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        items = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, items);

        //读取数据库里的announcements，替换掉下面的Item 1,Item 2 ...
        items.add("Ann 1");
        items.add("Ann 2");

        // Set adapter to the ListView
        announcementList.setAdapter(adapter);

        //items的点击器，实现界面跳转
        announcementList.setOnItemClickListener((parent, v, position, id) -> {
            String selectedAnnouncement = items.get(position);

            Intent intent = new Intent(getActivity(), Announcement_Detail.class);
            intent.putExtra("selectedAnnouncement", selectedAnnouncement);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        loadUserInfo();

        return view;
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DocumentReference userRef = db.collection("User profile").document(userID);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("name");
                    String course = documentSnapshot.getString("Course");
                    userInfoTextView.setText(String.format("Username: %s\nCourse: %s", username, course));
                } else {
                    Toast.makeText(getActivity(), "User data not found.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Error loading user data.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // You can add other lifecycle methods if needed.
}
