package janie.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Float.parseFloat;

public class MoneyActivity extends AppCompatActivity {
    private Float result = new Float(0);
    private Float operand = new Float(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
    }

    public void toUSD(View view) {
        EditText amount = (EditText) findViewById(R.id.amount);
        operand = parseFloat(amount.getText().toString());
        result = operand/22727.27f;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(result.toString());
    }

    public void toVND(View view) {
        EditText amount = (EditText) findViewById(R.id.amount);
        operand = parseFloat(amount.getText().toString());
        result = operand * 22727.27f;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(result.toString());
    }
}
