package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends ListFragment {

    private DatabaseAdapt dbAdapter;
    private Cursor cursor;

    public MainListFragment() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

         dbAdapter = new DatabaseAdapt(getActivity().getBaseContext());
        dbAdapter.open();
        cursor = dbAdapter.loadAllNotes();
        String [] from = new String[]{DatabaseAdapt.NOTECOLUMN_TITLE , DatabaseAdapt.NOTECOLUMN_SUMMARY, DatabaseAdapt.NOTECOLUMN_DATE};
        int [] to = new int[]{R.id.noteTitle, R.id.noteSummary,R.id.noteDate};
        CursorAdapter adapter = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.custom_list_row,cursor,from,to,0);
        setListAdapter(adapter);
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    public void onListItemClick(ListView listView,View view,int position,long id) {

        super.onListItemClick(listView, view, position, id);

            procceedToOtherActivity(MainActivity.fragmentToChoose.DETAIL,position);

    }

        public void onStart() {

        super.onStart();
        registerForContextMenu(getListView());
    }
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbAdapter.close();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowNumber = menuInfo.position;
        cursor.moveToPosition(rowNumber);
        final int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.NOTECOLUMN_ID));
        switch(item.getItemId()) {

            case R.id.editOption:
                procceedToOtherActivity(MainActivity.fragmentToChoose.EDIT,rowNumber);
                return  true;

            case R.id.deleteOption:
                final DatabaseAdapt dbAdapter = new DatabaseAdapt(getActivity().getBaseContext());
                dbAdapter.open();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbAdapter.deleteNote(id);
                        Cursor newCursor = dbAdapter.loadAllNotes();
                        CursorAdapter adapter = (CursorAdapter)getListAdapter();
                        adapter.changeCursor(newCursor);
                        cursor=newCursor;
                    }
                });
                builder.setNegativeButton("No", null) ;

                builder.create().show();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void procceedToOtherActivity (MainActivity.fragmentToChoose fragm,int position){

        cursor.moveToPosition(position);
        Intent intent = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
        intent.putExtra(MainActivity.NOTEID,cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.NOTECOLUMN_ID)) );
        intent.putExtra(MainActivity.NOTETITLE,cursor.getString(cursor.getColumnIndex(DatabaseAdapt.NOTECOLUMN_TITLE)));
        intent.putExtra(MainActivity.NOTESUMMARY,cursor.getString(cursor.getColumnIndex(DatabaseAdapt.NOTECOLUMN_SUMMARY)));
        intent.putExtra(MainActivity.NOTEDATE,cursor.getString(cursor.getColumnIndex(DatabaseAdapt.NOTECOLUMN_DATE)));
        switch (fragm){

            case EDIT:
                intent.putExtra(MainActivity.FRAGMENTTOCHOOSE,MainActivity.fragmentToChoose.EDIT);
                break;
            case DETAIL:
                intent.putExtra(MainActivity.FRAGMENTTOCHOOSE,MainActivity.fragmentToChoose.DETAIL);
                break;
        }
        startActivity(intent);
    }
}


