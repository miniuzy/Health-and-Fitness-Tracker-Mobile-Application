package com.example.mohammeduzair.androidapp.ScndPage;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.utility.MusicService;
import com.example.mohammeduzair.androidapp.utility.Song;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import com.example.mohammeduzair.androidapp.R;
import com.example.mohammeduzair.androidapp.utility.BotNavHelp;
import com.example.mohammeduzair.androidapp.utility.SongAdapter;
import com.example.mohammeduzair.androidapp.utility.savepref;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class musicplayer extends AppCompatActivity implements MediaPlayerControl {

    private static final int NUM = 1;
    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController musicController;
    private boolean paused=false, playbackPaused=false;
    savepref savePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new savepref(this);
        if(savePref.loadDarkTheme()==true){
            setTheme(R.style.darktheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        setupBottomNavView();
        setMusicController();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                return;
            }}

        songView = (ListView)findViewById(R.id.song_list);


        songList = new ArrayList<Song>();


        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);


        getSongList();
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    // music player is hidden tap to pop it up for controls as showing music player from the start was creating an error
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        musicController.show();
        return false;
    }

    private void setupBottomNavView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavBar);
        BotNavHelp.setupBottomNavView(bottomNavigationViewEx);
        BotNavHelp.enableNav(musicplayer.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(NUM);
        menuItem.setChecked(true);
    }
    //connecting to the music service class created
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicService = binder.getService();
            musicService.setList(songList);
            musicBound = true;
//            musicController.show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
            stopService(playIntent);
            musicService=null;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    public void getSongList() {
        //retrieve song information from device using Uri and ContentResolver
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shufflemenu, menu);
        return true;
    }
    //Creating a button in action bar to allow shuffling
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicService.setShuffle();
                Toast.makeText(this, "Shuffling playlist", Toast.LENGTH_LONG).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void songPicked(View view){
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playSong();
    }

    @Override
    protected void onDestroy() {
        if (musicBound) unbindService(musicConnection);
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
    }
    private void setMusicController(){
        //Creating and placing the music player
        musicController = new MusicController(this);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.musicplayerhere));
        musicController.setEnabled(true);
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getPosn();
        else return 0;
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    //play next
    private void playNext(){
        musicService.playNext();
    }

    private void playPrev(){
        musicService.playPrev();
    }
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

}
