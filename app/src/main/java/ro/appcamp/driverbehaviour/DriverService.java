package ro.appcamp.driverbehaviour;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

public class DriverService extends Service implements SensorEventListener{
    public DriverService() {
    }

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    LocationManager locManager;
    LocationListener locListener;

    private static JSONArray eventList;

    private float globalSpeed;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 10;
    private static final int SAMPLE_RATE = 1000;   // ms
    private static final String serverURL = "http://elaborate-hash-105813.appspot.com/";

    // TODO: Rename parameters
    public static String SIM_NR = "ro.appcamp.driverbehaviour.extra.PARAM1";


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String param1 = intent.getStringExtra(SIM_NR);
            Log.d("drvbehav", "SIM = " + param1);

            eventList = new JSONArray();

            senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locListener = new speed();
            try
            {locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);}
            catch (Exception e)
            {Log.d("drvbehav", "------->>>>> SPEED exception!!!!!");}

           // String response = executePost(serverURL, "toSend");
        }
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

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

                float acc = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                Log.d("drvbehav", "CONTINUE_READ Acceleration : x = " + x + " ; y = " + y + " ; z = " + z);
                if (acc > SHAKE_THRESHOLD)
                {
                    Toast.makeText(getApplicationContext(), "Acceleration : x = " + x + " ; y = " + y + " ; z = " + z, Toast.LENGTH_LONG).show();
                    Log.d("drvbehav", "Acceleration : x = " + x + " ; y = " + y + " ; z = " + z);

                    JSONObject event = new JSONObject();

                    try
                    {
                        event.put("msisdn", SIM_NR);
                        event.put("timestamp", new Timestamp(new java.util.Date().getTime()).toString());
                        event.put("type", "acc");
                        event.put("acc", Float.toString(acc));
                        eventList.put(event);
                    }
                    catch (Exception e){}
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    public static String executePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            //InputStream is = connection.getInputStream();
            //BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            //while((line = rd.readLine()) != null) {
            //    response.append(line);
            //    response.append('\r');
            //}
            //rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }



    class speed implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            loc.getLatitude();
            loc.getLongitude();

            Float currentSpeed = loc.getSpeed() * (float)3.6;
            Log.d("drvbehav", "CONTINUE_READ Speed : " + currentSpeed);

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > SAMPLE_RATE)
            {
                Log.d("drvbehav", "Speed : " + currentSpeed);
                Toast.makeText(getApplicationContext(), "Speed : " + currentSpeed, Toast.LENGTH_SHORT).show();

                globalSpeed = currentSpeed;

                JSONObject event = new JSONObject();

                try
                {
                    event.put("msisdn", SIM_NR);
                    event.put("timestamp", new Timestamp(new java.util.Date().getTime()).toString());
                    event.put("type", "speed");
                    event.put("value",  Float.toString(currentSpeed));
                    eventList.put(event);
                }
                catch (Exception e){}
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("drvbehav", "------->>>>> SPEED status changed");
        }

        @Override
        public void onProviderDisabled(String arg0) {Log.d("drvbehav", "------->>>>> SPEED provider DISABLED");}


        @Override
        public void onProviderEnabled(String arg0) {Log.d("drvbehav", "------->>>>> SPEED provider ENABLED");}
    }

}
