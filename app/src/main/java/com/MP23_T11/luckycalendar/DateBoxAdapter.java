package com.MP23_T11.luckycalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DateBoxAdapter extends RecyclerView.Adapter<DateBoxAdapter.DateBoxViewHolder> {
    private List<Memo> memos; // 여기서 Memo는 Firebase 에서 받아올 데이터 핸들링 클래스
    private int selectedItem = -1;// 현재 선택된 박스 indexing

    public static class DateBoxViewHolder extends RecyclerView.ViewHolder {
        // 여기에는 view holder에 들어갈 view들을 선언
        TextView date;
        TextView day;

        public DateBoxViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date_text);
            day = v.findViewById(R.id.day_text);
        }
    }

    public DateBoxAdapter(List<Memo> memos) {
        this.memos = memos;
    }

    @Override
    public DateBoxAdapter.DateBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_box, parent, false);
        return new DateBoxViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DateBoxViewHolder holder, int position) {
        Memo memo = memos.get(position);
        holder.date.setText(memo.getDate());
        holder.day.setText(memo.getDayOfWeek());

        // Change the background color if this item is selected
        if (position == selectedItem) {
            holder.itemView.setBackgroundResource(R.color.buttonSelected);
            holder.date.setTextColor(Color.WHITE);
            holder.day.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.rectangle_long_static);
            holder.date.setTextColor(Color.BLACK);
            holder.day.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }


    //박스 선택 관련 method
    public void selectNextItem() {
        if (selectedItem < memos.size() - 1) {
            selectedItem++;
            notifyDataSetChanged();
        }
    }

    public void selectPreviousItem() {
        if (selectedItem > 0) {
            selectedItem--;
            notifyDataSetChanged();
        }
    }

    public Memo getSelectedItem() {
        if (selectedItem >= 0 && selectedItem < memos.size()) {
            return memos.get(selectedItem);
        } else {
            return null;
        }
    }

}
