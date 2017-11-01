package janie.monitorlifecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "now running onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "now running onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "now running onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "now running onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity", "now running onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "now running onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "now running onDestroy");
    }
}
