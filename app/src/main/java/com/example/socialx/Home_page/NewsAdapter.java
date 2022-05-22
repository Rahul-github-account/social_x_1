package com.example.socialx.Home_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialx.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    List<NewsHeadlines> newsHeadlines;

    public NewsAdapter(Context context, List<NewsHeadlines> newsHeadlines) {
        this.context = context;
        this.newsHeadlines = newsHeadlines;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.news_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        String date = newsHeadlines.get(position).getPublishedAt().split("T")[0];
        String time = newsHeadlines.get(position).getPublishedAt().split("T")[1].substring(0, 5);

        holder.publishTv.setText("Dated : " + date + " by " + time);
        holder.sourceTv.setText(newsHeadlines.get(position).getSource().getName());
        holder.tittleTv.setText(newsHeadlines.get(position).getTitle());
        holder.descTv.setText(newsHeadlines.get(position).getDescription());

        Glide.with(context)
                .load(newsHeadlines.get(position).getUrlToImage())
                .placeholder(R.drawable.image_icon)
                .into(holder.sourceImv);
    }

    @Override
    public int getItemCount() {
        return newsHeadlines.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView publishTv, sourceTv, tittleTv, descTv;
        ImageView sourceImv;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            publishTv = itemView.findViewById(R.id.publishedAtTv);
            sourceTv = itemView.findViewById(R.id.newsSourceTv);
            tittleTv = itemView.findViewById(R.id.newsTittleTv);
            descTv = itemView.findViewById(R.id.newsDescTv);
            sourceImv = itemView.findViewById(R.id.newsImageView);
        }
    }
}
