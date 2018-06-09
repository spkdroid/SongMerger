package com.dija.songmerge.songmerger.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.dija.songmerge.songmerger.helper.SongList;
import com.dija.songmerge.songmerger.repository.SongRepository;
import com.dija.songmerge.songmerger.repository.database.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongModel
{
    private SongRepository repository;

    public SongModel(Context applicationContext) {
        repository = new SongRepository(applicationContext);
    }

    public List<SongList> getSongList(ContentResolver contentResolver) {
        ArrayList<HashMap<String, String>> fromDevice = getAudioList(contentResolver);
        List<SongList> songList = new ArrayList<>();

        for (HashMap<String,String> p:fromDevice)
        {
            System.out.println(p.get("songTitle")+" "+p.get("songPath"));
            SongList a = new SongList(p.get("songTitle"),p.get("songPath"));
            songList.add(a);
        }
        return songList;
    }

    private ArrayList<HashMap<String, String>> getAudioList(ContentResolver contentResolver) {

        ArrayList<HashMap<String, String>> mSongsList = new ArrayList<HashMap<String, String>>();
        Cursor mCursor = contentResolver.query(
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

    public void addSongToRepo(Song songToBeAdded) {
        StoreSongInRepo task = new StoreSongInRepo();
        task.execute(songToBeAdded);
    }


    private class StoreSongInRepo extends AsyncTask<Song,String, String>
    {

        @Override
        protected String doInBackground(Song... params)
        {
            repository.addSong(params[0]);
            List<Song> songs = repository.getAllSongs();
            Log.d("DbSize", String.valueOf(songs.size()));
            return "adding song";
        }
    }

}
