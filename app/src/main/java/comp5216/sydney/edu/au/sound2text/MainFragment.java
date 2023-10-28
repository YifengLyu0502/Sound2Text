package comp5216.sydney.edu.au.sound2text;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MainFragment extends Fragment {
    //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
    FirebaseAuth mAuth;
    FirebaseUser user;
    private ImageView iv_mic;
    private TextView tv_Speech_to_text,text_gpt;
    private SpeechRecognizer speechRecognizer;
    String speechText, GPT_respond;
    Button gpt_btn,upload_btn;
    private boolean isListening = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StringBuilder continuousSpeechResult = new StringBuilder();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        //获取当前用户信息
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        iv_mic = view.findViewById(R.id.iv_mic);
        tv_Speech_to_text = view.findViewById(R.id.tv_speech_to_text);
        text_gpt = view.findViewById(R.id.text_from_gpt);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        gpt_btn = view.findViewById(R.id.gpt_button);
        upload_btn = view.findViewById(R.id.upload);

        gpt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandlerThread handlerThread = new HandlerThread("NetworkThread");
                handlerThread.start();
                Handler backgroundHandler = new Handler(handlerThread.getLooper());

                backgroundHandler.post(() -> {
                    ChatGPTService service = new ChatGPTService();
                    service.callChatGPT("Help me summarise these text: " + tv_Speech_to_text.getText().toString(),new ChatGPTService.CompletionCallback(){
                        @Override
                        public void onCompletion(String result) {
                            GPT_respond = result;
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    text_gpt.setText(result);
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // 在这里处理错误情况
                            Log.e("GPT Error", errorMessage);
                        }
                    });
                });

            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RecordID= UUID.randomUUID().toString();
                String Content= GPT_respond;



                db.collection("User profile").document(user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                  @Override
                                                  public void onSuccess(DocumentSnapshot document) {
                                                      if (document.exists()){
                                                          //获取他的course
                                                          uploadRecord(RecordID,user,Content, (String) document.get("Course"));
                                                      }
                                                  }
                                              });


            }
        });

        String text1;
        //在下载的文件夹里创建txt文件，并将text1保存进去。


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
                if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
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
                    Toast.makeText(getActivity(), "Speech recognizer stopped", Toast.LENGTH_SHORT).show();
                } else {
                    isListening = true;
                    startListening();  // 开始语音识别
                    Toast.makeText(getActivity(), "Please speak now", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void startListening() {
        Intent intent = new Intent();
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.SIMPLIFIED_CHINESE);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000); // 例如：10秒
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000); // 例如：7秒
        speechRecognizer.startListening(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }


    //上传到firebase
    public void uploadRecord(String recordID, FirebaseUser user, String Content, String Label) {
        CollectionReference RecordInfo = db.collection("records");
        CollectionReference UserInfo = db.collection("User profile");
        DocumentReference userDocRef = UserInfo.document(user.getUid());
        userDocRef.update("recordsList",FieldValue.arrayUnion(recordID));

//        userDocRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            ArrayList<String> records = (ArrayList<String>) documentSnapshot.get("records");
//
//                            records.add(recordID);
//                            userDocRef.update("recordsList",records);
//
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error getting records", e);
//                    }
//                });


        DocumentReference recordDocRef = RecordInfo.document(recordID);
        Map<String, Object> dataOfRecord = new HashMap<>();

        dataOfRecord.put("RecordID", recordID);
        dataOfRecord.put("owner", user.getUid()); // 存储用户的 UID，而不是整个用户对象
        dataOfRecord.put("content", Content);
        dataOfRecord.put("label", Label);
        dataOfRecord.put("comment", "");
        recordDocRef.set(dataOfRecord);

        }


}


