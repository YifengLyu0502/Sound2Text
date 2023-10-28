package comp5216.sydney.edu.au.sound2text;//package comp5216.sydney.edu.au.sound2text;
//
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.api.gax.rpc.ApiStreamObserver;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.google.api.gax.rpc.BidiStreamingCallable;
//import com.google.api.gax.rpc.ClientStream;
//import com.google.api.gax.rpc.ResponseObserver;
//import com.google.api.gax.rpc.StreamController;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.speech.v1.*;
//import com.google.protobuf.ByteString;
//import com.google.cloud.speech.v1.SpeechSettings;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//public class Speech2Text extends AppCompatActivity {
//    private AudioRecord audioRecord;
//    private boolean isRecording = false;
//    private byte[] buffer;
//    TextView tv_Speech_to_text;
//    private final int sampleRate = 16000; // 16kHz
//    private SpeechClient speechClient;
//    private StreamingRecognizeRequest.Builder requestBuilder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_fragment);
//
//        ImageView iv_mic = findViewById(R.id.iv_mic); // replace with your ImageView's ID
//        tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);
//
//        iv_mic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isRecording) {
//                    startRecordingAndRecognizing();
//                    tv_Speech_to_text.setText("Stop Recording");
//                } else {
//                    stopRecordingAndRecognizing();
//                    tv_Speech_to_text.setText("Start Recording");
//                }
//            }
//        });
//
//
//
//        // 初始化SpeechClient
//        try {
//            speechClient = SpeechClient.create();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 设置音频配置
//        RecognitionConfig config = RecognitionConfig.newBuilder()
//                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//                .setSampleRateHertz(sampleRate)
//                .setLanguageCode("en-US")
//                .build();
//
//        requestBuilder = StreamingRecognizeRequest.newBuilder().setStreamingConfig(
//                StreamingRecognitionConfig.newBuilder()
//                        .setConfig(config)
//                        .setInterimResults(true)
//                        .build()
//        );
//
//        // ... 其他初始化代码
//    }
//
//    private void startRecordingAndRecognizing() {
//        // 初始化AudioRecord
//        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
//        buffer = new byte[minBufferSize];
//        audioRecord.startRecording();
//        isRecording = true;
//
//        // 开始发送数据至Google Cloud
//        new Thread(() -> {
//            BidiStreamingCallable<StreamingRecognizeRequest, StreamingRecognizeResponse> callable = speechClient.streamingRecognizeCallable();
//            ApiStreamObserver<StreamingRecognizeResponse> responseObserver = new ApiStreamObserver<StreamingRecognizeResponse>() {
//                @Override
//                public void onNext(StreamingRecognizeResponse response) {
//                    if (response.getResultsList().size() > 0) {
//                        String transcript = response.getResultsList().get(0).getAlternativesList().get(0).getTranscript();
//                        tv_Speech_to_text.setText(transcript);
//                    }
//                }
//
//
//                @Override
//                public void onError(Throwable t) {
//                    // 处理错误
//                    // ...
//                }
//
//                @Override
//                public void onCompleted() {
//                    // ...
//                }
//            };
//
//            ClientStream<StreamingRecognizeRequest> clientStream = (ClientStream<StreamingRecognizeRequest>) callable.bidiStreamingCall(responseObserver);
//
//
//            while (isRecording) {
//                int bytesRead = audioRecord.read(buffer, 0, buffer.length);
//                if (bytesRead > 0) {
//                    ByteString audioBytes = ByteString.copyFrom(buffer, 0, bytesRead);
//                    StreamingRecognizeRequest request = requestBuilder.setAudioContent(audioBytes).build();
//                    clientStream.send(request);
//                }
//            }
//            clientStream.closeSend();
//        }).start();
//    }
//
//    private void stopRecordingAndRecognizing() {
//        isRecording = false;
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
//    }
//
//
//    private void initSpeechClient() {
//        try {
//            // 从assets文件夹加载服务账户密钥
//            GoogleCredentials credentials = GoogleCredentials.fromStream(getAssets().open("your-service-account-key.json"))
//                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
//
//            // 使用上面的credentials初始化SpeechClient
//            SpeechSettings settings = SpeechSettings.newBuilder()
//                    .setCredentialsProvider(() -> credentials)
//                    .build();
//
//            speechClient = SpeechClient.create(settings);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    // ... 其他活动代码
//}
