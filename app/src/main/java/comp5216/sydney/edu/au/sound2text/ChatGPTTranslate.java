package comp5216.sydney.edu.au.sound2text;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import comp5216.sydney.edu.au.sound2text.bean.TranslationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatGPTTranslate {

    public interface HttpListener {
        void success(String content);

        void error(String message);
    }

    public static void doHttp(String text, HttpListener listener) {
        String apiKey = "AIzaSyBY-0CxQGkpnDqB7LK2U3LqLJCgn4QY6Ao";
        String textToTranslate = text;
        String sourceLanguage = "en";
        String targetLanguage = "zh-CN"; // 目标语言
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory( GsonConverterFactory.create())
                .baseUrl("https://translation.googleapis.com/language/translate/v2/")
                .build();
        TranslationApi translationApi = retrofit.create(TranslationApi.class);
        Call<TranslationResponse> call = translationApi.translateText(apiKey, textToTranslate, sourceLanguage, targetLanguage);

        call.enqueue(new Callback<TranslationResponse>() {


            @Override
            public void onResponse(Call<TranslationResponse> call, retrofit2.Response<TranslationResponse> response) {

                String translatedText = response.body().getData().getTranslations().get(0).getTranslatedText();
                Log.d("TAG", "onResponse: "+translatedText);
                listener.success(translatedText);
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {
                // 处理请求失败
                Log.d("TAG", "onResponse: "+t.getMessage());
            }
        });

    }

    private static String sign(String origin) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            StringBuilder sb = new StringBuilder();
            for (byte b : md5.digest(origin.getBytes(StandardCharsets.UTF_8))) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.d("TAG", "sign: " + e.getMessage());
            return "";
        }

    }
}
