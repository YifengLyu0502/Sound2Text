package comp5216.sydney.edu.au.sound2text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
public class MainFragment extends Fragment {
    private ImageView iv_mic;
    private TextView tv_Speech_to_text;
    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;
    private final StringBuilder continuousSpeechResult = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        iv_mic = view.findViewById(R.id.iv_mic);
        tv_Speech_to_text = view.findViewById(R.id.tv_speech_to_text);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                // 可以在此处添加代码，但如果不需要，可以保持为空
            }

            @Override
            public void onBeginningOfSpeech() {
                // ...
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // ...
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // ...
            }

            @Override
            public void onEndOfSpeech() {
                // ...
            }

            @Override
            public void onError(int error) {
                if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT && isListening) {
                    startListening(); // 如果检测到语音输入超时并且仍在监听状态，则重新开始监听
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String text_result = tv_Speech_to_text.getText().toString();
                    text_result = text_result + " " + matches.get(0).toString();
                    tv_Speech_to_text.setText(text_result);

                    RecordsFragment fragment = new RecordsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("key_string", text_result);
                    fragment.setArguments(bundle);
                }

                if (isListening) {
                    startListening();  // 如果isListening仍然为true，则再次开始监听
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    continuousSpeechResult.append(matches.get(0)).append(" "); // 添加到持续的语音结果
                    tv_Speech_to_text.setText(continuousSpeechResult.toString()); // 更新文本视图
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // 此方法是您之前缺少的。您可以保持其为空，除非需要处理某些特定事件
            }

        });


        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListening) {
                    isListening = false;
                    speechRecognizer.stopListening();  // 停止语音识别
                } else {
                    isListening = true;
                    startListening();  // 开始语音识别
                }
            }
        });

        return view;
    }

    private void startListening() {
        Intent intent = new Intent();
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.SIMPLIFIED_CHINESE);
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}


