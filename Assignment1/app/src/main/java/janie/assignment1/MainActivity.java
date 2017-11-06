package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(".MainActivity", "Application started");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(".MainActivity", "Application started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(".MainActivity", "Application stopped");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(".MainActivity", "Application paused");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity", "Apllication restarted");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(".MainActivity", "Application resumed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(".MainActivity", "Application destroyed");
    }

    public void setBirthday(View view) {
        Intent intent = new Intent(this, toBirthday.class);
        startActivity(intent);
    }

    public void setParty(View view) {
        Intent intent = new Intent(this, TimeParty.class);
        startActivity(intent);
    }
}
