package com.dija.songmerge.songmerger.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dija.songmerge.songmerger.R;
import com.dija.songmerge.songmerger.SelectSongActivity;
import com.dija.songmerge.songmerger.adapter.RecyclerListAdapter;
import com.dija.songmerge.songmerger.helper.OnStartDragListener;
import com.dija.songmerge.songmerger.helper.SimpleItemTouchHelperCallback;
import com.dija.songmerge.songmerger.helper.SongList;
import com.dija.songmerge.songmerger.repository.SongRepository;
import com.dija.songmerge.songmerger.repository.database.Song;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
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
public class SongFragment extends Fragment implements OnStartDragListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;
    private RecyclerView songList;
    private Button AddButton;
    private Button MergeButton;
    private int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private SongRepository songRepository;
    private static RecyclerListAdapter adapter;


    static List<FileInputStream> ar = new ArrayList<FileInputStream>();
    static SequenceInputStream seq;
    static DataOutputStream fos;
    int temp;
    static int counter = 1;
    ProgressDialog barProgressDialog;
    ProgressDialog progress;


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
        MergeButton = v.findViewById(R.id.mergebutton);

        songRepository = new SongRepository(getActivity().getApplicationContext());
        loadSongsFromDB();

        adapter = new RecyclerListAdapter(
                getActivity(),
                this,
                convertSongsToSongList(loadSongsFromDB()));

        if (adapter != null)
            songList.setAdapter(adapter);


        songList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(songList);

        AddButton.setOnClickListener(this);
        MergeButton.setOnClickListener(this);

        return v;
    }

    private List<SongList> convertSongsToSongList(List<Song> songList) {
        List<SongList> converted = new ArrayList<>();
        for (Song song : songList) {
            SongList aSongList = new SongList(song.getSongID(), song.getSongLocation());
            converted.add(aSongList);
        }
        return converted;
    }

    private List<Song> loadSongsFromDB() {
        List<Song> songsFromRepo = new ArrayList<>();
        FetchSongFromRepo fetchTask = new FetchSongFromRepo();
        try {
            songsFromRepo = fetchTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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

        if (v == AddButton) {

            if (checkPermissionForReadExtertalStorage()) {
                Intent AddSong = new Intent(getActivity(), SelectSongActivity.class);
                startActivityForResult(AddSong, 1);
                //startActivity(AddSong);
            } else {
                try {
                    requestPermissionForReadExtertalStorage();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Permission Issue", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (v == MergeButton) {

            List<SongList> s = adapter.getmItems();

            for (SongList d : s) {
                try {
                    ar.add(new FileInputStream(new File(d.getSongLocation())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            /**
             *  File Dialog Implementation
             */
            final Dialog dialog = new Dialog(getActivity());
            // Include dialog.xml file
            dialog.setContentView(R.layout.file_save);
            // Set dialog title
            dialog.setTitle("Please Enter the File Name");
            final EditText fileName = dialog.findViewById(R.id.filenameedit);
            Button saveFinal = dialog.findViewById(R.id.mergersave);
            saveFinal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (fileName.getText().toString().length() > 1 && ar.size() > 1) {
                        dialog.dismiss();
                        mergerService(ar, fileName.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Invalid Operation", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        loadSongsFromDB();

        adapter = new RecyclerListAdapter(
                getActivity(),
                this,
                convertSongsToSongList(loadSongsFromDB()));
        if (adapter != null)
            songList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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

    private class FetchSongFromRepo extends AsyncTask<String, Void, List<Song>> {
        @Override
        protected List<Song> doInBackground(String... params) {
            return songRepository.getAllSongs();
        }
    }


    protected void mergerService(List<FileInputStream> ar2, String length) {
        // TODO Auto-generated method stub

        final String res = Environment.getExternalStorageDirectory() + "/videotomusic/" + length + ".mp3";
        final Iterator<FileInputStream> it = ar2.iterator();

        Enumeration<DataInputStream> en = new Enumeration<DataInputStream>() {

            public boolean hasMoreElements() {
                return it.hasNext();
            }

            public DataInputStream nextElement() {
                return new DataInputStream(new BufferedInputStream(it.next()));
            }
        };
        seq = new SequenceInputStream(en);

        try {
            fos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(res)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        new AsyncTask<Integer, Void, Void>() {
            protected void onPreExecute() {
                progress = new ProgressDialog(getActivity());
                progress.setMessage("Merging..Please wait!");
                progress.setCancelable(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Integer... params) {
                // TODO Auto-generated method stub

                try {
                    while ((temp = seq.read()) != -1) {
                        fos.write(temp);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                progress.dismiss();
                counter = 0;
                ar.clear();


                new AlertDialog.Builder(getActivity())
                        .setTitle("Your File Has been Successfully Created")
                        .setMessage("The Output file can be found in the memory card in the folder named Mp3Editor in the memory card")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Rate Us", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dija.songmerge.songmerger")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.dija.songmerge.songmerger")));
                                }
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .show();
            }


        }.execute();


    }


}
