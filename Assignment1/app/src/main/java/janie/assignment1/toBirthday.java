package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class toBirthday extends AppCompatActivity {
    private String sender, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_birthday);
        setTitle("Set contents");
    }

    public void Birthday(View view) {
        EditText nameInput = (EditText) findViewById(R.id.sender);
        sender = nameInput.getText().toString();
        EditText messageInput = (EditText) findViewById(R.id.message);
        message = messageInput.getText().toString();

        Intent intent = new Intent(this, Birthday.class);
        intent.putExtra("Name", sender);
        intent.putExtra("Message", message);
        startActivity(intent);
    }
}
