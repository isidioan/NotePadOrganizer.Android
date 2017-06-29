package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LocationMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Cursor cursor;
    private DatabaseAdapt dbAdapt;
    private CursorAdapter cursorAdapter;
    public static final String LOCATION_TITLE = "title";
    public static final String LOCATION_ADDRESS = "address";
    public static final String LOCATION_ID = "locId";
    public static final String LOCATION_LAT = "lat";
    public static final String LOCATION_lONG = "long";
    public static final String VIEW_LOC = "viewloc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.locationContainer);

        dbAdapt = new DatabaseAdapt(this);
        dbAdapt.open();
        cursor =dbAdapt.loadLocationTitles();
        String [] from = new String[]{DatabaseAdapt.LOCATION_TITLE,DatabaseAdapt.LOCATION_ADDRESS};
        int [] to = new int[] {android.R.id.text1,android.R.id.text2};


        cursorAdapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,
                from,to,0);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(this);
    }

    public void onResume(){
        super.onResume();
       Cursor newCursor=dbAdapt.loadLocationTitles();
        cursorAdapter.changeCursor(newCursor);
        cursor=newCursor;
    }
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        dbAdapt.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.add_button){

                Intent intent = new Intent(LocationMainActivity.this,LocationSecondActivity.class);
                intent.putExtra(LocationMainActivity.VIEW_LOC,false);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        Intent intent = new Intent(LocationMainActivity.this,LocationSecondActivity.class);
        intent.putExtra(LocationMainActivity.LOCATION_ID,cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.LOCATION_ID)));
        intent.putExtra(LocationMainActivity.LOCATION_TITLE,cursor.getString(cursor.getColumnIndex(DatabaseAdapt.LOCATION_TITLE)));
        intent.putExtra(LocationMainActivity.LOCATION_ADDRESS,cursor.getString(cursor.getColumnIndex(DatabaseAdapt.LOCATION_ADDRESS)));
        intent.putExtra(LocationMainActivity.LOCATION_LAT,cursor.getDouble(cursor.getColumnIndex(DatabaseAdapt.LOCATION_LAT)));
        intent.putExtra(LocationMainActivity.LOCATION_lONG,cursor.getDouble(cursor.getColumnIndex(DatabaseAdapt.LOCATION_LONG)));
        intent.putExtra(LocationMainActivity.VIEW_LOC,true);
        startActivity(intent);

    }
}
