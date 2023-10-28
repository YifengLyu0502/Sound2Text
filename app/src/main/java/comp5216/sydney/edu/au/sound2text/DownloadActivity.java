package comp5216.sydney.edu.au.sound2text;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadActivity extends AppCompatActivity {

    private TextView recordLabel, recordText;
    private EditText fileNameInput;
    private Button downloadButton;
    private String recordContent, recordLabelContent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // Initialize views
        recordLabel = findViewById(R.id.recordLabel);
        recordText = findViewById(R.id.recordText);
        fileNameInput = findViewById(R.id.fileNameInput);
        downloadButton = findViewById(R.id.downloadButton);

        // Get data from intent
        Intent intent = getIntent();
        recordContent = intent.getStringExtra("recordContent");
        recordLabelContent = intent.getStringExtra("recordLabel");

        // Set text to views
        recordLabel.setText(recordLabelContent);
        recordText.setText(recordContent);

        // Set download button click listener
        downloadButton.setOnClickListener(view -> {
            String fileName = fileNameInput.getText().toString().trim();
            if (!fileName.isEmpty()) {
                downloadFile(fileName);
            } else {
                Toast.makeText(DownloadActivity.this, "Please enter a file name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFile(String fileName) {
        // Check if external storage is available for read and write
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Path to the Download directory
            File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // Create the file with the desired name
            String finalFileName = (recordLabelContent.isEmpty() ? "" : (recordLabelContent + "_")) + fileName;
            File file = new File(downloadPath, finalFileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(recordContent.getBytes());
                Toast.makeText(this, "File downloaded: " + finalFileName, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "External storage is not available", Toast.LENGTH_LONG).show();
        }
    }
}

