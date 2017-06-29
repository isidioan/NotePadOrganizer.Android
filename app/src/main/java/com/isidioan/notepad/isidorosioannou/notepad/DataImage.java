package com.isidioan.notepad.isidorosioannou.notepad;

/**
 * Created by akis on 22/5/2017.
 */

public class DataImage {

    private String title, desc ;
    private byte [] path;
    private int cId;


    public DataImage(int cId,String title, String desc, byte [] path ){

        this.cId=cId;
        this.title=title;
        this.desc=desc;
        this.path=path;
    }

    public DataImage(String title, String desc, byte [] path){

        this.title=title;
        this.desc=desc;
        this.path=path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public byte[] getPath(){
        return path;
    }
    public void setPath(byte [] path){
        this.path = path;
    }

    @Override
    public String toString() {
        return "ID: " + cId+", Title: " + title+ ",  Description: "+ desc;
    }
}
