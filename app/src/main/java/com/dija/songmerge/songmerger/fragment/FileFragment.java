package com.dija.songmerge.songmerger.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dija.songmerge.songmerger.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    public FileFragment() {
        // Required empty public constructor
    }

    private static final int EXIT = 0;
    private static final int STOP = 1;
    private static final String DIRECTORY = "/mnt/sdcard/songmerger";
    private MediaPlayer mp = new MediaPlayer();
    List<String> Ringtones = new ArrayList<String>();
    Boolean hasErrors = false;
    int currentPosition = 0;
    SwipeRefreshLayout swipeContainer;
    ListView lv;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_file, container, false);

        lv = v.findViewById(R.id.listfiles);


        Ringtones = getFiles();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                Ringtones);

        lv.setAdapter(arrayAdapter);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call the send request method to call the url
                refreshRequest();
                swipeContainer.setRefreshing(false);
            }
        });

        // setting color scheme for the swipes
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lv.setOnItemClickListener(this);

        return v;
    }

    private void refreshRequest() {
        Ringtones.clear();
        Ringtones = getFiles();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                Ringtones);
        lv.setAdapter(arrayAdapter);
    }



    private List<String> getFiles() {

        File folder = new File(DIRECTORY);

        if(!folder.exists())
            folder.mkdir();

        List<String> list = new ArrayList<>();

        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                list.add(listOfFiles[i].getName());
            }
        }
        return list;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playAudioFile(DIRECTORY + "/" + Ringtones.get(position));
    }

    private void playAudioFile(String s) {

        Intent j = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        j.setDataAndType(Uri.parse(s), "video/mp4");
        startActivity(j);
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
}
