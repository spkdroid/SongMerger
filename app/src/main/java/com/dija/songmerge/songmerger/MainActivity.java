package com.dija.songmerge.songmerger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dija.songmerge.songmerger.fragment.FileFragment;
import com.dija.songmerge.songmerger.fragment.SongFragment;
import com.dija.songmerge.songmerger.repository.SongRepository;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
              }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        songRepository = new SongRepository(getApplicationContext());

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        currentFragment = new SongFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, currentFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

    //    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    //    if (drawer.isDrawerOpen(GravityCompat.START)) {
    //        drawer.closeDrawer(GravityCompat.START);
    //    } else {
    //        super.onBackPressed();
    //    }

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
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
