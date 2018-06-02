package com.dija.songmerge.songmerger.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface SongDAOAccess
{
    @Insert
    void insertSingleSong(Song songToInsert);

    @Query("SELECT * FROM Song WHERE songID = :songID")
    Song fetchSongByID(String songID);

    @Delete
    void deleteSong(Song songToBeDeleted);


}
