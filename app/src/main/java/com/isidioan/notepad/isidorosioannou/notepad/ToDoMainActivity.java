package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoMainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText taskNew;
    private AlertDialog alertWindow,alertEdit;
    private DatabaseAdapt dbAdapter;
    private Cursor cursor;
    private ToDoCursorAdapter cursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.listView);
        taskNew = new EditText(this);

        dbAdapter = new DatabaseAdapt(getApplicationContext());
        dbAdapter.open();
        cursor = dbAdapter.loadAllTasks();
        cursorAdapter = new ToDoCursorAdapter(this,cursor,dbAdapter);
        listView.setAdapter(cursorAdapter);
        createAlertWindow();
        registerForContextMenu(listView);

    }

    public void onDestroy(){

        super.onDestroy();
        cursor.close();
        dbAdapter.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.add_button) {
            taskNew.setText("");
            showAlertWindow();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlertWindow () {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add a new Task");
        alertDialog.setView(taskNew);
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = taskNew.getText().toString();
                dbAdapter.createTask(text,0);
                new AsyncTasks().execute();

            }
        });
        alertDialog.setNegativeButton("Cancel",null);

        alertWindow = alertDialog.create();

    }
    public void showAlertWindow () {

        alertWindow.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);
       // MenuItem item = menu.findItem(R.id.editOption);
        //item.setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowNumber = menuInfo.position;
        cursor.moveToPosition(rowNumber);
        final int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_ID));
        if (item.getItemId() == R.id.deleteOption){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbAdapter.deleteTask(id);
                    new AsyncTasks().execute();
                }
            });
            builder.setNegativeButton("No",null);
            builder.create().show();


            return true;
        }
        else {
            AlertDialog.Builder alertEditDialog = new AlertDialog.Builder(this);
            alertEditDialog.setTitle("Edit the Task");
            final EditText taskEdit = new EditText(this);
            taskEdit.setText(cursor.getString(cursor.getColumnIndex(DatabaseAdapt.TODO_TEXT)));
            alertEditDialog.setView(taskEdit);
            alertEditDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = taskEdit.getText().toString();
                    dbAdapter.updateTitleTask(id,text);
                    new AsyncTasks().execute();

                }
            });
            alertEditDialog.setNegativeButton("Cancel",null);

            alertEdit = alertEditDialog.create();
            alertEdit.show();
        }

        return super.onContextItemSelected(item);
    }

    public void clickHandler(View v){
        if(v.getId()==R.id.toDoCheckBox){
            new AsyncTasks().execute();
        }
    }

    private class AsyncTasks extends AsyncTask<Void,Void,Cursor>{

        @Override
        protected Cursor doInBackground(Void... params) {

             return dbAdapter.loadAllTasks();
        }

        @Override
        protected void onPostExecute(Cursor newCursor) {
            ToDoCursorAdapter adapter = (ToDoCursorAdapter)listView.getAdapter();
            adapter.changeCursor(newCursor);
            cursor=newCursor;
        }
    }



}
