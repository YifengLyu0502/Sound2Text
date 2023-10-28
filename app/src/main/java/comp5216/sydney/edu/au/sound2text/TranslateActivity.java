package comp5216.sydney.edu.au.sound2text;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.sound2text.bean.Records;

public class TranslateActivity extends AppCompatActivity {
    private   TextView tvResult;
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