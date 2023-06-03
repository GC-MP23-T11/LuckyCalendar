package com.MP23_T11.luckycalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tab";

    Settings settingsFragment;
    Calendar calendarFragment;
    Statistics statisticsFragment;

    private static final int userTab = R.id.userTab;
    private static final int calTab = R.id.calTab;
    private static final int statTab = R.id.statTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsFragment = new Settings();
        calendarFragment = new Calendar();
        statisticsFragment = new Statistics();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.calTab);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);
        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == userTab) {
                            Log.d(TAG, "userTab open");
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                            return true;

                        } else if (item.getItemId() == calTab) {
                            Log.d(TAG, "calTab open");
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
                            return true;

                        } else if (item.getItemId() == statTab) {
                            Log.d(TAG, "statTab open");
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, statisticsFragment).commit();
                            return true;

                        }

                        return false;
                    }
                }
        );

    }
}