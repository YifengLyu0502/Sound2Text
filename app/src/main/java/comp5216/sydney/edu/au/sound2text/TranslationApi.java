package comp5216.sydney.edu.au.sound2text;

import comp5216.sydney.edu.au.sound2text.bean.TranslationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationApi {
    @GET("/language/translate/v2")
    Call<TranslationResponse> translateText(
            @Query("key") String apiKey,
            @Query("q") String textToTranslate,
            @Query("source") String sourceLanguage,
            @Query("target") String targetLanguage
    );
}
