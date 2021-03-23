package com.example.firebaseemployee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etFName;
    private EditText etLName;
    private TextView tvEmp;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btnSubmit);
        etFName = (EditText)findViewById(R.id.etFirstName);
        etLName =  (EditText)findViewById(R.id.etLastName);
        tvEmp = (TextView) findViewById(R.id.tvEmp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.i("MainActivity",database.toString());
        DatabaseReference myRef = database.getReference("employees");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                StringBuilder strEmp = new StringBuilder();
                Employee emp;
                counter = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    emp = ds.getValue(Employee.class);
                    Log.i("MainActivity",counter+" - Firstname : "+emp.getFirstName()+" Lastname : "+emp.getLastName());
                    //strEmp.append("Employee No. = "+counter+"\n");
                    strEmp.append(emp.toString());
                    strEmp.append("\n");
                    strEmp.append("\n");
                    counter = counter + 1;
                }

                tvEmp.setText(strEmp);
                Log.d("MainActivity", "Counter : " + counter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                String firstName = etFName.getText().toString();
                String lastName = etLName.getText().toString();

                if (firstName.isEmpty()) {
                    etFName.setError("Please enter the first Name");
                }
                else if (lastName.isEmpty()) {
                    etLName.setError("Please enter the last Name");
                }
                else  {
                    Employee emp = new Employee(firstName,lastName);
                    myRef.child(String.valueOf(counter)).setValue(emp);
                    etFName.setText("");
                    etLName.setText("");

                }
            }
        });
    }
}