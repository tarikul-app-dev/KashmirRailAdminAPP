package limited.it.planet.traintimingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import limited.it.planet.traintimingapp.R;
import limited.it.planet.traintimingapp.model.StatusM;

public class StatusActivity extends AppCompatActivity {
    Button btnSaveStatus,btnDelStatus;
    EditText edtStatus;
    DatabaseReference databaseReference;
    StatusM statusM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        btnSaveStatus = (Button)findViewById(R.id.btn_save_status);
        btnDelStatus = (Button)findViewById(R.id.btn_delete_status) ;
        edtStatus = (EditText)findViewById(R.id.edt_status);
        statusM = new StatusM();
        databaseReference = FirebaseDatabase.getInstance().getReference("status");
        btnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = edtStatus.getText().toString();
                statusM.setStatus_nume(status);
              //  statusM.setId("status");
//                Random generator = new Random();
//                int  n = 10000;
//                n = generator.nextInt(n);
//                String childOfStation = String.valueOf(n);
                try {
                    databaseReference.child("status").setValue(statusM);
                    Toast.makeText(StatusActivity.this, "Successfully send to server", Toast.LENGTH_SHORT).show();


                }
                catch (Exception ex)
                {
                    Toast.makeText(StatusActivity.this, "Some Issue (Check your Internet connection and try again)", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDelStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("status").removeValue();
                Toast.makeText(StatusActivity.this, "Successfully Remove", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
