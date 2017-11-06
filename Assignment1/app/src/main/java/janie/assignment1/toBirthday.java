package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class toBirthday extends AppCompatActivity {
    private String sender, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_birthday);
        setTitle("Set contents");

        Log.i(".toBirthday", "Birthday card chosen.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(".toBirthday", "Birthday card customisation started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(".toBirthday", "Birthday card customisation paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(".toBirthday", "Birthday card customisation resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(".toBirthday", "Birthday card customisation stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(".toBirthday", "Birthday card customisation restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(".toBirthday", "Birthday card customisation destroyed");
    }

    public void Birthday(View view) {
        EditText nameInput = findViewById(R.id.sender);
        sender = nameInput.getText().toString();
        EditText messageInput = findViewById(R.id.message);
        message = messageInput.getText().toString();

        Intent intent = new Intent(this, Birthday.class);
        intent.putExtra("Name", sender);
        intent.putExtra("Message", message);
        startActivity(intent);
    }
}
