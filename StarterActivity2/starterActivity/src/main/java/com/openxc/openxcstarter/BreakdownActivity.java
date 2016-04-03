package com.openxc.openxcstarter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.openxcplatform.openxcstarter.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BreakdownActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_layout);
        NumberFormat format = new DecimalFormat("#0.00");

        Bundle extras = getIntent().getExtras();
        String speedScore = extras.getString("SpeedScore");
        String RPMscore = extras.getString("RPMScore");
        String accelScore = extras.getString("AccScore");
        String totalScore = extras.getString("totalScore");
        String Grade = extras.getString("grade");

        //rest of the code
        TextView accelScoreText = (TextView) findViewById(R.id.acceleration);
        accelScoreText.setText(format.format(accelScore) + " / 333");

        TextView RPMScore = (TextView) findViewById(R.id.rpm);
        RPMScore.setText(format.format(RPMscore) + " / 333");

        TextView grade = (TextView) findViewById(R.id.grade);
        grade.setText(Grade);

        TextView speed = (TextView) findViewById(R.id.speed);
        speed.setText(format.format(speedScore) + " / 334");

        TextView score = (TextView) findViewById(R.id.score);
        score.setText(format.format(totalScore) + " / 1000");
    }
}