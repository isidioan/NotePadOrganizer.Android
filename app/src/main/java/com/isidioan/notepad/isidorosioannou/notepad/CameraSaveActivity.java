package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class CameraSaveActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText cameraSaveTitle , cameraSaveDesc;
    private ImageView cameraPreview;
    private Button saveButton;
    private byte [] byteArray;
    private DatabaseAdapt adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_save);
        setTitle("New Photo");
        cameraSaveTitle = (EditText)findViewById(R.id.cameraEditTitle);
        cameraSaveDesc = (EditText)findViewById(R.id.cameraEditDesc);
        cameraPreview = (ImageView)findViewById(R.id.cameraPreview);
        saveButton = (Button)findViewById(R.id.saveCameraButton);
        Intent intent = getIntent();
        byteArray = intent.getByteArrayExtra("byteImage");
        ByteArrayInputStream imageStream = new ByteArrayInputStream(byteArray);
        cameraPreview.setImageBitmap(BitmapFactory.decodeStream(imageStream));
       // adapt = CameraMainActivity.getInstance();
        adapt = new DatabaseAdapt(this);
        adapt.open();
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.saveCameraButton){
            String text1 = cameraSaveTitle.getText().toString();
            String text2 = cameraSaveDesc.getText().toString();
            DataImage image = new DataImage(text1,text2,byteArray);
            try {
                adapt.createCamera(image);
                Toast.makeText(this,"Saved successfully",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
          // Intent intent = new Intent(this,CameraMainActivity.class);
            //startActivity(intent);
            finish();
        }
    }
    public void onDestroy(){
        super.onDestroy();
        adapt.close();
    }
}
