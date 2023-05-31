package com.MP23_T11.luckycalendar;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;


/*
Statistics UI의 월, 년도를 컨트롤하는 클래스
 */
public class DateViewModel extends ViewModel {

    private int month = Calendar.getInstance().get(Calendar.MONTH)+1; // 월(0 ~ 11)
    private int year = Calendar.getInstance().get(Calendar.YEAR); // 년



    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
