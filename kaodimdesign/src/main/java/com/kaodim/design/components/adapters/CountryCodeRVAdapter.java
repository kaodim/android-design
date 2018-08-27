package com.kaodim.design.components.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.models.CountryCodeRowItem;

import java.util.ArrayList;

public class CountryCodeRVAdapter extends RecyclerView.Adapter<CountryCodeRVAdapter.ViewHolder> {

    ArrayList<CountryCodeRowItem> items;
    private LayoutInflater inflater;
    private Context context;
    CountryCodeSelectionListener listener;

    public interface CountryCodeSelectionListener {
        void onSelected(CountryCodeRowItem item);
    }

    public CountryCodeRVAdapter(Context context, ArrayList<CountryCodeRowItem> items, CountryCodeSelectionListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView ivFlag;
        public TextView tvCode;

        public ViewHolder(View v) {
            super(v);
            view = v;
            ivFlag = view.findViewById(R.id.ivFlag);
            tvCode = view.findViewById(R.id.tvCode);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.single_country_code_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CountryCodeRowItem item = items.get(position);

        holder.ivFlag.setImageResource(item.flagIconResource);
        holder.tvCode.setText(item.code);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public CountryCodeRowItem getItem(int postion) {
        return items.get(postion);
    }
}