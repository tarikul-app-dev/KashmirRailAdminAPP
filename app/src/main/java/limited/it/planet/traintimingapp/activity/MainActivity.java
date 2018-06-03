package limited.it.planet.traintimingapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import limited.it.planet.traintimingapp.R;
import limited.it.planet.traintimingapp.model.TrainTimingM;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DatabaseReference databaseReference;
    TrainTimingM trainTimingM;
    Spinner spinRailways;
    EditText edtTrainNumber,edtDay,edtTiming;
    Button btnSave,btnDelete,btnShow,btnUploadImage,btnStatus;

    String trainNumber = "";
    String day = "";
    String timing ="";
    String stationItem = "";
    static ArrayList<TrainTimingM> trainScheduleList ;
    TableLayout tableLayout;
    public static int mValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews(){
        databaseReference = FirebaseDatabase.getInstance().getReference("name_of_station");
        trainTimingM = new TrainTimingM();
        trainScheduleList = new ArrayList<>();

        tableLayout = findViewById(R.id.tbl_layout_train_list);
        spinRailways = (Spinner)findViewById(R.id.spin_list_of_railway);
        edtTrainNumber = (EditText)findViewById(R.id.edt_train_number);
        edtDay = (EditText)findViewById(R.id.edt_day);
        edtTiming = (EditText)findViewById(R.id.edt_timing);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnDelete = (Button)findViewById(R.id.btn_delete);
        btnShow = (Button)findViewById(R.id.btn_show);
        btnUploadImage = (Button)findViewById(R.id.btn_upload_img);
        btnStatus = (Button)findViewById(R.id.btn_status);
        edtDay.setText("DAILY");

        spinRailways.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mValue = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stationItem = spinRailways.getSelectedItem().toString();
                trainTimingM.setStation_title(stationItem);
                trainNumber = edtTrainNumber.getText().toString();
                trainTimingM.setTrain_number(trainNumber);
                day = edtDay.getText().toString();
                trainTimingM.setDay(day);
                timing = edtTiming.getText().toString();
                trainTimingM.setTiming(timing);
                mValue ++;
                trainTimingM.setSerialNumber(mValue);


                Random generator = new Random();
                int  n = 10000;
                n = generator.nextInt(n);
                String childOfStation = String.valueOf(n);
                try {
                    databaseReference.child(trainTimingM.getStation_title()).child(childOfStation).setValue(trainTimingM);
                    Toast.makeText(MainActivity.this, "Successfully send to server", Toast.LENGTH_SHORT).show();
                    edtTrainNumber.setText("");
                    edtDay.setText("");
                    edtTiming.setText("");


                    redrawEverything();
                    addHeaders();
                    showTableView();

                }
                catch (Exception ex)
                {
                    Toast.makeText(MainActivity.this, "Some Issue (Check your Internet connection and try again)", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stationItem = spinRailways.getSelectedItem().toString();
                trainTimingM.setStation_title(stationItem);
                databaseReference.child(trainTimingM.getStation_title()).removeValue();
                Toast.makeText(MainActivity.this, "Successfully Remove", Toast.LENGTH_SHORT).show();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stationItem = spinRailways.getSelectedItem().toString();
                trainTimingM.setStation_title(stationItem);
                trainScheduleList.clear();
                redrawEverything();
                addHeaders();
                showTableView();
            }
        });

        spinRailways.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                stationItem = spinRailways.getSelectedItem().toString();
                trainTimingM.setStation_title(stationItem);
                redrawEverything();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UploadPicActivity.class);
                startActivity(intent);

            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showTableView(){

        DatabaseReference dr = databaseReference.child(trainTimingM.getStation_title());
        dr.addValueEventListener(new ValueEventListener() {
            // ArrayList<TrainSchedule> trainScheduleList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trainScheduleList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    TrainTimingM c = snapshot.getValue(TrainTimingM.class);
                    //Log.d("Categories: ", c.name + " " + c.food_items);
                    trainScheduleList.add(c);

                }

               orderDescending(trainScheduleList);

                redrawEverything();
                addHeaders();
                addRows();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void orderDescending(final ArrayList<TrainTimingM> list) {
        Collections.sort(list, new Comparator<TrainTimingM>() {
            public int compare(TrainTimingM s1, TrainTimingM s2) {
                Integer i1 =  s1.getSerialNumber();
                Integer i2 =  s2.getSerialNumber() ;
                // return i1.compareTo(i2);
                return i2.compareTo(i1);
            }
        });
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }
    private TextView getRowsTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {

        TableLayout tl = findViewById(R.id.tbl_layout_train_list);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        //  tr.addView(getTextView(0, "Auditor id", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "TRAIN NUMBER", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "DAY", Color.WHITE, Typeface.BOLD,  R.color.colorAccent));
        tr.addView(getTextView(0, "TIMING", Color.WHITE, Typeface.BOLD,  R.color.colorAccent));

        tl.addView(tr, getTblLayoutParams());
    }
    public void addRows(){
        Collections.reverse(trainScheduleList);

        for (int i = 0; i < trainScheduleList.size(); i++) {
            TableRow tr = new TableRow(MainActivity.this);
            tr.setLayoutParams(getLayoutParams());

            tr.addView(getRowsTextView(0, trainScheduleList.get(i).getTrain_number(), Color.BLACK , Typeface.BOLD ,ContextCompat.getColor(MainActivity.this, R.color.cell_background_color)));
            tr.addView(getRowsTextView(0, trainScheduleList.get(i).getDay(), Color.BLACK,Typeface.BOLD , ContextCompat.getColor(MainActivity.this, R.color.cell_background_color)));
            tr.addView(getRowsTextView(0, trainScheduleList.get(i).getTiming(), Color.BLACK,Typeface.BOLD ,  ContextCompat.getColor(MainActivity.this, R.color.cell_background_color)));

            tableLayout.addView(tr, getTblLayoutParams());

        }

    }

    private void redrawEverything()
    {
        tableLayout.removeAllViews();

    }

}
