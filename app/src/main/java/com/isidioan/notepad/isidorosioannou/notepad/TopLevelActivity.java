package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TopLevelActivity extends AppCompatActivity implements View.OnClickListener{
    private PopupWindow popupWindow;
    private ImageView close;
    private TextView popupBody;
    private ConstraintLayout c1,c2,c3,c4;
    private StringBuilder text = new StringBuilder();
    private int height;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        c1 = (ConstraintLayout)findViewById(R.id.clayoutNotes);
        c2=(ConstraintLayout)findViewById(R.id.clayoutTasks);
        c3=(ConstraintLayout)findViewById(R.id.clayoutCamera);
        c4=(ConstraintLayout)findViewById(R.id.clayoutLocation);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
         width = displayMetrics.widthPixels;
        instructionsText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_activity_menu,menu);
      return   super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.help_button){
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.popup_layout,(ConstraintLayout)findViewById(R.id.popupContainer));
            popupBody = (TextView)view.findViewById(R.id.popupBody);
            popupWindow = new PopupWindow(view,width,height,true);

           popupBody.setText(text);
            popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
            close = (ImageView)view.findViewById(R.id.imageClose);
            close.setOnClickListener(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
     //   ViewGroup viewGroup = (ViewGroup) v;
        switch (v.getId()){
            case R.id.clayoutNotes:
                intent = new Intent(TopLevelActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.clayoutTasks:
                 intent = new Intent(TopLevelActivity.this,ToDoMainActivity.class);
                startActivity(intent);
                break;
            case R.id.clayoutCamera:
                intent = new Intent(TopLevelActivity.this,CameraMainActivity.class);
                startActivity(intent);
                break;
            case R.id.clayoutLocation:
                intent = new Intent(TopLevelActivity.this,LocationMainActivity.class);
                startActivity(intent);
                break;
            case R.id.imageClose:
                popupWindow.dismiss();
                break;
        }
    }
    private void instructionsText(){
        String line;
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new InputStreamReader(getAssets().open("instructions.txt")));
            while ((line=reader.readLine())!=null){
                text.append(line);
                text.append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();

                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}
