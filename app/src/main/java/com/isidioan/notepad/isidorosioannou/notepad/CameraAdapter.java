package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by akis on 23/5/2017.
 */

public class CameraAdapter extends ArrayAdapter<DataImage>{

    private ArrayList<DataImage> imageList;
    private static class ViewRef{
        ImageView icon;
        TextView title;
        TextView desc;
    }


    public CameraAdapter(Context context, ArrayList<DataImage> list){

        super(context,0,list);
        imageList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewRef viewRef;

        if(convertView == null){
            viewRef = new ViewRef();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_camera_view,parent,false);
            viewRef.title = (TextView)convertView.findViewById(R.id.cameraTitle);
            viewRef.desc = (TextView)convertView.findViewById(R.id.cameraDesc);
            viewRef.icon = (ImageView)convertView.findViewById(R.id.customImageView);
            convertView.setTag(viewRef);
        }
        else {
            viewRef = (ViewRef)convertView.getTag();
        }
        DataImage image = imageList.get(position);
        viewRef.title.setText(image.getTitle());
        viewRef.desc.setText(image.getDesc());
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image.getPath());
        viewRef.icon.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        return convertView;
    }
    public void refresh (ArrayList<DataImage> list){
           imageList.clear();
           imageList.addAll(list);
            notifyDataSetChanged();
    }
}
