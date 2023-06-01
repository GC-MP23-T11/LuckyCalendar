package com.MP23_T11.luckycalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DateBoxAdapter extends RecyclerView.Adapter<DateBoxAdapter.DateBoxViewHolder> {
    private List<Memo> memos; // 여기서 Memo는 당신이 사용하는 메모 데이터 클래스입니다.

    public static class DateBoxViewHolder extends RecyclerView.ViewHolder {
        // 여기에는 view holder에 들어갈 view들을 선언합니다.
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
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

}
