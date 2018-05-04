package com.example.mohammeduzair.androidapp.utility;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;
import android.widget.Toast;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private String songTitle="";
    private boolean shuffle=false;
    private Random rand;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosition;
    private final IBinder musicBind = new MusicBinder();

    public void onCreate(){
        super.onCreate();
        songPosition=0;
        rand=new Random();
        player = new MediaPlayer();
        initMusicPlayer();
    }


    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }
    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }

    public void playSong(){
        player.reset();
        Song playSong = songs.get(songPosition);
        songTitle=playSong.getTitle();
        long currSong = playSong.getID();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
        }

        player.prepareAsync();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

    }

    public void setSong(int songIndex){
        songPosition=songIndex;
    }
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
    public void playPrev(){
        songPosition--;
        if(songPosition<0) songPosition=songs.size()-1;
        playSong();
    }
    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong = songPosition;
            while(newSong==songPosition){
                newSong=rand.nextInt(songs.size());
            }
            songPosition=newSong;
        }
        else{
            songPosition++;
            if(songPosition>=songs.size()) songPosition=0;
        }
        playSong();
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
        player.stop();
        player.release();
    }
}