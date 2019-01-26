package com.commandus.voiceecho;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextToSpeech.OnInitListener
{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabVoice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTest();
            }
        });

        mTTS = new TextToSpeech(this, this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private static android.speech.tts.TextToSpeech mTTS;

    /**
     * Say phrase
     * @param value phrase to say
     * @param language language identifier e.g. "en"
     * @return true if TTS ie enabled
     */
    private void say(String value, String language) {
        if (mTTS != null) {
            Locale loc = new Locale(language);
            mTTS.setLanguage(loc);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle alarm = new Bundle();
                alarm.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_ALARM);
                mTTS.speak(value, TextToSpeech.QUEUE_ADD, alarm, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            } else {
                HashMap<String, String> alarm = new HashMap<>();
                alarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                mTTS.speak(value, TextToSpeech.QUEUE_ADD, alarm);
            }
            return;
        }
        // missing data, install it
        installTTS(this);
    }

    /**
     * Show dialog to install TTS
     * @param context    Application context
     */
    private static void installTTS(Context context) {
        // missing data, install it
        Intent installIntent = new Intent();
        installIntent.setAction(
                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        context.startActivity(installIntent);
    }

    private void doTest() {
        //say("Attention. Emergency. All personnel must evacuate immediately. You now have 4 minutes to reach minimum safe distance.", "en");
        say("Автобус по маршруту 101 Якутск - Маган отправляется через 10 минут с платформы номер 5.", "ru");
    }

    @Override
    public void onInit(int status) {
        if ((status == TextToSpeech.SUCCESS) && (mTTS != null)) {
            Log.i(TAG, "TTS started successfully.");
        } else {
            mTTS = null;
            Log.e(TAG, "TTS is not started. Status = " + Integer.toString(status));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTTS != null) {
            mTTS.shutdown();
            mTTS = null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            installTTS(this);
        } else if (id == R.id.nav_gallery) {
            installTTS(this);
        } else if (id == R.id.nav_slideshow) {
            installTTS(this);
        } else if (id == R.id.nav_manage) {
            installTTS(this);
        } else if (id == R.id.nav_share) {
            installTTS(this);
        } else if (id == R.id.nav_send) {
            installTTS(this);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
