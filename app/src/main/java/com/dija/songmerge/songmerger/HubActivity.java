package com.dija.songmerge.songmerger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.dija.songmerge.songmerger.fragment.FileFragment;
import com.dija.songmerge.songmerger.fragment.NotificationFragment;
import com.dija.songmerge.songmerger.fragment.SongFragment;
import com.dija.songmerge.songmerger.repository.SongRepository;

public class HubActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Fragment currentFragment = null;
    private FragmentTransaction ft;
    private SongRepository songRepository;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currentFragment = new SongFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    currentFragment = new FileFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
             //   case R.id.navigation_notifications:
               //     currentFragment = new NotificationFragment();
               //     ft = getSupportFragmentManager().beginTransaction();
               //     ft.replace(R.id.content, currentFragment);
              //      ft.commit();
              //      return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        songRepository = new SongRepository(getApplicationContext());



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        currentFragment = new SongFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, currentFragment);
        ft.commit();
    }

    public void onBackPressed(){

        /**
         *  Clear the database over here
         *
         */
        songRepository.clearTable();
    }

}
