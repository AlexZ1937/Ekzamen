package com.example.ekzamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.LinkedList;

public class AddNoteActivity extends AppCompatActivity {

    Calendar dateAndTime=Calendar.getInstance();

    boolean isChange=false;
    int IDChange=0;
    double Lat=0.0;
    double Lang=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);








        Bundle args = getIntent().getExtras();
        Lat=args.getDouble("lat");
        Lang=args.getDouble("lang");
        if(args.get("tittle") != null)
        {
            IDChange= (int) args.get("ID");
            isChange=true;
            ((TextView)findViewById(R.id.editTextTextPersonName2)).setText(args.get("tittle").toString());
            ((TextView)findViewById(R.id.editTextTextPersonName3)).setText(args.get("notetext").toString());



        }
    }



    public void SaveToDBNote(String tittle, String notetext, Double lat, Double lang)
    {
        try {


            SQLiteDatabase datab = getBaseContext().openOrCreateDatabase("noteDB", MODE_PRIVATE, null);
            datab.execSQL("CREATE TABLE IF NOT EXISTS notes(ID INTEGER PRIMARY KEY AUTOINCREMENT,tittle TEXT,notetext TEXT, lat REAL, lang REAL)");
            ContentValues values = new ContentValues();
            values.put("tittle",tittle);
            values.put("notetext",notetext);
            values.put("lat",lat);
            values.put("lang",lang);

            if(isChange)
            {
                datab.update("notes",values,"ID = "+IDChange,null);
            }
            else {
                datab.insert("notes", null, values);

            }
            datab.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void SaveClick(View v)
    {
        if(((TextView)findViewById(R.id.editTextTextPersonName3)).getText().length()>0 && ((TextView)findViewById(R.id.editTextTextPersonName2)).getText().length()>0) {
//            getIntent().putExtra("tittle", ((TextView)findViewById(R.id.editTextTextPersonName2)).getText());
//            getIntent().putExtra("date", ((TextView)findViewById(R.id.button2)).getText());
//            getIntent().putExtra("nodetext", ((TextView)findViewById(R.id.editTextTextPersonName3)).getText());
            try {

//
                SaveToDBNote(((TextView) findViewById(R.id.editTextTextPersonName2)).getText().toString(),
                        ((TextView) findViewById(R.id.editTextTextPersonName3)).getText().toString(),Lat,Lang );

            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
            this.finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Add any text to node!", Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearClick(View v)
    {
        ((TextView)findViewById(R.id.editTextTextPersonName3)).setText("");
    }

    public void CancelClick(View v)
    {
        this.finish();
    }





}

