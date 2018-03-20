package com.kks.agrisense;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    public static final String TAG="kks12345";
    TextView textView6;
    int moist;

    public String currenTime1()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView1 = (TextView) findViewById(R.id.textview1);
        final TextView textView2 = (TextView) findViewById(R.id.textview2);
        final TextView textView3 = (TextView) findViewById(R.id.textview3);
        final TextView textView4 = (TextView) findViewById(R.id.textview4);
        final TextView textView5 = (TextView) findViewById(R.id.textview5);
        textView6 = (TextView) findViewById(R.id.textview6);

        final DatabaseReference rootrefs = FirebaseDatabase.getInstance().getReference().child("data");
        rootrefs.child("LastIrrig").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                textView6.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child("data");
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value1 = dataSnapshot.child("SensorA").getValue().toString();
                    textView1.setText(value1);
                    moist = Integer.parseInt(value1);
                    if(moist <= 0) {
                        textView5.setText("NO SOIL");
                      //  textView6.setText("MOTOR OFF");
                    }else if(moist <300) {
                        textView5.setText("DRY SOIL");
                        String timet = currenTime1();
                        Log.i(TAG, timet);
                        textView6.setText(timet);
                        rootref.child("LastIrrig").setValue(timet);
                    }else if(moist <700) {
                        textView5.setText("HUMID SOIL");
                        String timet = currenTime1();
                        Log.i(TAG, timet);
                        textView6.setText(timet);
                        rootref.child("LastIrrig").setValue(timet);


                    } else {
                        textView5.setText("WATER SOIL");
                       // textView6.setText("MOTOR OFF");
                    }
                }catch (Exception e) {}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        DatabaseReference rootref1 = FirebaseDatabase.getInstance().getReference().child("data");
        rootref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value2 = dataSnapshot.child("SensorB").getValue().toString();
                    textView2.setText(value2);
                }catch (Exception e) {}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        DatabaseReference rootref2 = FirebaseDatabase.getInstance().getReference().child("data");
        rootref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value3 = dataSnapshot.child("SensorC").getValue().toString();
                    textView3.setText(value3);

                }catch (Exception e) {}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        DatabaseReference rootref3 = FirebaseDatabase.getInstance().getReference().child("data");
        rootref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value4 = dataSnapshot.child("SensorD").getValue().toString();
                    textView4.setText(value4);
                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });




    }

    public void clickAutomated(View view)
    {
        DatabaseReference rootref5 = FirebaseDatabase.getInstance().getReference().child("data");

       rootref5.child("MotorOn").setValue(0);
        Toast.makeText(MainActivity.this,"Automated motor control selected!",Toast.LENGTH_SHORT).show();


    }

     public void clickForcedOn(View view)
     {
         DatabaseReference rootref5 = FirebaseDatabase.getInstance().getReference().child("data");

         rootref5.child("MotorOn").setValue(1);
         Toast.makeText(MainActivity.this,"Motor is running!!",Toast.LENGTH_SHORT).show();
         String timet = currenTime1();
         Log.i(TAG, timet);
         textView6.setText(timet);
         rootref5.child("LastIrrig").setValue(timet);

     }

     public void clickForcedStop(View view)
     {
         DatabaseReference rootref5 = FirebaseDatabase.getInstance().getReference().child("data");

         rootref5.child("MotorOn").setValue(2);
         Toast.makeText(MainActivity.this,"Motor is forced stop!!",Toast.LENGTH_SHORT).show();
     }

}
