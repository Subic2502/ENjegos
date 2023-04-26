package com.gorskivijenac.enjegos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.gorskivijenac.enjegos.databinding.ActivityMainBinding;
import com.gorskivijenac.enjegos.ui.citaj.CitajFragment;
import com.gorskivijenac.enjegos.ui.obelezivaci.ObelezivaciFragment;
import com.gorskivijenac.enjegos.ui.raspolozenja.RaspolozenjaFragment;
import com.gorskivijenac.enjegos.ui.zanimljivosti.ZanimljivostiFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        replaceFragment(new PocetniFragment());

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                    1);

        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(this, MyNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.navigation_bar_citaj:
                    replaceFragment(new CitajFragment());
                    break;
                case R.id.navigation_bar_raspolozenja:
                    replaceFragment(new RaspolozenjaFragment());
                    break;
                case R.id.navigation_bar_obelezivaci:
                    replaceFragment(new ObelezivaciFragment());
                    break;
                case R.id.navigation_bar_zanimljivosti:
                    replaceFragment(new ZanimljivostiFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public static boolean isTablet(Context context) {
        boolean isTablet = false;

        try {
            // Get the display metrics of the device
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            // Calculate the physical size of the screen in inches
            float widthInches = metrics.widthPixels / metrics.xdpi;
            float heightInches = metrics.heightPixels / metrics.ydpi;
            double screenInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));

            // Check if the screen size is larger than 7 inches
            if (screenInches >= 7.0) {
                isTablet = true;
            } else {
                // Check if the screen density is high
                if (metrics.densityDpi >= DisplayMetrics.DENSITY_HIGH) {
                    isTablet = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isTablet;
    }

}