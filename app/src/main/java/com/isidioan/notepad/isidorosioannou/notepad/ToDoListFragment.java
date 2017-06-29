package com.isidioan.notepad.isidorosioannou.notepad;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoListFragment extends ListFragment {

    private AlertDialog alertWindow;
    private DatabaseAdapt dbAdapter;
    private Cursor cursor;
    private ToDoCursorAdapter cursorAdapter;
    private EditText taskNew;
    private CheckBox cb;

    public ToDoListFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater,container,savedInstanceState);
       // cb = (CheckBox) fragmentView.findViewById(R.id.toDoCheckBox);
        taskNew = new EditText(getActivity());
        setHasOptionsMenu(true);
        dbAdapter = new DatabaseAdapt(getActivity().getBaseContext());
        dbAdapter.open();
        cursor = dbAdapter.loadAllTasks();
        cursorAdapter = new ToDoCursorAdapter(getActivity().getBaseContext(),cursor,dbAdapter);
        setListAdapter(cursorAdapter);
        createAlertWindow();
        /*
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor newCursor = dbAdapter.loadAllTasks();
                ToDoCursorAdapter adapter = (ToDoCursorAdapter)getListAdapter();
                adapter.changeCursor(newCursor);
                cursor=newCursor;
            }
        });
        */

        return fragmentView;
    }

    public void onStart(){

        super.onStart();
        registerForContextMenu(getListView());
    }
    public void onDestroy(){

        super.onDestroy();
        cursor.close();
        dbAdapter.close();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.add_button){
            taskNew.setText("");
            showAlertWindow();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAlertWindow () {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Add a new Task");
        alertDialog.setView(taskNew);
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = taskNew.getText().toString();
               // dbAdapter.open();
                dbAdapter.createTask(text,0);
                Cursor newCursor = dbAdapter.loadAllTasks();
                ToDoCursorAdapter adapter = (ToDoCursorAdapter) getListAdapter();
                adapter.changeCursor(newCursor);
                cursor = newCursor;

            }
        });
        alertDialog.setNegativeButton("Cancel",null);

        alertWindow = alertDialog.create();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);
        MenuItem item = menu.findItem(R.id.editOption);
        item.setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowNumber = menuInfo.position;
        cursor.moveToPosition(rowNumber);
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_ID));
        if (item.getItemId() == R.id.deleteOption){
            dbAdapter.deleteTask(id);
            Cursor newCursor = dbAdapter.loadAllTasks();
            ToDoCursorAdapter adapter = (ToDoCursorAdapter)getListAdapter();
            adapter.changeCursor(newCursor);
            cursor = newCursor;
            return true;

        }

        return super.onContextItemSelected(item);
    }

    public void showAlertWindow () {

        alertWindow.show();
    }

}
