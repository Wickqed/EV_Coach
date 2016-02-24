package com.openxc.openxcstarter;
//Testing the commets
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.openxcplatform.openxcstarter.R;
import com.openxc.VehicleManager;
import com.openxc.measurements.AcCompressorPower;
import com.openxc.measurements.BatteryStateOfCharge;
import com.openxc.measurements.FuelLevel;
import com.openxc.measurements.HvBatteryCurrent;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.LastRegenEventScore;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.RelativeDrivePower;
import com.openxc.measurements.VehicleSpeed;

import java.util.TimerTask;
import java.util.Timer;

public class StarterActivity extends Activity {
	private static final String TAG = "StarterActivity";

	private VehicleManager mVehicleManager;
	private TextView mEngineSpeedView;
	TextToSpeech ttobj;
	String status = "";

	ImageView ev_logo;
	ImageView ford_logo;
	ImageView mtu_logo;
	ImageView openxc_logo;

	// TextViews on activity
	private TextView connection_status;

	// the tuple key corresponding to vibration on the watch
	private static final int VIBE_KEY = 0;
	// the tuple key corresponding to the logo displayed on the watch
	private static final int LOGO_KEY = 1;
	// UUID identifier for the vibe app
	private final static UUID VIBE_UUID = UUID.fromString("7dd8789d-3bb2-4596-ac10-fbe15196419d");

	private static final int SHORT_PULSE = 0;
	private static final int LONG_PULSE = 1;
	private static final int DOUBLE_PULSE = 2;

	//Temp variable to simulate all vibration types
	int tempVibeVar = 0;

	//Counter variable
	int count = 0;
	Timer t = new Timer();

	ArrayList<Double> listRPM = new ArrayList<Double>();
	ArrayList<Double> listSpeed = new ArrayList<Double>();
	ArrayList<Double> listBatStateCharge = new ArrayList<Double>();
	ArrayList<Double> listHVBatCurr = new ArrayList<Double>();
	ArrayList<Double> listLastRegEventScore = new ArrayList<Double>();
	ArrayList<Double> listRelDrivePower = new ArrayList<Double>();
	ArrayList<Double> listAcCompressorPower = new ArrayList<Double>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		// grab a reference to the engine speed text object in the UI, so we can
		// manipulate its value later from Java code
		mEngineSpeedView = (TextView) findViewById(R.id.vehicle_speed);

		connection_status = (TextView) findViewById(R.id.connection_status);
		connection_status.setTextColor(Color.RED);
		connection_status.setText("Not connected");


		ttobj=new TextToSpeech(getApplicationContext(), 
				new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					ttobj.setLanguage(Locale.UK);
				}				
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		// When the activity goes into the background or exits, we want to make
		// sure to unbind from the service to avoid leaking memory
		if(mVehicleManager != null) {
			Log.i(TAG, "Unbinding from Vehicle Manager");
			// Remember to remove your listeners, in typical Android
			// fashion.
			mVehicleManager.removeListener(EngineSpeed.class,
					mSpeedListener);
			mVehicleManager.removeListener(IgnitionStatus.class,
					mIgnitionListener);
			mVehicleManager.removeListener(FuelLevel.class, mFuelListener);
			mVehicleManager.removeListener(VehicleSpeed.class, mSpeedVehicleListener);
			mVehicleManager.removeListener(AcCompressorPower.class, mAcCompressorPowerListener);
			mVehicleManager.removeListener(BatteryStateOfCharge.class, mBatteryStateOfChargeListener);
			mVehicleManager.removeListener(HvBatteryCurrent.class, mHvBatteryCurrentListener);
			mVehicleManager.removeListener(LastRegenEventScore.class, mLastRegenEventScoreListener);
			mVehicleManager.removeListener(RelativeDrivePower.class, mRelativeDrivePowerListener);
			unbindService(mConnection);
			mVehicleManager = null;
		}

