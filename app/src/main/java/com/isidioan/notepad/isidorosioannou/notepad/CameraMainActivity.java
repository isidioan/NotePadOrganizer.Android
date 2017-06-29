package com.isidioan.notepad.isidorosioannou.notepad;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraMainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ImageView buttonImage;
    private GridView gridView;
    private ArrayList<DataImage> imageList;
    private DatabaseAdapt dbAdapter;
    private CameraAdapter cameraAdapter;
    private AlertDialog alertBuilder;
    private Uri imageUri;
    private Bitmap bitMap;
    private String mCurrentPhotoPath;
    private byte [] byteArray;
    private static final String EXTERNAL_PERMISSIONS[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private boolean perm;
    private static final int EXTERNAL_CODE = 3;
    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_GALLERY_CODE=2;
    public static final String VIEW_INTENT = "viewIntent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        buttonImage = (ImageView)findViewById(R.id.cameraOptionButton);
        buttonImage.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridviewPhoto);
        dbAdapter = new DatabaseAdapt(getApplicationContext());
        dbAdapter.open();
        imageList = new ArrayList<>();
        imageList = dbAdapter.loadAllImages();
        cameraAdapter = new CameraAdapter(this,imageList);
        gridView.setAdapter(cameraAdapter);
        createAlertWindow();
        gridView.setOnItemClickListener(this);
        askForPerm();

    }
    public void onResume(){
        super.onResume();
        cameraAdapter.refresh(dbAdapter.loadAllImages());
    }
    public void onDestroy(){
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataImage image = (DataImage) gridView.getItemAtPosition(position);
        Intent intent = new Intent(this,CameraViewActivity.class);
        intent.putExtra(VIEW_INTENT,image.getcId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.cameraOptionButton){
            alertBuilder.show();
        }
    }

    private void createAlertWindow(){

        AlertDialog.Builder alertDialog = new  AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alert_title)
                    .setItems(R.array.alert_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0){
                                if (perm){
                                    activateCamera();
                                }
                            }
                            else{
                                if(perm){
                                    chooseFromGallery();
                                }
                            }
                        }
                    });
        alertBuilder = alertDialog.create();
    }
    private void activateCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".CameraMainActivity", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
            }else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
            }
        }
    }
    private void chooseFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUEST_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {
            //   Bundle extra = data.getExtras();
            //  bitMap = extra.getParcelable("data");
            //   byteArray = convertToByte(bitMap);
            Uri imageU = Uri.parse(mCurrentPhotoPath);
            bitMap =decodeUri(imageU,600);
            byteArray=convertToByte(bitMap);

        }
        else if(requestCode==REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data !=null){
            imageUri = data.getData();
            bitMap = decodeUri(imageUri,600);
            byteArray=convertToByte(bitMap);
        }
       if(byteArray!=null) {
           Intent intent = new Intent(this, CameraSaveActivity.class);
           intent.putExtra("byteImage", byteArray);
           startActivity(intent);
       }
    }

    private Bitmap decodeUri(Uri image,int size){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(image),null,options);
            int width = options.outWidth;
            int height = options.outHeight;
            int scale = 1;
            while (true){
                if(width/2<size || height/2<size){
                    break;
                }
                width /=2;
                height /=2;
                scale *=2;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize=scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(image),null,options2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  byte[] convertToByte(Bitmap bitmap){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,b);
        return  b.toByteArray();
    }

   private void askForPerm () {

      // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           int w = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
           int r = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

           if (w != PackageManager.PERMISSION_GRANTED && r != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, EXTERNAL_PERMISSIONS, EXTERNAL_CODE);
           } else {
             //  Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show();
               perm=true;
           }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
          case EXTERNAL_CODE:
            if(ActivityCompat.checkSelfPermission(this,permissions[0])==PackageManager.PERMISSION_GRANTED)
            {
                perm=true;
             }
            else {
            Toast.makeText(this,"Permissions denied",Toast.LENGTH_LONG).show();
            perm=false;
        }
        break;
    }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
