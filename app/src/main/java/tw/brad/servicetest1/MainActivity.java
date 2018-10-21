package tw.brad.servicetest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private MyReceiver myReceiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiver = new MyReceiver();
        filter = new IntentFilter();
        filter.addAction("brad");

        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void seekTo(int seekto){
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("seekto", seekto);
        startService(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(myReceiver, filter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    public void test1(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("start", true);
        startService(intent);
    }
    public void test2(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("start", false);
        startService(intent);
    }
    public void test3(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int len = intent.getIntExtra("len", -1);
            int now = intent.getIntExtra("now", -1);
            if (len != -1){
                seekBar.setMax(len);
            }else if (now != -1){
                seekBar.setProgress(now);
            }



        }
    }

}
