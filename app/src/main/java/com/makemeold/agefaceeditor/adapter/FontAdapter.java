package com.makemeold.agefaceeditor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.common.FontModel;
import com.makemeold.agefaceeditor.share.Share;

import java.util.ArrayList;


public class FontAdapter extends RecyclerView.Adapter<FontAdapter.MyViewHolder> {

    private ArrayList<FontModel> list = new ArrayList<>();

    private Context context;
    private EventListener mEventListener;

    public FontAdapter(Context context, ArrayList<FontModel> list_model) {
        this.context = context;
        this.list = list_model;
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

    public FontModel getItem(int position) {
        return list.get(position);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView font_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            font_name = (TextView) itemView.findViewById(R.id.tv_font_name);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.font_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.font_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClicked(position);
            }
        });
        Share.FONT_STYLE = list.get(position).getFont_name();
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.09);
        holder.itemView.getLayoutParams().height = height;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Share.FONT_STYLE.toLowerCase() + ".ttf");
        holder.font_name.setText("Hello");
        holder.font_name.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   /* Context context;
    List<FontModel> list = new ArrayList<>();

    public FontAdapter(Context context, List<FontModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

            view = layoutInflater.inflate(R.layout.font_item,viewGroup,false);

            TextView font_name = (TextView)view.findViewById(R.id.tv_font_name);

            Share.FONT_STYLE = list.get(i).getFont_name();

//        Log.e("Share.FONT_STYLE", Share.FONT_STYLE);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), Share.FONT_STYLE.toLowerCase() + ".ttf");
            font_name.setText("Hello");
            font_name.setTypeface(typeface);

        return view;
    }*/
}
