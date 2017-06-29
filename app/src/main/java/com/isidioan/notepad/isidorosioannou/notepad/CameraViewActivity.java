package com.isidioan.notepad.isidorosioannou.notepad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView displayTitle;
    private TextView displayDesc;
    private ImageView displayImage;
    private int id;
    private Button backButton , deleteButton;
    private DataImage dataImage;
    private DatabaseAdapt dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        setTitle("Photo Display");
        displayTitle = (TextView) findViewById(R.id.displayTitle);
        displayDesc = (TextView) findViewById(R.id.displayDesc);
        displayImage = (ImageView) findViewById(R.id.displayImage);
        Button backButton = (Button) findViewById(R.id.displayBack);
        Button deleteButton = (Button) findViewById(R.id.displayDelete);
        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt(CameraMainActivity.VIEW_INTENT);
        dbAdapter = new DatabaseAdapt(this);
        dbAdapter.open();
        dataImage =dbAdapter.getOneCamera(id);
        displayTitle.setText(dataImage.getTitle());
        displayDesc.setText(dataImage.getDesc());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        displayImage.setImageBitmap(decodeSampledBitmapFromFile(dataImage.getPath(),width,height));
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.displayBack){
           // Intent intent = new Intent(this,CameraMainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
            finish();

        }
        else if (v.getId()==R.id.displayDelete){
            dbAdapter.deleteCamera(id);
           // Intent intent = new Intent(this,CameraMainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
            finish();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        dbAdapter.close();
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2
            // and keeps both height and width larger than the requested
            // height and width.
            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromFile(byte[] byteArray,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
      //  ByteArrayInputStream imageStream = new ByteArrayInputStream(byteArray);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray,0,byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize =
                calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length, options);
    }
}
