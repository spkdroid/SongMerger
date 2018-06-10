package com.dija.songmerge.songmerger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

    @SuppressLint("StaticFieldLeak")
    public void onBackPressed() {


        new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle("Exit Application")
                .setMessage("Are you sure you want to exit this application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... voids) {
                                songRepository.clearTable();
                                return 0;
                            }
                            @Override
                            protected void onPostExecute(Integer integer) {
                                super.onPostExecute(integer);
                                finish();
                            }
                        }.execute();

                    }
                }).setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.dija.songmerge.songmerger")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.dija.songmerge.songmerger")));
                }
            }
        }).setNegativeButton("No", null).show();
    }

}
