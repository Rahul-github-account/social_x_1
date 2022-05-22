package com.example.socialx;

import android.content.Context;
import android.widget.Toast;

import com.example.socialx.Home_page.NewsApiRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(" https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void retrieveNews(OnFetchDataMethod listener, String category, String query){
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiRes> call = callNewsApi.callHeadlines("in",
                category, query, "b4291449c7a742498a4b1e9448177308");
        try {
            call.enqueue(new Callback<NewsApiRes>() {
                @Override
                public void onResponse(Call<NewsApiRes> call, Response<NewsApiRes> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listener.onFetchData(response.body().getArticles(), response.message());
                }

                @Override
                public void onFailure(Call<NewsApiRes> call, Throwable t) {
                    listener.onError(t.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //b4291449c7a742498a4b1e9448177308
    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiRes> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query(("q")) String query,
                @Query("apiKey") String apiKey);
    }
}
