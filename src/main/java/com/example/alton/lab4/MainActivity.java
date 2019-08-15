package com.example.alton.lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText e5 = (EditText) findViewById(R.id.ID);
        final EditText e1 = (EditText) findViewById(R.id.name);
        final EditText e2 = (EditText) findViewById(R.id.email);
        final EditText e4 = (EditText) findViewById(R.id.percentage);
        final EditText e3 = (EditText) findViewById(R.id.dob);


        Button b = (Button) findViewById(R.id.insert);
        Button b2=(Button) findViewById(R.id.delete);
        Button b3=(Button) findViewById(R.id.view);
        Button b4=(Button) findViewById(R.id.get);
        db = this.openOrCreateDatabase("studentmarks", MODE_PRIVATE, null);
        Log.i("Created Database:","True");
        db.execSQL("CREATE TABLE IF NOT EXISTS MARKS (ID VARCHAR,NAME VARCHAR,EMAIL VARCHAR,PERCENTAGE decimal,DOB VARCHAR)");
        Log.i("Created Table:","True");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Values:","Received");
                Log.i("ID:",e5.getText().toString());
                Log.i("Name:",e1.getText().toString());
                Log.i("Email:",e2.getText().toString());
                Log.i("Percentage:",e4.getText().toString());
                Log.i("DOB:",e3.getText().toString());
                Log.i("Values:","Inserting into Database");
                db.execSQL("INSERT INTO MARKS VALUES('" + e5.getText().toString() +"','"+e1.getText().toString()+ "','"+ e2.getText().toString() +"','"+ e4.getText().toString() +"','"+ e3.getText().toString() +"');");
                Log.i("Values:","Inserted");
                showMessage("Success", "Record added");

            }

        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Data:","Retrieving");
                Cursor c= db.rawQuery("Select DISTINCT ID from MARKS",null);
                Log.i("Data:","Retrieved");
                Log.i("Cursor:","Moving to First");
                c.moveToPosition(-1);
                Log.i("Cursor:","Moved");
                StringBuffer buffer= new StringBuffer();
                ArrayList<String> ID = new ArrayList<String>();
                ArrayList<Integer> count=new ArrayList<Integer>();
                while (c.moveToNext())
                {
                    ID.add(c.getString(0));
                    count.add(0);
                }
                Log.i("Unique Values:","Retrieved");
                c=db.rawQuery("Select * from MARKS",null);
                c.moveToPosition(-1);
                for(int i=0;i<ID.size();i++)
                {
                    while (c.moveToNext())
                    {
                        if(ID.get(i).equals(c.getString(0)))
                        {
                            count.set(i,count.get(i)+1);
                        }
                        if(count.get(i)>1)
                        {
                            db.execSQL("DELETE FROM MARKS WHERE EMAIL = '"+c.getString(2).toString()+"'");
                        }
                    }
                }

                showMessage("Success", "Duplicate Records Deleted");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor c= db.rawQuery("SELECT * FROM MARKS",null);
                c.moveToPosition(-1);
                if(c.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }

                StringBuffer buffer= new StringBuffer();
                while (c.moveToNext())
                {
                    buffer.append("ID:"+c.getString(0)+"\n");
                }
                showMessage("Students",buffer.toString());
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c=db.rawQuery("Select * from MARKS where PERCENTAGE >= 70",null);
                c.moveToPosition(-1);
                if(c.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }

                StringBuffer buffer= new StringBuffer();
                while (c.moveToNext())
                {
                    buffer.append("ID:"+c.getString(0)+"\t");
                    buffer.append("Name:"+c.getString(1)+"\t");
                    buffer.append("Percentage:"+c.getString(3)+"\n");
                }
                showMessage("Students",buffer.toString());
            }

        });
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
