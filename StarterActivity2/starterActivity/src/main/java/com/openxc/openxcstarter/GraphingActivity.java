package com.openxc.openxcstarter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.openxcplatform.openxcstarter.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GraphingActivity extends Activity implements OnItemSelectedListener  {

	// TextViews on activity
	private TextView connection_status;
	private TextView graphView1TextView;
	private TextView mGearPositionView;
	private TextView mFuelLevelView;
	private String name = "name";
	private String value = "value";

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


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphviewlayout);
		graph = (GraphView) findViewById(R.id.graph);
		ArrayList<Double> listRPM = (ArrayList<Double>) getIntent().getSerializableExtra("listRPM");
		ArrayList<Double> listSpeed = (ArrayList<Double>) getIntent().getSerializableExtra("listSpeed");
		
		ArrayList<Double> listBatStateCharge = (ArrayList<Double>) getIntent().getSerializableExtra("listBatStateCharge");
		ArrayList<Double> listHVBatCurr = (ArrayList<Double>) getIntent().getSerializableExtra("listHVBatCurr");
		
		ArrayList<Double> listLastRegEventScore= (ArrayList<Double>) getIntent().getSerializableExtra("listLastRegEventScore");
		ArrayList<Double> listRelDrivePower = (ArrayList<Double>) getIntent().getSerializableExtra("listRelDrivePower");
		ArrayList<Double> listAcCompressorPower = (ArrayList<Double>) getIntent().getSerializableExtra("listAcCompressorPower");

		
		
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
				rpmSeries.appendData(new DataPoint(i,listRPM.get(i)), true,1000);
			}
		}

		if (listSpeed != null){
			for (int i = 0; i < listSpeed.size(); i++){
				speedSeries.appendData(new DataPoint(i,listSpeed.get(i)), true,1000);
			}
		}	
		if (listBatStateCharge != null){
			for (int i = 0; i < listBatStateCharge.size(); i++){
				bSCSeries.appendData(new DataPoint(i,listBatStateCharge.get(i)), true,1000);
			}
		}

		if (listHVBatCurr != null){
			for (int i = 0; i < listHVBatCurr.size(); i++){
				HVBatCurSeries.appendData(new DataPoint(i,listHVBatCurr.get(i)), true,1000);
			}
		}
		if (listLastRegEventScore != null){
			for (int i = 0; i < listLastRegEventScore.size(); i++){
				lstRegSeries.appendData(new DataPoint(i,listLastRegEventScore.get(i)), true,1000);
			}
		}

		if (listRelDrivePower != null){
			for (int i = 0; i < listRelDrivePower.size(); i++){
				RelDrPowSeries.appendData(new DataPoint(i,listRelDrivePower.get(i)), true,1000);
			}
		}
		if (listAcCompressorPower != null){
			for (int i = 0; i < listAcCompressorPower.size(); i++){
				AcCompPowSeries.appendData(new DataPoint(i,listAcCompressorPower.get(i)), true,1000);
			}
		}
		

		//JSONArray m_jArry = obj.getJSONgoogle.rray("name");
		//ArrayList<HashMap<String,String>>



	}

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