package com.MP23_T11.luckycalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ImageView ProfilePic = findViewById(R.id.ProfilePic);
        ProfilePic.setClipToOutline(true);
    }

    public void modProfilePic(View view) {
    }

    public void modUserNum(View view) {
    }

    public void modUserEmail(View view) {
    }
}