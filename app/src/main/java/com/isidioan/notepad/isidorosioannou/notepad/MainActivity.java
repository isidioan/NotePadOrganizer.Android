package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

   public static final String NOTEID = "noteID";
    public static final String NOTETITLE = "noteTitle";
    public static final String NOTESUMMARY = "noteSummary";
    public static final String NOTEDATE = "noteDate";
    public static final String FRAGMENTTOCHOOSE = "fragmentToChoose";
    public enum fragmentToChoose { DETAIL , EDIT, ADD };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_button) {
            Intent intent = new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtra(MainActivity.FRAGMENTTOCHOOSE,MainActivity.fragmentToChoose.ADD);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
