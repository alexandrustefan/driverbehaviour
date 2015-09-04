package ro.appcamp.driverbehaviour;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MainService extends IntentService implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 10;
    private static final int SAMPLE_RATE = 1000;   // ms


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS



//    private static final String ACTION_FOO = "ro.appcamp.driverbehaviour.action.FOO";
//    private static final String ACTION_BAZ = "ro.appcamp.driverbehaviour.action.BAZ";

    // TODO: Rename parameters
    public static final String SIM_NR = "ro.appcamp.driverbehaviour.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "ro.appcamp.driverbehaviour.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionMain(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MainService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > SAMPLE_RATE)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                Log.d("drvbehav","BEFORE IF Acceleration : x = " + x + " ; y = " + y + " ; z = " + z);

                if (speed > SHAKE_THRESHOLD)
                {
                    Toast.makeText(getApplicationContext(), "Acceleration : x = " + x + " ; y = " + y + " ; z = " + z, Toast.LENGTH_LONG).show();
                    Log.d("drvbehav", "Acceleration : x = " + x + " ; y = " + y + " ; z = " + z);
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String param1 = intent.getStringExtra(SIM_NR);
            handleActionMain(param1);
        }


    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMain(String param1) {
        // TODO: Handle action Foo

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
