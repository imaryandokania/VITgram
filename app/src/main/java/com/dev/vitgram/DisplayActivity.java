package com.dev.vitgram;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    public MediaPlayer mp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        mp1 = MediaPlayer.create(DisplayActivity.this,R.raw.mu);
        mp1.start();
        mp1.setVolume(100,100);
        imageView.animate().alpha(1).setDuration(3000);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mp1.stop();

                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
