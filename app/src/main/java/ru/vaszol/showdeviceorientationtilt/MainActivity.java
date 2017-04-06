package ru.vaszol.showdeviceorientationtilt;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <h3>Пример инклиномера</h3>
 *
 * @author Vasiliy Zolotarev <vaszol@mail.ru>
 */
public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor sensorAccel;
    private TextView angleX, angleY, angleZ;
    Timer timer;
    float[] valuesAccel = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        angleX = (TextView) findViewById(R.id.angleX);
        angleY = (TextView) findViewById(R.id.angleY);
        angleZ = (TextView) findViewById(R.id.angleZ);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorAccel != null)
            sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    void showInfo() {
        float tiltX = ((float) Math.round(Math.toDegrees(Math.atan(valuesAccel[0] / (float) Math.sqrt((double) (valuesAccel[1] * valuesAccel[1]) + (valuesAccel[2] * valuesAccel[2]))))));
        float tiltY = ((float) Math.round(Math.toDegrees(Math.atan(valuesAccel[1] / (float) Math.sqrt((double) (valuesAccel[0] * valuesAccel[0]) + (valuesAccel[2] * valuesAccel[2]))))));
        float tiltZ = ((float) Math.round(Math.toDegrees(Math.atan(valuesAccel[2] / (float) Math.sqrt((double) (valuesAccel[0] * valuesAccel[0]) + (valuesAccel[1] * valuesAccel[1]))))));

        angleX.setText("X = " + tiltX);
        angleY.setText("Y = " + tiltY);
        angleZ.setText("Z = " + tiltZ);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i=0; i < 3; i++){
                        valuesAccel[i] = event.values[i];
                    }
                    break;
            }
        }
    };
}
