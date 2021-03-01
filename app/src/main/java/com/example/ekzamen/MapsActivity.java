package com.example.ekzamen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public LinkedList<Note> AllNotes;
    public LinkedList<Note> currentNotes;
    Marker CurrentMarker=null;
    Intent intent;
    ArrayAdapter<Note> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        intent=new Intent(getApplicationContext(),AddNoteActivity.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AllNotes=new LinkedList<Note>();
        currentNotes=new LinkedList<Note>();


        adapter=new ArrayAdapter<Note>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, currentNotes)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);

                return view;
            }
        };
//       adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,R.id.textView,history);

        ((ListView)findViewById(R.id.listView)).setAdapter(adapter);
        ((ListView)findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.putExtra("tittle", currentNotes.get(position).getHeader());
                intent.putExtra("ID", currentNotes.get(position).getID());
                intent.putExtra("lang", currentNotes.get(position).getPlace().longitude);
                intent.putExtra("notetext", currentNotes.get(position).getText());
                intent.putExtra("lat", currentNotes.get(position).getPlace().latitude);

                startActivity(intent);
            }
        });



    }


    public void ClickAddNode(View v)
    {
        if(CurrentMarker!=null) {
            intent.putExtra("lang", CurrentMarker.getPosition().longitude);
            intent.putExtra("lat", CurrentMarker.getPosition().latitude);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You must make a marker first!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {


            Bundle args = intent.getExtras();
            intent.removeExtra("lat");
            intent.removeExtra("lang");
            if (args != null) {
                intent.removeExtra("tittle");
                intent.removeExtra("ID");
                intent.removeExtra("notetext");

            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        ReadFromDB();
        if(CurrentMarker!=null) {
            GetCurrentNotes(CurrentMarker.getPosition());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
            public boolean onMarkerClick(Marker marker) {
                CurrentMarker=marker;
                GetCurrentNotes(marker.getPosition());
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                CurrentMarker=mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        FillMarkersFromBD();
    }

    public void GetCurrentNotes(LatLng position)
    {
        currentNotes.clear();
        for(int k=0;k<AllNotes.size();k++)
        {
            if(AllNotes.get(k).getPlace().latitude==position.latitude &&
                    AllNotes.get(k).getPlace().longitude==position.longitude)
            {
                currentNotes.add(AllNotes.get(k));
            }

        }
        adapter.notifyDataSetChanged();
    }


    public void ReadFromDB()
    {
//
        try {


            AllNotes.clear();
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("noteDB", MODE_PRIVATE, null);

//            db.execSQL("DROP TABLE IF EXISTS notes");
            db.execSQL("CREATE TABLE IF NOT EXISTS notes(ID INTEGER PRIMARY KEY AUTOINCREMENT,tittle TEXT,notetext TEXT, lat REAL, lang REAL)");
            Cursor cursor = db.rawQuery("SELECT * FROM notes", null);

            while (cursor.moveToNext()) {
                AllNotes.add(new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        new LatLng(cursor.getDouble(3), cursor.getDouble(4))));

            }

            cursor.close();
            db.close();



        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void FillMarkersFromBD()
    {
        mMap.clear();
        for(int k=0;k<AllNotes.size();k++)
        {
            mMap.addMarker(new MarkerOptions().position(AllNotes.get(k).getPlace()).title("Marker "+(k+1)));
        }

    }

}