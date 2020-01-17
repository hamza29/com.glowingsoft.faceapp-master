package com.makemeold.agefaceeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.common.StickerModel_Assets;

import java.util.ArrayList;

public class StickerAdapter_Assets extends RecyclerView.Adapter<StickerAdapter_Assets.MyView> {

    private ArrayList<StickerModel_Assets> list = new ArrayList<>();
    private Context context;
    private EventListener mEventListener;

    public StickerAdapter_Assets(Context context, ArrayList<StickerModel_Assets> list) {
        this.context = context;
        this.list = list;
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView iv_sticker;
        ProgressBar progressBar;

        public MyView(View itemView) {
            super(itemView);
            iv_sticker = (ImageView) itemView.findViewById(R.id.iv_sticker_row);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface EventListener {
        void onItemViewClicked(int position);

        void onDeleteMember(int position);
    }

    private void onItemViewClicked(int position) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(position);
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }


    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.more_sticker_row, parent, false);

        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(MyView holder, final int position) {

        holder.iv_sticker.setImageDrawable(list.get(position).getDrawable());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemViewClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
