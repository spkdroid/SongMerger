package com.dija.songmerge.songmerger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.dija.songmerge.songmerger.Model.SongModel;
import com.dija.songmerge.songmerger.adapter.SongAdapter;
import com.dija.songmerge.songmerger.adapter.SongSelectedListener;
import com.dija.songmerge.songmerger.helper.SongList;
import com.dija.songmerge.songmerger.repository.database.Song;

import java.util.ArrayList;
import java.util.List;

public class SelectSongActivity extends AppCompatActivity implements TextWatcher, SongSelectedListener
{

    private List<SongList> songList = new ArrayList<>();
    private List<SongList> resultList = new ArrayList<>();
    private SongModel model;
    private RecyclerView recyclerView;
    private SongAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song);

        model = new SongModel(getApplicationContext());

        recyclerView = findViewById(R.id.fetchsong);

        mAdapter = new SongAdapter(songList, this);

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
        resultList = model.getSongList(getContentResolver());
        songList.addAll(resultList);
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onSongSelected(String songId, String songLocation)
    {
        Song songToBeAdded = new Song(songId, songLocation);
        model.addSongToRepo(songToBeAdded);
        Log.d("dbMessage","added");
    }


}