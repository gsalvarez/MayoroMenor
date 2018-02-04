package com.igabeto.mayoromenor;

public class Player {
    private String name;
    private int normalRecord;
    private int advancedRecord;
    private float average;

    public Player(String name, int normalRecord, int advancedRecord, float average){
        this.name = name;
        this.normalRecord = normalRecord;
        this.advancedRecord = advancedRecord;
        this.average = average;
    }

    public String getName(){
        return this.name;
    }
    public int getNormalRecord() {
        return this.normalRecord;
    }
    public int getAdvancedRecord() {
        return this.advancedRecord;
    }
    public float getAverage() {
        return this.average;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setNormalRecord(int normalRecord){
        this.normalRecord = normalRecord;
    }
    public void setAdvancedRecord(int advancedRecord){
        this.advancedRecord = advancedRecord;
    }
    public void setAverage(int average){
        this.average = average;
    }
}
