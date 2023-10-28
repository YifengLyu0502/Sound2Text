package comp5216.sydney.edu.au.sound2text;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTService {

    private static final String CHATGPT_URL = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    final String[] finalresult = new String[1];

    public interface CompletionCallback {
        void onCompletion(String result);
        void onError(String errorMessage);
    }
    public void callChatGPT(String input, CompletionCallback callback) {
        OkHttpClient client = new OkHttpClient();


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-instruct");
            jsonBody.put("prompt", input);
            jsonBody.put("max_tokens", 3000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        callback.onCompletion(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Failed to parse the response");
                    }
                } else {
                    callback.onError("Fail to connect");
                    Log.i("Failed to load response due to ", response.body().string());
                }
            }
        });
    }
}
