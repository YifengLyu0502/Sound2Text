package comp5216.sydney.edu.au.sound2text;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    Button logoutBtn;
    Button feedbackBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        logoutBtn = view.findViewById(R.id.Logout);
        feedbackBtn = view.findViewById(R.id.Feedback_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

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
