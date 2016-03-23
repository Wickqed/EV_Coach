package com.openxc.openxcstarter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.openxcplatform.openxcstarter.R;

public class BreakdownActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        double speedScore = (double) getIntent().getSerializableExtra("AccScore");
        double RPMscore = (double) getIntent().getSerializableExtra("RPMScore");;
        double totalScore = (double) getIntent().getSerializableExtra("totalScore");;
        String Grade = (String) getIntent().getSerializableExtra("grade");;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_layout);
        //rest of the code

        TextView accelScore = (TextView) findViewById(R.id.acceleration);
        accelScore.setText(speedScore + " / 333");

        TextView RPMScore = (TextView) findViewById(R.id.rpm);
        RPMScore.setText(RPMscore + " / 334");

        TextView grade = (TextView) findViewById(R.id.Grade);
        grade.setText(Grade);

        TextView score = (TextView) findViewById(R.id.score);
        score.setText(totalScore + " / 1000");
    }
}
