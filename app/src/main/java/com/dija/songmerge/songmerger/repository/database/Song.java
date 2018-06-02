package com.dija.songmerge.songmerger.repository.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Song
{
    @PrimaryKey
    @NonNull
    private String songID;

    private String songLocation;

    public Song(String songID, String songLocation) {
        this.songID = songID;
        this.songLocation = songLocation;
    }

    @NonNull
    public String getSongID()
    {
        return songID;
    }

    public void setSongID(@NonNull String songID)
    {
        this.songID = songID;
    }

    public String getSongLocation()
    {
        return songLocation;
    }

    public void setSongLocation(String songLocation)
    {
        this.songLocation = songLocation;
    }


}
