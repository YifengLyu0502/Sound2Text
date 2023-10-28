package comp5216.sydney.edu.au.sound2text;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DownloadActivity extends AppCompatActivity {
    private String translatedText;
    private String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // 初始化UI组件
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView5 = findViewById(R.id.textView5);
        EditText etFileName = findViewById(R.id.editTextText);
        Button btnDownload = findViewById(R.id.download_btn);


        String tvContentData = getIntent().getStringExtra("tv_content_data");
        String recordLabel = getIntent().getStringExtra("record_label");

        textView3.setText(tvContentData);
        textView5.setText(recordLabel);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = etFileName.getText().toString();
                if (!fileName.trim().isEmpty()) {
                    // 如果label不为空，则在文件名中添加label
                    String finalFileName = (label != null && !label.trim().isEmpty()) ? label + "_" + fileName : fileName;
                    downloadFile(finalFileName, translatedText);
                } else {
                    Toast.makeText(DownloadActivity.this, "File name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadFile(String fileName, String content) {
        // 创建文件并写入内容
        try {
            File directory = new File(Environment.getExternalStorageDirectory(), "download");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
            Toast.makeText(this, "File download successful: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}



