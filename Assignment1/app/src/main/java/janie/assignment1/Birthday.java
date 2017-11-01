package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Birthday extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        setTitle("");

        Intent Extra = getIntent();
        String sender = Extra.getStringExtra("Name");
        String message = Extra.getStringExtra("Message");

        TextView messageDisplay = (TextView) findViewById(R.id.message);
        TextView nameDisplay = (TextView) findViewById(R.id.sender);

        messageDisplay.setText(message);
        nameDisplay.setText("From " + sender);
    }
}
