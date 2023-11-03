package com.lsk.protocol.transport;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class UDP extends Transport{
    //长度
    private Field length;
    //检验和
    private Field checkSum;
    public UDP(char[] packetChars) {
        name = "UDP";
        //0-3:源端口
        char[] char1 = Utils.slice(packetChars,0,4);
        this.setSource(new Field(Utils.hexToInt(char1)+"",char1));
        //4-7:目的端口
        char[] char2 = Utils.slice(packetChars,4,8);
        this.setDestination(new Field(Utils.hexToInt(char2)+"",char2));
        //8-11:长度
        char[] char3 = Utils.slice(packetChars,8,12);
        this.length = new Field(Utils.hexToInt(char3)+"",char3);
        //12-15:检验和
        char[] char4 = Utils.slice(packetChars,12,16);
        this.checkSum = new Field("0x"+String.valueOf(char4),char4);

        //应用层
        this.setApplication(Utils.getApplicationName((int) Utils.hexToInt(char2)),Utils.slice(packetChars,16));


    }

    public void getUI(){

        model.addRow(Utils.fieldToObject("sourcePort",getSource()));
        model.addRow(Utils.fieldToObject("destinationPort",getDestination()));
        model.addRow(Utils.fieldToObject("length",length));
        model.addRow(Utils.fieldToObject("checkSum",checkSum));
    }
    public Field getLength() {
        return length;
    }

    public void setLength(Field length) {
        this.length = length;
    }

    public Field getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Field checkSum) {
        this.checkSum = checkSum;
    }
}
