package com.example.tab_widget;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(final View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("int the ViewHolder","222");
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Item item = items.get(pos);
                        Intent intent = new Intent(view.getContext(), imageEnlargeActivity.class);//여기서 왜 view.getContext()?
                        intent.putExtra("title",items.get(pos).getImageTitle());
                        intent.putExtra("image",items.get(pos).getUri().toString());
                        Log.d("title",items.get(pos).getImageTitle());
                        view.getContext().startActivity(intent);//여기서는 왜 view?

                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        View deleteDialogView = (View) View.inflate(context, R.layout.delete_dialog, null);
                        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                        dlg.setTitle(items.get(pos).getImageTitle());
                        dlg.setView(deleteDialogView);
                        dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                intent.putExtra("position",pos);
                                view.getContext().startActivity(intent);
                            }
                        });
                        dlg.show();

                    }
                    return false;
                }
            });
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), items.get(position).getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bm);
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