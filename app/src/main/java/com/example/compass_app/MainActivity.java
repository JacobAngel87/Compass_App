package com.example.compass_app;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView compBack;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    /*there are 3 axis. x,y,z. 3x3 array creates the matrix of 9*/
    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];

    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.comp_point);
        compBack = findViewById(R.id.comp_back);

        /*allows for access to the sensors Accelerometer and Magnetic Field*/
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGravity = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix,null, floatGravity,floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix,floatOrientation);

                imageView.setRotation((float) (-floatOrientation[0]*180/Math.PI));
                compBack.setRotation((float) (-floatOrientation[0]*180/Math.PI));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }; /* end of SensorEventListener for Accelerometer */


       SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {

               floatGeoMagnetic = event.values;

               SensorManager.getRotationMatrix(floatRotationMatrix,null, floatGravity,floatGeoMagnetic);
               SensorManager.getOrientation(floatRotationMatrix,floatOrientation);

               imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
               compBack.setRotation((float) (-floatOrientation[0]*180/3.14159));

           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       };/* end of SensorEventListener for MagneticField */


        sensorManager.registerListener(sensorEventListenerAccelerometer,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField,sensorMagneticField,SensorManager.SENSOR_DELAY_NORMAL);

    }/* end onCreate */
    public void ResetButton (View view){
      imageView.setRotation(180);
      compBack.setRotation(180);
    }
}/* end main */