package comp5216.sydney.edu.au.sound2text;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyDialogFragment extends AlertDialog {

    private EditText inputEditText;
    private String id;
    private String label;
    private String text;
    private String comments;

    protected MyDialogFragment(Context context,String id,String label,String text,String comments) {
        super(context);
        this.id = id;
        this.label = label;
        this.text = text;
        this.comments = comments;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        setView(dialogView);

        inputEditText = dialogView.findViewById(R.id.editText);

        setTitle("输入框对话框");

        setButton(BUTTON_POSITIVE, "确定", (dialog, which) -> {
            // 处理确定按钮点击事件
            String userInput = inputEditText.getText().toString();
            change(userInput);
            // 在这里处理用户输入
        });

        setButton(BUTTON_NEGATIVE, "取消", (dialog, which) -> {
            // 处理取消按钮点击事件
            dialog.dismiss();
        });
    }

    public void change(String string){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userId = id; // 替换为您要替换的用户的文档 ID

        // 创建一个包含新数据的 Map
        Map<String, Object> newData = new HashMap<>();
        newData.put("label", label+","+string);
        newData.put("Text", text);
        newData.put("comments", comments);


        // 执行替换操作
        db.collection("records").document(userId)
                .set(newData)
                .addOnSuccessListener(aVoid -> {
                    dismiss();
                })
                .addOnFailureListener(e -> {
                   dismiss();
                });
    }
}
