package com.dija.songmerge.songmerger.helper;

public class SongList {

    private String songName;

    private String songLocation;

    public SongList(String _songName,String _songLocation){
        songName = _songName;
        songLocation = _songLocation;
    }


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongLocation() {
        return songLocation;
    }

    public void setSongLocation(String songLocation) {
        this.songLocation = songLocation;
    }

}
