package com.openxc.openxcstarter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openxcplatform.openxcstarter.R;


public class CoachFragment extends Fragment {

    private int rpmScore;
    private int speedScore;
    private int fuelScore;
    private int totalScore;
    private SharedPreferences sharedPreferences;

    //TODO - Make sure the correct integers are bundled up before swapping fragments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coaching, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle bundle = getArguments();
        rpmScore = bundle.getInt("rpmScore");
        speedScore = bundle.getInt("speedScore");
        fuelScore = bundle.getInt("fuelScore");
        totalScore = rpmScore + speedScore + fuelScore;

        String rpmGrade = calculateGrade(rpmScore * 3);
        String speedGrade = calculateGrade(speedScore * 3);
        String fuelGrade = calculateGrade(fuelScore * 3);
        String totalGrade = calculateGrade(totalScore);

        double rpmPercent = 100 * (rpmScore / 333.0);
        double speedPercent = 100 * (speedScore / 333.0);
        double fuelPercent = 100 * (fuelScore / 333.0);
        double totalPercent = 100 * (totalScore / 1000.0);

        //Find the TextViews
        TextView totalScoreView = (TextView) rootView.findViewById(R.id.coach_totalScore);
        TextView totalGradeView = (TextView) rootView.findViewById(R.id.coach_grade);
        TextView rpmScoreView = (TextView) rootView.findViewById(R.id.coach_rpmScore);
        TextView rpmGradeView = (TextView) rootView.findViewById(R.id.coach_rpmGrade);
        TextView rpmMessageView = (TextView) rootView.findViewById(R.id.coach_rpmContents);
        TextView speedScoreView = (TextView) rootView.findViewById(R.id.coach_speedScore);
        TextView speedGradeView = (TextView) rootView.findViewById(R.id.coach_speedGrade);
        TextView speedMessageView = (TextView) rootView.findViewById(R.id.coach_speedContents);
        TextView fuelScoreView = (TextView) rootView.findViewById(R.id.coach_gasScore);
        TextView fuelGradeView = (TextView) rootView.findViewById(R.id.coach_gasGrade);
        TextView fuelMessageView = (TextView) rootView.findViewById(R.id.coach_gasContents);

        //Update the TextViews
        totalScoreView.append(totalGrade + " ");
        totalGradeView.setText(Double.toString(totalPercent));
        rpmScoreView.append(rpmGrade + " ");
        rpmGradeView.setText(Double.toString(rpmPercent));
        speedScoreView.append(speedGrade + " ");
        speedGradeView.setText(Double.toString(speedPercent));
        fuelScoreView.append(fuelGrade + " ");
        fuelGradeView.setText(Double.toString(fuelPercent));

        //Display messages
        //TODO - Move these into the strings.xml file
        String rpmMessage = "Try to not accelerate as rapidly when driving.";
        String speedMessage;
        String fuelMessage = "Make sure you have a fully charged battery, and to keep your speed and RPMs low";

        if(Integer.parseInt(sharedPreferences.getString("road", "0")) == 2) {
            speedMessage = "Try slowing down when traveling on the highway.";
            rpmMessage = "Try using cruise control when traveling on the highway.";
        } else if(Integer.parseInt(sharedPreferences.getString("road", "0")) == 1) {
            speedMessage = "Try slowing down when traveling on rural roads.";
        } else {
            speedMessage = "Try slowing down when traveling through the city.";
        }

        if(rpmPercent < 80.0) {
            rpmMessageView.setText(rpmMessage);
        } else {
            rpmMessageView.setText("Good job!");
        }

        if(fuelPercent < 80.0) {
            fuelMessageView.setText(fuelMessage);
        } else {
            fuelMessageView.setText("Good job!");
        }

        if(speedPercent < 80.0) {
            speedMessageView.setText(speedMessage);
        } else {
            speedMessageView.setText("Good job!");
        }


        return rootView;
    }


    private String calculateGrade(int score) {

        if(score < 500) {
            return "F";
        } else if(score < 600) {
            return "E";
        } else if(score < 625) {
            return "D-";
        } else if(score < 675) {
            return "D";
        } else if(score < 700) {
            return "D+";
        } else if(score < 725) {
            return "C-";
        } else if(score < 775) {
            return "C";
        } else if(score < 800) {
            return "C+";
        } else if(score < 825) {
            return "B-";
        } else if(score < 875) {
            return "B";
        } else if(score < 900) {
            return "B+";
        } else if(score < 925) {
            return "A-";
        } else if(score < 975) {
            return "A";
        } else {
            return "A+";
        }
    }
}
