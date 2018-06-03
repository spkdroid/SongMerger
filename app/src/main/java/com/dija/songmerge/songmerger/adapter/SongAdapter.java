package com.dija.songmerge.songmerger.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dija.songmerge.songmerger.R;
import com.dija.songmerge.songmerger.helper.SongList;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private List<SongList> songList;
    private SongSelectedListener songSelectedListener;

    public SongAdapter(List<SongList> songList, SongSelectedListener songSelectedListener) {
        this.songList = songList;
        this.songSelectedListener = songSelectedListener;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView songName,songLocation;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            songName = view.findViewById(R.id.songname);
            songLocation = view.findViewById(R.id.songlocation);
        }

        @Override
        public void onClick(View v) {
       //     Toast.makeText(v.getContext(),songName.getText().toString(),Toast.LENGTH_LONG).show();
            new BottomSheet.Builder(v.getContext())
                    .setSheet(R.menu.bottom_sheet)
                    .setTitle(R.string.options)
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                        }

                        @Override
                        public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                            String songId = songName.getText().toString();
                            String location = songName.getText().toString();
                            songSelectedListener.onSongSelected(songId,location);
                        }

                        @Override
                        public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @Nullable Object o, int i) {

                        }
                    })
                    .show();


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
