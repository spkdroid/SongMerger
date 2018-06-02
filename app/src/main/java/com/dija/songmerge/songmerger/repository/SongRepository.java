package com.dija.songmerge.songmerger.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.dija.songmerge.songmerger.repository.database.Song;
import com.dija.songmerge.songmerger.repository.database.SongDatabase;

import java.util.List;

public class SongRepository
{

    private Context applicationContext;
    private SongDatabase database;


    public SongRepository(Context applicationContext) {
        this.applicationContext = applicationContext;
        initDB();
    }

    private void initDB()
    {
        final String DB_NAME = "SONGDATABASE";
        database = Room.databaseBuilder(applicationContext,SongDatabase.class, DB_NAME )
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<Song> getAllSongs() {
        return database.getSongDAOAccess().fetchAllSongs();
    }

    public void addSong(Song songToBeAdded) {
        database.getSongDAOAccess().insertSingleSong(songToBeAdded);
    }

    public void deleteSong(Song songToBeDeleted) {
        database.getSongDAOAccess().deleteSong(songToBeDeleted);
    }

    public Song getSongById(String songId) {
       return  database.getSongDAOAccess().fetchSongByID(songId);
    }

}