		if(ttobj !=null){
			ttobj.stop();
			ttobj.shutdown();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// When the activity starts up or returns from the background,
		// re-connect to the VehicleManager so we can receive updates.
		if(mVehicleManager == null) {
			Intent intent = new Intent(this, VehicleManager.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	/* This is an OpenXC measurement listener object - the type is recognized
	 * by the VehicleManager as something that can receive measurement updates.
	 * Later in the file, we'll ask the VehicleManager to call the receive()
	 * function here whenever a new EngineSpeed value arrives.
	 */
	EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
		@Override
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final EngineSpeed speed = (EngineSpeed) measurement;
			Log.i(TAG, "received measurement speed");
			listRPM.add(speed.getValue().doubleValue());
			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(speed.getValue().doubleValue() > 2500){
							ttobj.speak("Don't go above 2500 rpm to improve battery range", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}

						}
					}*/



				}
			});
		}
	};

	IgnitionStatus.Listener mIgnitionListener = new IgnitionStatus.Listener(){

		@Override
		public void receive(Measurement measurement) {
			final IgnitionStatus status = (IgnitionStatus) measurement;
			double totalScore = 0;
			Log.i(TAG, "received measurement IgnitionStatus");
			if(status.getValue().toString().toUpperCase().equals("OFF")) {
				Log.i(TAG, "IS RUNNING");
				Intent i = new Intent(getApplicationContext(), GraphingActivity.class);
				i.putExtra("listRPM", listRPM);
				i.putExtra("listSpeed", listSpeed);
				i.putExtra("listBatStateCharge", listBatStateCharge);
				i.putExtra("listHVBatCurr", listHVBatCurr);
				i.putExtra("listLastRegEventScore", listLastRegEventScore);
				i.putExtra("listRelDrivePower", listRelDrivePower);
				i.putExtra("listAcCompressorPower", listAcCompressorPower);

				startActivity(i);
			}
		}

	};

	VehicleSpeed.Listener mSpeedVehicleListener = new VehicleSpeed.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final VehicleSpeed speed = (VehicleSpeed) measurement;
			Log.i(TAG, "received measurement");
			listSpeed.add(speed.getValue().doubleValue());
			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value


					/*if(count == 1){
						t.schedule(
								new TimerTask(){
									public void run(){
										count = 0;
									}
								}, 10000, 10000);
					}*/
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(speed.getValue().doubleValue() > 30){
						if(count == 0){
							count = 1;
							ttobj.speak("Don't go above 30 kilometers per hour to improve battery range", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}
						}
					}*/
				}
			});
		}
	};

	FuelLevel.Listener mFuelListener = new FuelLevel.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new FuelLevel value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an FuelLevel.
			final FuelLevel fuel = (FuelLevel) measurement;
			Log.i(TAG, "received measurement");

			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value
				}
			});
		}
	};

	/* This is an OpenXC measurement listener object - the type is recognized
	 * by the VehicleManager as something that can receive measurement updates.
	 * Later in the file, we'll ask the VehicleManager to call the receive()
	 * function here whenever a new EngineSpeed value arrives.
	 */
	AcCompressorPower.Listener mAcCompressorPowerListener = new AcCompressorPower.Listener() { 
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final AcCompressorPower power = (AcCompressorPower) measurement;
			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.

			listAcCompressorPower.add(power.getValue().doubleValue());
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value

					// Toasting if criteria is met
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(power.getValue().doubleValue() > 1000){
							count = 0;
							//Toast.makeText(getApplicationContext(), "Turn down AC to conserve battery", Toast.LENGTH_SHORT).show();
							ttobj.speak("Turn down AC to conserve battery", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}
						}
					}*/
				}
			});
		}
	};

	BatteryStateOfCharge.Listener mBatteryStateOfChargeListener = new BatteryStateOfCharge.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final BatteryStateOfCharge charge = (BatteryStateOfCharge) measurement;
			Log.i(TAG, "received measurement");
			listBatStateCharge.add(charge.getValue().doubleValue());

			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value

					// Toasting if criteria is met
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(charge.getValue().doubleValue() < 10){
							//Toast.makeText(getApplicationContext(), "Low battery find charging station", Toast.LENGTH_SHORT).show();
							ttobj.speak("Low battery find charging station", TextToSpeech.QUEUE_FLUSH, null);
							//batteryWarning = true;
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}
						}
					}*/
				}
			});
		}
	};

	HvBatteryCurrent.Listener mHvBatteryCurrentListener = new HvBatteryCurrent.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final HvBatteryCurrent current = (HvBatteryCurrent) measurement;
			Log.i(TAG, "received measurement");
			listHVBatCurr.add(current.getValue().doubleValue());

			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value
					// Toasting if criteria is met
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(current.getValue().doubleValue() > 50){
							ttobj.speak("Turn off AC", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}

							//Toast.makeText(getApplicationContext(), "Don't go above 2000RPM to improve battery range", Toast.LENGTH_SHORT).show();
						}
					}*/
				}
			});
		}
	};

	LastRegenEventScore.Listener mLastRegenEventScoreListener = new LastRegenEventScore.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final LastRegenEventScore score = (LastRegenEventScore) measurement;
			Log.i(TAG, "received measurement");
			listLastRegEventScore.add(score.getValue().doubleValue());

			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value
					// Toasting if criteria is met
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(score.getValue().doubleValue() < 75){
							//Toast.makeText(getApplicationContext(), "Brake sooner to better use your regenerative brake", Toast.LENGTH_SHORT).show();
							ttobj.speak("Brake sooner to conserve battery", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}
						}
					}*/
				}
			});
		}
	};

	RelativeDrivePower.Listener mRelativeDrivePowerListener = new RelativeDrivePower.Listener() {
		public void receive(Measurement measurement) {
			// When we receive a new EngineSpeed value from the car, we want to
			// update the UI to display the new value. First we cast the generic
			// Measurement back to the type we know it to be, an EngineSpeed.
			final RelativeDrivePower power = (RelativeDrivePower) measurement;
			Log.i(TAG, "received measurement");
			listRelDrivePower.add(power.getValue().doubleValue());

			// In order to modify the UI, we have to make sure the code is
			// running on the "UI thread" - Google around for this, it's an
			// important concept in Android.
			StarterActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Finally, we've got a new value and we're running on the
					// UI thread - we set the text of the EngineSpeed view to
					// the latest value

					// Toasting if criteria is met
				/*	if(count > 0){
						count++;
					}
					if(count == 100){
						count = 0;
					}
					if(count == 0){
						if(power.getValue().doubleValue() > 2000){
							//Toast.makeText(getApplicationContext(), "Don't go above 2000RPM to improve battery range", Toast.LENGTH_SHORT).show();
							ttobj.speak("Turn down relative drive power", TextToSpeech.QUEUE_FLUSH, null);
							boolean connected = isPebbleConnected();
							if(connected == true){
								tempVibeVar = 0;
								sendDataToWatch();
							}
						}
					}*/


				}
			});
		}
	};



	private ServiceConnection mConnection = new ServiceConnection() {
		// Called when the connection with the VehicleManager service is
		// established, i.e. bound.
		public void onServiceConnected(ComponentName className,
				IBinder service) {
			Log.i(TAG, "Bound to VehicleManager");
			// When the VehicleManager starts up, we store a reference to it
			// here in "mVehicleManager" so we can call functions on it
			// elsewhere in our code.
			mVehicleManager = ((VehicleManager.VehicleBinder) service)
					.getService();


			setContentView(R.layout.splash_screen);
			connection_status = (TextView) findViewById(R.id.connection_status);
			connection_status.setTextColor(Color.GREEN);
			connection_status.setText("Connected");

			// We want to receive updates whenever the EngineSpeed changes. We
			// have an EngineSpeed.Listener (see above, mSpeedListener) and here
			// we request that the VehicleManager call its receive() method
			// whenever the EngineSpeed changes
			mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
			mVehicleManager.addListener(IgnitionStatus.class, mIgnitionListener);
			mVehicleManager.addListener(VehicleSpeed.class, mSpeedVehicleListener);
			mVehicleManager.addListener(FuelLevel.class, mFuelListener);
			mVehicleManager.addListener(AcCompressorPower.class, mAcCompressorPowerListener);
			mVehicleManager.addListener(BatteryStateOfCharge.class, mBatteryStateOfChargeListener);
			mVehicleManager.addListener(HvBatteryCurrent.class, mHvBatteryCurrentListener);
			mVehicleManager.addListener(LastRegenEventScore.class, mLastRegenEventScoreListener);
			mVehicleManager.addListener(RelativeDrivePower.class, mRelativeDrivePowerListener);
		}


		// Called when the connection with the service disconnects unexpectedly
		public void onServiceDisconnected(ComponentName className) {
			Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
			mVehicleManager = null;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.starter, menu);
		return true;
	}

	public boolean isPebbleConnected() {
		boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
		Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
		return connected;
	}

	public void sendDataToWatch() {

		// Build dictionary with vibration data
		PebbleDictionary data = new PebbleDictionary();

		// replace tempVibeVar with SHORT_PULSE, LONG_PULSE, DOUBLE_PULSE, CUSTOM_PULSE
		data.addUint8(VIBE_KEY, (byte) tempVibeVar);
		data.addString(1, "A string");

		//send the dictionary
		PebbleKit.sendDataToPebble(getApplicationContext(), VIBE_UUID, data);
	}
}
