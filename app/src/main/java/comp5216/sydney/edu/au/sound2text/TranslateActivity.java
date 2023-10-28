package comp5216.sydney.edu.au.sound2text;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import comp5216.sydney.edu.au.sound2text.bean.Records;

public class TranslateActivity extends AppCompatActivity {
    private   TextView tvResult;
    private String currentLabel; // 用于存储当前record的标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        // 获取上个页面传递过来的record数据
        Records records = getIntent().getParcelableExtra("records");
        // 初始化控件
        TextView tvContent = findViewById(R.id.tv_content);
         tvResult = findViewById(R.id.tv_result);
        TextView btnTranslate = findViewById(R.id.btn_translate);

        Button btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sending Data from TranslateActivity to DownloadActivity
                Intent intent = new Intent(TranslateActivity.this, DownloadActivity.class);
                intent.putExtra("content", tvContent.getText().toString());
                intent.putExtra("label", records.getLabel());
                startActivity(intent);
            }
        });

        // 对content内容进行设置 放到页面里
        tvContent.setText(records.getContent());

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            // 点击翻译按钮的时候调用API
            public void onClick(View v) {
                doHttp(records.getContent());
            }
        });


    }

    private void doHttp(String content) {
//        Http.post(content, false, new Http.OnTranslateResult() {
//            @Override
//            public void result(TranslateResult result) {
//                Log.d("TAG", "result: "+result.getTranslation().get(0));
//            }
//        });
        ChatGPTTranslate.doHttp(content, new ChatGPTTranslate.HttpListener() {
            @Override
            public void success(String content) {
                tvResult.setText(content);
            }

            @Override
            public void error(String message) {

            }
        });
    }
}