package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.isidioan.notepad.isidorosioannou.notepad.R.id.toDoText;

/**
 * Created by akis on 19/5/2017.
 */

public class ToDoCursorAdapter extends CursorAdapter {

    private  DatabaseAdapt adapter;
    private static class ViewRef {

          TextView text;
          CheckBox check;

      }

    public ToDoCursorAdapter(Context context, Cursor cursor, DatabaseAdapt databaseAdapt){
        super(context,cursor,0);
        this.adapter = databaseAdapt;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ViewRef viewRef = new ViewRef();
        View convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_todo,parent,false);
        convertView.setTag(viewRef);
        return convertView;
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        ViewRef viewRef = (ViewRef) view.getTag();
        viewRef.text = (TextView) view.findViewById(toDoText);
        viewRef.check = (CheckBox) view.findViewById(R.id.toDoCheckBox);
        CheckBox cb = viewRef.check;
        //TextView toDoText = (TextView) view.findViewById(toDoText);
        //CheckBox toDoCheck = (CheckBox) view.findViewById(toDoCheckBox);
        //toDoCheck.setTag(new Integer(cursor.getPosition()));
        cb.setTag(new Integer(cursor.getPosition()));
        String text = cursor.getString(cursor.getColumnIndex(DatabaseAdapt.TODO_TEXT));
        //boolean status = ((cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_CHECKED)) == 1 ? true : false));
         viewRef.text.setText(text);
        //toDoText.setText(text);
        //toDoCheck.setChecked(status);
        //if(status){
          //  toDoText.setPaintFlags(toDoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //}
        //else {
          //  toDoText.setPaintFlags(toDoText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        //}

       // final int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_ID));

        CompoundButton.OnCheckedChangeListener changeListener =     new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TextView toDoText = (TextView) view.findViewById(toDoText);
               // CheckBox toDoCheck = (CheckBox) view.findViewById(toDoCheckBox);
                Integer pos = (Integer) buttonView.getTag();
              //  adapter.open();
                if(cursor.moveToPosition(pos)) {
                    int rowId = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_ID));
                    if (isChecked) {
                        adapter.updateTask(rowId, 1);
                    } else if (!isChecked) {
                        adapter.updateTask(rowId, 0);
                    }
                }
               // toDoCheck.setChecked(isChecked);
                //if(isChecked){
                  //  toDoText.setPaintFlags(toDoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //}
                //else
                //{
                  //  toDoText.setPaintFlags(toDoText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                //}

            }
        };
        //};

       // toDoCheck.setOnCheckedChangeListener(changeListener);
        cb.setOnCheckedChangeListener(changeListener);
        boolean status = ((cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.TODO_CHECKED)) == 1 ? true : false));
        if(status){
            cb.setChecked(status);
            viewRef.text.setPaintFlags(viewRef.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            cb.setChecked(status);
            viewRef.text.setPaintFlags(viewRef.text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
