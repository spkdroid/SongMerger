package com.dija.songmerge.songmerger.repository.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SongDAOAccess
{
    @Insert
    void insertSingleSong(Song songToInsert);

    @Query("SELECT * FROM Song WHERE songID = :songID")
    Song fetchSongByID(String songID);

    @Query("SELECT * FROM Song")
    List<Song> fetchAllSongs();

    @Delete
    void deleteSong(Song songToBeDeleted);


}
