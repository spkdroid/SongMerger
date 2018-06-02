package com.dija.songmerge.songmerger;



/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dija.songmerge.songmerger.soundfile.CheapSoundFile;

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


public class RingMergeActivity
        extends ListActivity
        implements TextWatcher, OnClickListener {
    private TextView mFilter;
    private SimpleCursorAdapter mAdapter;
    private boolean mWasGetContentIntent;
    private boolean mShowAll;

    // Result codes
    private static final int REQUEST_CODE_EDIT = 1;
    private static final int REQUEST_CODE_CHOOSE_CONTACT = 2;

    // Menu commands
    private static final int CMD_ABOUT = 1;
    private static final int CMD_PRIVACY = 2;
    private static final int CMD_SHOW_ALL = 3;

    // Context menu
    private static final int CMD_EDIT = 4;
    private static final int CMD_DELETE = 5;
    private static final int CMD_SET_AS_DEFAULT = 6;
    private static final int CMD_SET_AS_CONTACT = 7;
    String filename, countname;


    static List<FileInputStream> ar = new ArrayList<FileInputStream>();
    static SequenceInputStream seq;
    static DataOutputStream fos;
    int temp;
    static int counter = 1;
    ProgressDialog barProgressDialog;
    ProgressDialog progress;

    TextView dashboard;
    Button Next, Reset;

    public RingMergeActivity() {
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mShowAll = false;

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            showFinalAlert(getResources().getText(R.string.sdcard_readonly));
            return;
        }
        if (status.equals(Environment.MEDIA_SHARED)) {
            showFinalAlert(getResources().getText(R.string.sdcard_shared));
            return;
        }
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            showFinalAlert(getResources().getText(R.string.no_sdcard));
            return;
        }


        // Inflate our UI from its XML layout description.
        setContentView(R.layout.merger_menu_select);


        File folder = new File(Environment.getExternalStorageDirectory() + "/mp3editor");
        boolean success = true;
        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = folder.mkdir();
        }
        if (success) {
            //            Toast.makeText(this, "Directory Created", Toast.LENGTH_SHORT).show();
        } else {
            //            Toast.makeText(this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }


        dashboard = (TextView) findViewById(R.id.counterText);

        Next = (Button) findViewById(R.id.bottombutton1);
        Reset = (Button) findViewById(R.id.bottombutton2);

        Next.setOnClickListener(this);
        Reset.setOnClickListener(this);

        try {
            mAdapter = new SimpleCursorAdapter(
                    this,
                    // Use a template that displays a text view
                    R.layout.media_select_row,
                    // Give the cursor to the list adatper
                    createCursor(""),
                    // Map from database columns...
                    new String[]{
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.TITLE,
                    },
                    // To widget ids in the row layout...
                    new int[]{
                            R.id.row_artist,
                            R.id.row_album,
                            R.id.row_title,
                    }, 0);

            setListAdapter(mAdapter);

            getListView().setItemsCanFocus(true);

            // Normal click - open the editor
            getListView().setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView parent,
                                        View view,
                                        int position,
                                        long id) {


                    //           startRingdroidEditor();
                    //           int fullpath = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    //        filename=cursor.getString(fullpath);
         
                	
                	
              /*
                 Cursor c = mAdapter.getCursor();
              int title = c.getColumnIndex(MediaStore.Audio.Media.DATA);
              String filename=c.getString(title);
       	         
                	Toast.makeText(getApplicationContext(),filename, 2000).show();
                	
               if(filename==null)
        			{
        				  Toast.makeText(getApplicationContext(),"No File has been chosen!!!!!",100).show();				
        			}
        			else
        			{
        			  Toast.makeText(getApplicationContext(),filename+" has been chosen 1",100).show();
        			 finish();
        			  Intent myIntent = new Intent(getApplicationContext(),join.class);
        		
        			  myIntent.putExtra("keyName", filename);	
        			  myIntent.putExtra("keyName1",countname);
        			  startActivityForResult(myIntent,0);	
                }
                */
                    Cursor d = mAdapter.getCursor();
                    int title1 = d.getColumnIndex(MediaStore.Audio.Media.DATA);
                    String filename1 = d.getString(title1);

                    filename1 = filename1.substring(filename1.lastIndexOf("/") + 1, filename1.length());

                    new AlertDialog.Builder(RingMergeActivity.this)
                            .setIcon(R.drawable.icon)
                            .setTitle("Shall I Add This Song Into The Buffer.")
                            .setMessage("The Name of the Song to be added into the buffer \n\n" + filename1 + " \n\n\n\n Current Buffer size: " + (ar.size() + 1))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Cursor c = mAdapter.getCursor();
                                    int title = c.getColumnIndex(MediaStore.Audio.Media.DATA);
                                    String filename = c.getString(title);
                                    Toast.makeText(getApplicationContext(), "File " + (ar.size() + 1) + " has been added to the buffer", Toast.LENGTH_SHORT).show();
                                    dashboard.setText("Start Merging " + (ar.size() + 1) + " file has been in the buffer");
                                    try {
                                        ar.add(new FileInputStream(filename));
                                    } catch (FileNotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        } catch (SecurityException e) {
            // No permission to retrieve audio?

            // todo error 1
        } catch (IllegalArgumentException e) {
            // No permission to retrieve audio?

            // todo error 2
        }

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view,
                                        Cursor cursor,
                                        int columnIndex) {


                return false;
            }
        });

        // Long-press opens a context menu
        registerForContextMenu(getListView());

        mFilter = (TextView) findViewById(R.id.search_filter);
        if (mFilter != null) {
            mFilter.addTextChangedListener(this);
        }
    }

    private void setSoundIconFromCursor(ImageView view, Cursor cursor) {
    }

    /**
     * Called with an Activity we started with an Intent returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent dataIntent) {
        if (requestCode != REQUEST_CODE_EDIT) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        setResult(RESULT_OK, dataIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(CMD_ABOUT).setVisible(true);
        menu.findItem(CMD_PRIVACY).setVisible(true);
        menu.findItem(CMD_SHOW_ALL).setVisible(true);
        menu.findItem(CMD_SHOW_ALL).setEnabled(!mShowAll);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CMD_ABOUT:
                //   RingdroidEditActivity.onAbout(this);
                return true;
            case CMD_PRIVACY:
                showPrivacyDialog();
                return true;
            case CMD_SHOW_ALL:
                mShowAll = true;
                refreshListView();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Cursor c = mAdapter.getCursor();
        String title = c.getString(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.TITLE));

        menu.setHeaderTitle(title);

        menu.add(0, CMD_EDIT, 0, R.string.context_menu_edit);
        menu.add(0, CMD_DELETE, 0, R.string.context_menu_delete);

        // Add items to the context menu item based on file type
        if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_RINGTONE))) {
            menu.add(0, CMD_SET_AS_DEFAULT, 0, R.string.context_menu_default_ringtone);
            menu.add(0, CMD_SET_AS_CONTACT, 0, R.string.context_menu_contact);
        } else if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_NOTIFICATION))) {
            menu.add(0, CMD_SET_AS_DEFAULT, 0, R.string.context_menu_default_notification);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info =
                (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CMD_EDIT:
                startRingdroidEditor();
                return true;
            case CMD_DELETE:
                confirmDelete();
                return true;
            case CMD_SET_AS_DEFAULT:
                setAsDefaultRingtoneOrNotification();
                return true;
            case CMD_SET_AS_CONTACT:
                return chooseContactForRingtone(item);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showPrivacyDialog() {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, Uri.parse(""));
            intent.putExtra("privacy", true);
            intent.setClassName("com.ringdroid",
                    "com.ringdroid.RingdroidEditActivity");
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't show privacy dialog");
        }
    }

    private void setAsDefaultRingtoneOrNotification() {
        Cursor c = mAdapter.getCursor();

        // If the item is a ringtone then set the default ringtone, 
        // otherwise it has to be a notification so set the default notification sound
        if (0 != c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_RINGTONE))) {
            RingtoneManager.setActualDefaultRingtoneUri(
                    RingMergeActivity.this,
                    RingtoneManager.TYPE_RINGTONE,
                    getUri());
            Toast.makeText(
                    RingMergeActivity.this,
                    R.string.default_ringtone_success_message,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            RingtoneManager.setActualDefaultRingtoneUri(
                    RingMergeActivity.this,
                    RingtoneManager.TYPE_NOTIFICATION,
                    getUri());
            Toast.makeText(
                    RingMergeActivity.this,
                    R.string.default_notification_success_message,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private Uri getUri() {
        //Get the uri of the item that is in the row
        Cursor c = mAdapter.getCursor();
        int uriIndex = c.getColumnIndex(
                "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\"");
        if (uriIndex == -1) {
            uriIndex = c.getColumnIndex(
                    "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\"");
        }
        String itemUri = c.getString(uriIndex) + "/" +
                c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        return (Uri.parse(itemUri));
    }

    private boolean chooseContactForRingtone(MenuItem item) {
        try {
            //Go to the choose contact activity
            Intent intent = new Intent(Intent.ACTION_EDIT, getUri());
            intent.setClassName(
                    "com.ringdroid",
                    "com.ringdroid.ChooseContactActivity");
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_CONTACT);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't open Choose Contact window");
        }
        return true;
    }

    private void confirmDelete() {
        // See if the selected list item was created by Ringdroid to
        // determine which alert message to show
        Cursor c = mAdapter.getCursor();
        int artistIndex = c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.ARTIST);
        String artist = c.getString(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.ARTIST));
        CharSequence ringdroidArtist =
                getResources().getText(R.string.artist_name);

        CharSequence message;
        if (artist.equals(ringdroidArtist)) {
            message = getResources().getText(
                    R.string.confirm_delete_ringdroid);
        } else {
            message = getResources().getText(
                    R.string.confirm_delete_non_ringdroid);
        }

        CharSequence title;
        if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_RINGTONE))) {
            title = getResources().getText(R.string.delete_ringtone);
        } else if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_ALARM))) {
            title = getResources().getText(R.string.delete_alarm);
        } else if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_NOTIFICATION))) {
            title = getResources().getText(R.string.delete_notification);
        } else if (0 != c.getInt(c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.IS_MUSIC))) {
            title = getResources().getText(R.string.delete_music);
        } else {
            title = getResources().getText(R.string.delete_audio);
        }

        new AlertDialog.Builder(RingMergeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.delete_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onDelete();
                            }
                        })
                .setNegativeButton(
                        R.string.delete_cancel_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setCancelable(true)
                .show();
    }

    private void onDelete() {
        Cursor c = mAdapter.getCursor();
        int dataIndex = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        String filename = c.getString(dataIndex);

        int uriIndex = c.getColumnIndex(
                "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\"");
        if (uriIndex == -1) {
            uriIndex = c.getColumnIndex(
                    "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\"");
        }
        if (uriIndex == -1) {
            showFinalAlert(getResources().getText(R.string.delete_failed));
            return;
        }

        if (!new File(filename).delete()) {
            showFinalAlert(getResources().getText(R.string.delete_failed));
        }

        String itemUri = c.getString(uriIndex) + "/" +
                c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        getContentResolver().delete(Uri.parse(itemUri), null, null);
    }

    private void showFinalAlert(CharSequence message) {
        new AlertDialog.Builder(RingMergeActivity.this)
                .setTitle(getResources().getText(R.string.alert_title_failure))
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    private void onRecord() {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT,
                    Uri.parse("record"));
            intent.putExtra("was_get_content_intent",
                    mWasGetContentIntent);
            intent.setClassName(
                    "com.ringdroid",
                    "com.ringdroid.RingdroidEditActivity");
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't start editor");
        }
    }

    private void startRingdroidEditor() {
        Cursor c = mAdapter.getCursor();
        int dataIndex = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        String filename = c.getString(dataIndex);
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT,
                    Uri.parse(filename));
            intent.putExtra("was_get_content_intent",
                    mWasGetContentIntent);
            intent.setClassName(
                    "com.ringdroid",
                    "com.ringdroid.RingdroidEditActivity");
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't start editor");
        }
    }

    private Cursor getInternalAudioCursor(String selection,
                                          String[] selectionArgs) {
        return managedQuery(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                INTERNAL_COLUMNS,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    private Cursor getExternalAudioCursor(String selection,
                                          String[] selectionArgs) {
        return managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                EXTERNAL_COLUMNS,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    Cursor createCursor(String filter) {
        ArrayList<String> args = new ArrayList<String>();
        String selection;

        if (mShowAll) {
            selection = "(_DATA LIKE ?)";
            args.add("%");
        } else {
            selection = "(";
            for (String extension : CheapSoundFile.getSupportedExtensions()) {
                args.add("%." + extension);
                if (selection.length() > 1) {
                    selection += " OR ";
                }
                selection += "(_DATA LIKE ?)";
            }
            selection += ")";

            selection = "(" + selection + ") AND (_DATA NOT LIKE ?)";
            args.add("%espeak-data/scratch%");
        }

        if (filter != null && filter.length() > 0) {
            filter = "%" + filter + "%";
            selection =
                    "(" + selection + " AND " +
                            "((TITLE LIKE ?) OR (ARTIST LIKE ?) OR (ALBUM LIKE ?)))";
            args.add(filter);
            args.add(filter);
            args.add(filter);
        }

        String[] argsArray = args.toArray(new String[args.size()]);

        Cursor external = getExternalAudioCursor(selection, argsArray);
        Cursor internal = getInternalAudioCursor(selection, argsArray);

        Cursor c = new MergeCursor(new Cursor[]{
                getExternalAudioCursor(selection, argsArray),
                getInternalAudioCursor(selection, argsArray)});
        startManagingCursor(c);
        return c;
    }

    public void beforeTextChanged(CharSequence s, int start,
                                  int count, int after) {
    }

    public void onTextChanged(CharSequence s,
                              int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        refreshListView();
    }

    private void refreshListView() {
        String filterStr = mFilter.getText().toString();
        mAdapter.changeCursor(createCursor(filterStr));
    }

    private static final String[] INTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\""
    };

    private static final String[] EXTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\""
    };


    public void onBackPressed() {
        finish();
        //	Intent i=new Intent(getApplicationContext(),Mp3Editor.class);
        //	startActivity(i);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    protected void mergerService(List<FileInputStream> ar2, String length) {
        // TODO Auto-generated method stub

        final String res = Environment.getExternalStorageDirectory() + "/mp3editor/" + length + ".mp3";
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
                progress = new ProgressDialog(RingMergeActivity.this);
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


                new AlertDialog.Builder(RingMergeActivity.this)
                        .setTitle("Your File Has been Successfully Created")
                        .setMessage("The Output file can be found in the memory card in the folder named Mp3Editor in the memory card")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        })
                        .setNegativeButton("Rate Us", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=my.me.dija.mp3editor")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=my.me.dija.mp3editor")));
                                }
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .show();
            }


//			      finish();
//			      Intent i=new Intent(getApplicationContext(),Mp3Editor.class);
//	              startActivity(i);

        }.execute();
    }
}