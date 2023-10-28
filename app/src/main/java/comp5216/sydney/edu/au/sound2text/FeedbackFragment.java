package comp5216.sydney.edu.au.sound2text;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackFragment extends Fragment {

    EditText feedbackContent;
    Button submitFeedback;
    final int contentLengthCap = 1000;  // 反馈文本长度上限

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_fragment, container, false);

        feedbackContent = view.findViewById(R.id.feedback_content);
        submitFeedback = view.findViewById(R.id.submit_feedback);

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = feedbackContent.getText().toString().trim();
                if(content.isEmpty()) {
                    // 提示用户输入反馈内容
                    Toast.makeText(getContext(), "Please enter feedback", Toast.LENGTH_SHORT).show();
                } else if(content.length() > contentLengthCap) {
                    // 提示用户缩减反馈内容
                    Toast.makeText(getContext(), "Feedback too long. Please shorten it", Toast.LENGTH_SHORT).show();
                } else {
                    // 将反馈提交到Firebase数据库
                    submitToFirebase(content);
                }
            }
        });

        return view;
    }

    private void submitToFirebase(String content) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("contentLengthCap", contentLengthCap);
        feedback.put("dateSubmitted", FieldValue.serverTimestamp());
        feedback.put("feedbackContent", content);

        db.collection("Feedbacks").add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                        feedbackContent.setText("");  // 清空文本框
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error submitting feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
