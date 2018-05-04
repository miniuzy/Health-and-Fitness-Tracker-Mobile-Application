package com.example.mohammeduzair.androidapp.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.ScndPage.musicplayer;
import com.example.mohammeduzair.androidapp.ThrdPage.menu;
import com.example.mohammeduzair.androidapp.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by mohammeduzair on 25/03/2018.
 */

public class BotNavHelp {


    public static void setupBottomNavView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

    }
    //context is final so you can use it inside the override method
    public static void enableNav(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_dash:
                        Intent intent1 = new Intent(context, Homepage.class); //NUM 0
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_dash2:
                        Intent intent2 = new Intent(context, musicplayer.class); // NUM 1
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_dash3:
                        Intent intent3 = new Intent(context, menu.class);
                        context.startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }


}
