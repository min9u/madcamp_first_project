package com.example.tab_widget;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Item> items;
    private int lastPosition = -1;


    public MyAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    // 뷰 바인딩 부분을 한번만 하도록, ViewHolder 패턴 의무화
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.text_view);
        }
    }

    // 새로운 뷰 생성
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // RecyclerView의 getView 부분을 담당하는 부분
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(items.get(position).getImage());
        holder.textView.setText(items.get(position).getImageTitle());

        setAnimation(holder.imageView, position);
    }

    // Item 개수를 반환하는 부분
    @Override
    public int getItemCount() {
        return items.size();
    }

    // View가 나올때 Animation을 주는 부분
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.setAnimation(animation);
            lastPosition = position;
        }
    }
}