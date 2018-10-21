package tw.brad.servicetest1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer != null) return;

        Log.v("brad", "init");
        mediaPlayer = MediaPlayer.create(this, R.raw.brad);
        int len = mediaPlayer.getDuration();
        Intent intent = new Intent("brad");
        intent.putExtra("len", len);
        sendBroadcast(intent);

        timer = new Timer();
        timer.schedule(new MyTask(), 0, 100);
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Intent intent = new Intent("brad");
                intent.putExtra("now", mediaPlayer.getCurrentPosition());
                sendBroadcast(intent);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isStart = intent.getBooleanExtra("start", false);
        int seekto = intent.getIntExtra("seekto", -1);

        if (seekto != -1 && mediaPlayer !=null){
            mediaPlayer.seekTo(seekto);
        }else if (isStart){
            if (!mediaPlayer.isPlaying()) {
                Log.v("brad", "pause => start");
                mediaPlayer.start();
            }
        }else{
            Log.v("brad", "pause");
            if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("brad");
        intent.putExtra("now", 0);
        sendBroadcast(intent);

        Log.v("brad", "die");
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
