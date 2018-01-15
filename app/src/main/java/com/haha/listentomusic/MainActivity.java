package com.haha.listentomusic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.haha.listentomusic.R.id.btnPlay;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AlarmManager alarmManager;

    ArrayList<Songs> arraySong;
    private ImageButton mBtnPlay,mBtnNext,mBtnPrevious;
    private TextView mTxtTimeEnd,mTxtTimeStart;
    private SeekBar mSeekBar;
    int position = 0;
    MediaPlayer mediaPlayer;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        initView();
        addSong();
        initMedia();
        mBtnPlay.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

    }

    private void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Songs(R.raw.chuctet));
        arraySong.add(new Songs(R.raw.ngaytetqueem));
        arraySong.add(new Songs(R.raw.tetnguyendan));
    }

    private void initView() {
        mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        mBtnPlay = (ImageButton) findViewById(btnPlay);
        mBtnPrevious = (ImageButton) findViewById(R.id.btnPerious);
        mTxtTimeEnd = (TextView) findViewById(R.id.txtTimeEnd);
        mTxtTimeStart = (TextView) findViewById(R.id.txtTimeStart);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    @Override
    public void onClick(View view) {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(mSeekBar.getProgress());
            }
        });
        switch (view.getId()){
            case R.id.btnPlay :
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    mBtnPlay.setImageResource(R.drawable.play);

                }else {
                    initMedia();
                    mediaPlayer.start();
                    mBtnPlay.setImageResource(R.drawable.pause);
                    setTimeEnd();
                    updateTimeSong();
                }

               break;
            case R.id.btnNext :
                position++;
                if (position>arraySong.size()-1){
                    position = 0;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                initMedia();
                mediaPlayer.start();
                mBtnPlay.setImageResource(R.drawable.pause);
                setTimeEnd();
                updateTimeSong();
                break;
            case R.id.btnPerious :
                position--;
                if (position<0){
                    position = arraySong.size() -1;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                initMedia();
                mediaPlayer.start();
                mBtnPlay.setImageResource(R.drawable.pause);
                setTimeEnd();
                updateTimeSong();
                break;

        }
    }
    private void initMedia(){
        mediaPlayer = MediaPlayer.create(MainActivity.this,arraySong.
                get(position).getFile());

    }
    private void setTimeEnd(){
        SimpleDateFormat formatHout  = new SimpleDateFormat("mm:ss");
        mTxtTimeEnd.setText(formatHout.format(mediaPlayer.getDuration()));
        mSeekBar.setMax(mediaPlayer.getDuration());
    }

    private  void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat fomatHout = new SimpleDateFormat("mm:ss");
                mTxtTimeStart.setText(fomatHout.format(mediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        },100);
    }
}
