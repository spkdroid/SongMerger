package com.dija.songmerge.songmerger.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dija.songmerge.songmerger.R;
import com.dija.songmerge.songmerger.SelectSongActivity;
import com.dija.songmerge.songmerger.adapter.RecyclerListAdapter;
import com.dija.songmerge.songmerger.helper.OnStartDragListener;
import com.dija.songmerge.songmerger.helper.SimpleItemTouchHelperCallback;
import com.dija.songmerge.songmerger.helper.SongList;
import com.dija.songmerge.songmerger.repository.SongRepository;
import com.dija.songmerge.songmerger.repository.database.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends Fragment implements OnStartDragListener,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;
    private RecyclerView songList;
    private Button AddButton;
    private int READ_STORAGE_PERMISSION_REQUEST_CODE =1;
    private SongRepository songRepository;

    public SongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongFragment newInstance(String param1, String param2) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_song, container, false);

        songList = v.findViewById(R.id.songlist);
        AddButton = v.findViewById(R.id.addbutton);


        songRepository = new SongRepository(getActivity().getApplicationContext());
        loadSongsFromDB();
        RecyclerListAdapter adapter = new RecyclerListAdapter(
                getActivity(),
                this,
                convertSongsToSongList(loadSongsFromDB()));
        if(adapter!=null)
            songList.setAdapter(adapter);
        songList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(songList);

        AddButton.setOnClickListener(this);


        return v;
    }

    private List<SongList> convertSongsToSongList(List<Song> songList) {
        List<SongList> converted = new ArrayList<>();
        for(Song song: songList) {
            SongList aSongList = new SongList(song.getSongID(), song.getSongLocation());
            converted.add(aSongList);
        }
        return converted;
    }

    private List<Song> loadSongsFromDB()
    {
        List<Song> songsFromRepo = new ArrayList<>();
        FetchSongFromRepo fetchTask = new FetchSongFromRepo();
        try
        {
            songsFromRepo = fetchTask.get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return songsFromRepo;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View v) {

        if(checkPermissionForReadExtertalStorage()) {


            Intent AddSong = new Intent(getActivity(), SelectSongActivity.class);
            startActivityForResult(AddSong,1);
            //startActivity(AddSong);
        }
        else{
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                Toast.makeText(getActivity(),"Permission Issue",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
        System.out.println("Hello World!! This the activity Result");
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FetchSongFromRepo extends AsyncTask<String,Void,List<Song>>
    {

        @Override
        protected List<Song> doInBackground(String... params)
        {
            return songRepository.getAllSongs();
        }
    }
}
