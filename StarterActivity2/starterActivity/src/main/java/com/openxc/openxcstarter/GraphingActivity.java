package com.openxc.openxcstarter;


import java.util.ArrayList;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.openxcplatform.openxcstarter.R;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class GraphingActivity extends Activity implements OnItemSelectedListener  {

	// TextViews on activity
	private TextView connection_status;
	private TextView graphView1TextView;
	private TextView mGearPositionView;
	private TextView mFuelLevelView;
	private String name = "name";
	private String value = "value";
	private final int maxPoints = 1000;

	boolean EngineSpeed;
	boolean VehicleSpeed;
	boolean AcCompPowBool;
	boolean BSChargeBool;
	boolean HVBatCurBool;
	boolean LstRegBool;
	boolean RelDrPowBool;

	//GraphView 

	// Variables needed for Speed Graph View
	//private GraphViewData[] speedData;
	//private GraphViewSeries speedSeries;



	//Spinner Variable
	private Spinner canSelect;

	GraphView graph; 
	LineGraphSeries<DataPoint> rpmSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> speedSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> bSCSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> HVBatCurSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> lstRegSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> RelDrPowSeries = new LineGraphSeries<DataPoint>();
	LineGraphSeries<DataPoint> AcCompPowSeries = new LineGraphSeries<DataPoint>();


	/**
	 * Sets up the Arrays passed over from the starter activity in order to use them to graph.
	 *
	 * @param savedInstanceState - the saved instance state of the application
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Dialog dialog = new Dialog(GraphingActivity.this);
		double totalScore = 0; double RPMscore = 0; double speedScore = 0;

		setContentView(R.layout.graphviewlayout);
		graph = (GraphView) findViewById(R.id.graph);
		ArrayList<Double> listRPM = (ArrayList<Double>) getIntent().getSerializableExtra("listRPM");
		ArrayList<Double> listSpeed = (ArrayList<Double>) getIntent().getSerializableExtra("listSpeed");
		
		ArrayList<Double> listBatStateCharge = (ArrayList<Double>) getIntent().getSerializableExtra("listBatStateCharge");
		ArrayList<Double> listHVBatCurr = (ArrayList<Double>) getIntent().getSerializableExtra("listHVBatCurr");
		
		ArrayList<Double> listLastRegEventScore= (ArrayList<Double>) getIntent().getSerializableExtra("listLastRegEventScore");
		ArrayList<Double> listRelDrivePower = (ArrayList<Double>) getIntent().getSerializableExtra("listRelDrivePower");
		ArrayList<Double> listAcCompressorPower = (ArrayList<Double>) getIntent().getSerializableExtra("listAcCompressorPower");

		//Calculate score here and put it into the text box
		RPMscore = calcScore (2500, listRPM, .50);
		Log.i("TAG", "RPMscore is: " + RPMscore);

		speedScore = calcScore(73, listSpeed, .50);
		Log.i("TAG", "speedScore is: " + speedScore);


		//totalScore = totalScore + calcScore (1000, 2500, 4000, listBatStateCharge, .15);
		//totalScore = totalScore + calcScore (1000, 2500, 4000, listHVBatCurr, .15);
		//totalScore = totalScore + calcScore (1000, 2500, 4000, listLastRegEventScore, .15);
		//totalScore = totalScore + calcScore (1000, 2500, 4000, listRelDrivePower, .15);
		//totalScore = totalScore + calcScore (1000, 2500, 4000, listAcCompressorPower, .15);
		
		totalScore = RPMscore + speedScore;
		dialog.setTitle("Score Screen");

		dialog.setContentView(R.layout.userinterface);

		dialog.show();

		TextView textView = (TextView) dialog.findViewById(R.id.Score_Field);
		TextView gradeView = (TextView) dialog.findViewById(R.id.Grade);
		textView.setText(String.valueOf( (int) totalScore));

		Button graphButton = (Button) dialog.findViewById(R.id.Graph_Button);
		Button breakDownButton = (Button) dialog.findViewById(R.id.Break_button);

		if(totalScore >= 900) {
			if(totalScore > 975) { gradeView.setText("A+"); gradeView.setTextColor(Color.GREEN);}
			else if (totalScore <= 975 && totalScore >= 925){ gradeView.setText("A"); gradeView.setTextColor(Color.GREEN);}
			else { gradeView.setText("A-"); gradeView.setTextColor(Color.GREEN);}
		}
		else if(totalScore <= 899 && totalScore >= 800) {
			if(totalScore > 875) { gradeView.setText("B+"); gradeView.setTextColor(Color.GREEN);}
			else if (totalScore <= 875 && totalScore >= 825){ gradeView.setText("B"); gradeView.setTextColor(Color.GREEN);}
			else { gradeView.setText("B-"); gradeView.setTextColor(Color.GREEN);}
		}
		else if(totalScore <= 799 && totalScore >= 700){
			if(totalScore > 775) { gradeView.setText("C+"); gradeView.setTextColor(Color.YELLOW); }
			else if (totalScore <= 775 && totalScore >= 725){ gradeView.setText("C"); gradeView.setTextColor(Color.YELLOW); }
			else { gradeView.setText("C-"); gradeView.setTextColor(Color.YELLOW); }
		}
		else if (totalScore <= 699 && totalScore >= 600) {
			if(totalScore > 675) { gradeView.setText("D+"); gradeView.setTextColor(Color.YELLOW);}
			else if (totalScore <= 675 && totalScore >= 625){ gradeView.setText("D"); gradeView.setTextColor(Color.YELLOW); }
			else { gradeView.setText("D-"); gradeView.setTextColor(Color.YELLOW); }
		}
		else if(totalScore <= 599 && totalScore >= 500) {
			gradeView.setText("E");
			gradeView.setTextColor(Color.RED);
		}
		else {
			gradeView.setText("F");
			gradeView.setTextColor(Color.RED);
		}

		graphButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.cancel();
			}
		});

		breakDownButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "BreakDown", Toast.LENGTH_SHORT).show();
				dialog.cancel();

			}
		});
		
		
		canSelect = (Spinner) findViewById(R.id.canSelect);
		//JSONObject obj = new JSONObject(loadJSONFromAssets());
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.can_signals, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		canSelect.setAdapter(adapter);
		canSelect.setOnItemSelectedListener(this);
		
		
		if (listRPM != null){
			for (int i = 0; i < listRPM.size(); i++){
				rpmSeries.appendData(new DataPoint(i,listRPM.get(i)), true, maxPoints);
			}
		}

		if (listSpeed != null){
			for (int i = 0; i < listSpeed.size(); i++){
				speedSeries.appendData(new DataPoint(i,listSpeed.get(i)), true, maxPoints);
			}
		}	
		if (listBatStateCharge != null){
			for (int i = 0; i < listBatStateCharge.size(); i++){
				bSCSeries.appendData(new DataPoint(i,listBatStateCharge.get(i)), true, maxPoints);
			}
		}

		if (listHVBatCurr != null){
			for (int i = 0; i < listHVBatCurr.size(); i++){
				HVBatCurSeries.appendData(new DataPoint(i,listHVBatCurr.get(i)), true, maxPoints);
			}
		}
		if (listLastRegEventScore != null){
			for (int i = 0; i < listLastRegEventScore.size(); i++){
				lstRegSeries.appendData(new DataPoint(i,listLastRegEventScore.get(i)), true, maxPoints);
			}
		}

		if (listRelDrivePower != null){
			for (int i = 0; i < listRelDrivePower.size(); i++){
				RelDrPowSeries.appendData(new DataPoint(i,listRelDrivePower.get(i)), true, maxPoints);
			}
		}
		if (listAcCompressorPower != null){
			for (int i = 0; i < listAcCompressorPower.size(); i++){
				AcCompPowSeries.appendData(new DataPoint(i,listAcCompressorPower.get(i)), true, maxPoints);
			}
		}
		

		//JSONArray m_jArry = obj.getJSONgoogle.rray("name");
		//ArrayList<HashMap<String,String>>
	}

	//int lowerBase, int midBase,
	private static double calcScore (int upperBase, ArrayList<Double> parameters, double weight){
		double calcScore = 0;
		// normBaseCount = 0 , easyBaseCount = 0, normPct, easyPct,
		int hardBaseCount = 0, zeroCount = 0;
		double zeroPct, acceptPct;

		for ( int i = 0; i < parameters.size(); i++ ) {
			if ( parameters.get(i) > upperBase ) {
				hardBaseCount++;
			}
			//else if (parameters.get(i) > midBase){
			//	normBaseCount++;
			//}
			//else if (parameters.get(i) > lowerBase) {
			//	easyBaseCount++;
			//}
			else {
				zeroCount++;
			}
		}

		//normPct = (double) normBaseCount / (double) parameters.size();
		//easyPct = (double) easyBaseCount / (double) parameters.size();

		Log.i("TAG", "Number over:" + hardBaseCount + " Number below: " + zeroCount);
		//acceptPct = normPct + easyPct + zeroPct;
		acceptPct = (double) zeroCount / parameters.size();
		calcScore = acceptPct * weight * 1000;

		return calcScore;
	}


	/**
	 * Selects the correct graph based on a selection of tiles by the user.
	 *
	 * @param parentView
	 * @param v
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemSelected(AdapterView<?> parentView, View v, int position,
			long id) {

		graph.removeAllSeries();
		EngineSpeed = false;
		VehicleSpeed = false;
		AcCompPowBool = false;
		BSChargeBool = false;
		HVBatCurBool = false;
		LstRegBool = false;
		RelDrPowBool = false;
		
		if(canSelect.getSelectedItem().toString().equals("Vehicle Speed")){
			graph.addSeries(speedSeries);
			graph.setTitle("Vehicle Speed");
			VehicleSpeed = true;
			//Toast.makeText(getApplicationContext(), "vehicle speed selected", Toast.LENGTH_SHORT).show();
		}
		else if(canSelect.getSelectedItem().toString().equals("Engine Speed")){
			graph.addSeries(rpmSeries);
			graph.setTitle("Engine Speed");
			EngineSpeed = true;
			//Toast.makeText(getApplicationContext(), "engine speed selected", Toast.LENGTH_SHORT).show();
		}
		else if(canSelect.getSelectedItem().toString().equals("AC Commpressor Power")){
			graph.addSeries(AcCompPowSeries);
			graph.setTitle("AC Commpressor Power");
			AcCompPowBool = true;
			//Toast.makeText(getApplicationContext(), "AC Commpressor Power selected", Toast.LENGTH_SHORT).show();

		}
		else if(canSelect.getSelectedItem().toString().equals("Battery State of Charge")){
			graph.addSeries(bSCSeries);
			graph.setTitle("Battery Charge");
			BSChargeBool = true;
			//Toast.makeText(getApplicationContext(), "Battery State of Charge", Toast.LENGTH_SHORT).show();

		}
		else if(canSelect.getSelectedItem().toString().equals("HV Battery Currrent")){
			graph.addSeries(HVBatCurSeries);
			graph.setTitle("HV Battery Currrent");
			HVBatCurBool = true;
			//Toast.makeText(getApplicationContext(), "HV Battery Currrent", Toast.LENGTH_SHORT).show();

		}
		else if(canSelect.getSelectedItem().toString().equals("Last Regen. Event")){
			graph.addSeries(lstRegSeries);
			graph.setTitle("Last Regen. Event");
			LstRegBool = true;
			//Toast.makeText(getApplicationContext(), "Last Regen. Event", Toast.LENGTH_SHORT).show();

		}
		else if(canSelect.getSelectedItem().toString().equals("Relative Drive Power")){
			graph.addSeries(RelDrPowSeries);
			graph.setTitle("Relative Drive Power");
			RelDrPowBool = true;
			//Toast.makeText(getApplicationContext(), "Relative Drive Power", Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}