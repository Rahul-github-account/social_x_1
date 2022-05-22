package com.example.socialx.Home_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialx.OnFetchDataMethod;
import com.example.socialx.R;
import com.example.socialx.RequestManager;
import com.example.socialx.Sign_UP_In.SignUpIn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends AppCompatActivity {

    ImageView menuIcon;
    SearchView searchView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        menuIcon = findViewById(R.id.menuIconImg);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        retrieveNews();
        showMenu();
        searchItems();
    }

    private void searchItems() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                RequestManager manager = new RequestManager(HomePage.this);
                manager.retrieveNews(listener, "entertainment", s);
                return true;
            }
        });
    }

    private void retrieveNews() {
        RequestManager manager = new RequestManager(this);
        manager.retrieveNews(listener, "entertainment", null);
    }

    private final OnFetchDataMethod<NewsApiRes> listener = new OnFetchDataMethod<NewsApiRes>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String fetchMessage) {
            if (list.isEmpty()) {
                Toast.makeText(HomePage.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                recyclerView.setAdapter(new NewsAdapter(HomePage.this, list));
            }
        }

        @Override
        public void onError(String errorMessage) {
            Toast.makeText(HomePage.this, "Api Error Occurred", Toast.LENGTH_SHORT).show();
        }
    };

    private void showMenu() {
        menuIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menuRefresh:
                        recreate();
                        break;
                    case R.id.signOut:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomePage.this, SignUpIn.class));
                        finish();
                }
                return true;
            });
        });
    }

}