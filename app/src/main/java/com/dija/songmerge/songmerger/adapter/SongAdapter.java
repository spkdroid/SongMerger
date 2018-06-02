package com.dija.songmerge.songmerger.adapter;

import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dija.songmerge.songmerger.R;
import com.dija.songmerge.songmerger.helper.SongList;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private List<SongList> songList;

    public SongAdapter(List<SongList> songList) {
        this.songList = songList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView songName,songLocation;

        public MyViewHolder(View view) {
            super(view);
            songName = view.findViewById(R.id.songname);
            songLocation = view.findViewById(R.id.songlocation);
        }
    }

    @NonNull
    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.MyViewHolder holder, int position) {
        SongList song = songList.get(position);
        holder.songName.setText(song.getSongName());
        holder.songLocation.setText(song.getSongLocation());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

}
