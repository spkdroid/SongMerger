package com.dija.songmerge.songmerger;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dija.songmerge.songmerger.adapter.SongAdapter;
import com.dija.songmerge.songmerger.helper.SongList;
import com.dija.songmerge.songmerger.repository.database.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectSongActivity extends AppCompatActivity implements TextWatcher {

    private List<SongList> songList = new ArrayList<>();
    private List<SongList> resultList = new ArrayList<>();

    private RecyclerView recyclerView;
    private SongAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song);
        recyclerView = findViewById(R.id.fetchsong);
        mAdapter = new SongAdapter(songList);

        EditText e = findViewById(R.id.searchsong);
        e.addTextChangedListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        prepareSongData();
    }

    private void prepareSongData() {

        ArrayList<HashMap<String, String>> hello =  getAudioList();

        for (HashMap<String,String> p:hello)
        {
            System.out.println(p.get("songTitle")+" "+p.get("songPath"));
            SongList a = new SongList(p.get("songTitle"),p.get("songPath"));
            resultList.add(a);

        }
        songList.addAll(resultList);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<HashMap<String, String>> getAudioList() {

        ArrayList<HashMap<String, String>> mSongsList = new ArrayList<HashMap<String, String>>();
        Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA }, null, null, null);

        int count = mCursor.getCount();
        System.out.println("total no of songs are=" + count);
        HashMap<String, String> songMap;

        while (mCursor.moveToNext()) {
            songMap = new HashMap<String, String>();
            songMap.put(
                    "songTitle",
                    mCursor.getString(mCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
            songMap.put("songPath", mCursor.getString(mCursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            mSongsList.add(songMap);
        }

        mCursor.close();
        return mSongsList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        songList.clear();

        for (SongList d:resultList) {
            if(d.getSongName().contains(s.toString())){
                songList.add(d);
            }
        }
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}