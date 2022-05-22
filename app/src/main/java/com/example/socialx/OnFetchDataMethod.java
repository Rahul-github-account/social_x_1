package com.example.socialx;

import com.example.socialx.Home_page.NewsHeadlines;

import java.util.List;

public interface OnFetchDataMethod<NewsApiRes> {

    void onFetchData(List<NewsHeadlines> list, String fetchMessage);
    void onError(String errorMessage);

}
