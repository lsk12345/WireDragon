package com.lsk.protocol.transport;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class TCP extends Transport{
    //数据流编号
    private long streamIndex;
    //序号
    private Field sequenceNumber;
    //确认号
    private Field acknowledgeNumber;
    //报文头长(4bytes为单位)
    private Field headerLength;
    //标志位
    private Field flag;
    //窗口
    private Field window;
    //检验和
    private Field checkSum;
    //紧急指针
    private Field urgentPointer;
    //选项
    private Field option;

    public TCP(char[] packetChars) {
        name = "TCP";
        //0-3:源端口
        char[] char1 = Utils.slice(packetChars,0,4);
        this.setSource(new Field(Utils.hexToInt(char1)+"",char1));
        //4-7:目的端口
        char[] char2 = Utils.slice(packetChars,4,8);
        this.setDestination(new Field(Utils.hexToInt(char2)+"",char2));
        //8-15:序号
        char[] char3 = Utils.slice(packetChars,8,16);
        this.sequenceNumber = new Field(Utils.hexToInt(char3)+"",char3);
        //16-23:确认号
        char[] char4 = Utils.slice(packetChars,16,24);
        this.acknowledgeNumber = new Field(Utils.hexToInt(char4)+"",char4);
        //24:报文头长度
        int headerLength = (int) Utils.hexToInt(packetChars[24])*4;
        this.headerLength = new Field(headerLength+"bytes",Utils.toArray(packetChars[24]));
        //25-27:25的4位以及26的前2位为保留位
        char[] char5 = Utils.slice(packetChars,25,28);
        String binary = Utils.hexToBinary(new String(char5));
        this.flag = new Field(
                "URG: "+isSet(binary.charAt(6))+
                        "/ACK: "+isSet(binary.charAt(7))+
                        "/PSH: "+isSet(binary.charAt(8))+
                        "/RST: "+isSet(binary.charAt(9))+
                        "/SYN: "+isSet(binary.charAt(10))+
                        "/FIN: "+isSet(binary.charAt(11)),char5);
        //28-31:窗口
        char[] char6 = Utils.slice(packetChars,28,32);
        this.window = new Field(Utils.hexToInt(char6)+"",char6);
        //32-35:检验和
        char[] char7 = Utils.slice(packetChars,32,36);
        this.checkSum = new Field("0x"+String.valueOf(char7),char7);
        //36-40:紧急指针
        char[] char8 = Utils.slice(packetChars,36,40);
        this.urgentPointer = new Field(Utils.hexToInt(char8)+"",char8);
        //40-headerLength(例如32)*2:选项
        if(headerLength-20>0){
            char[] char9 = Utils.slice(packetChars,40,headerLength*2);
            this.option = new Field((headerLength-20)+"bytes",char9);
        }
        //应用层
        this.setApplication(Utils.getApplicationName((int) Utils.hexToInt(char2)),Utils.slice(packetChars,headerLength*2));
    }

    public void getUI(){
        model.addRow(Utils.fieldToObject("streamIndex",streamIndex));
        model.addRow(Utils.fieldToObject("sourcePort",getSource()));
        model.addRow(Utils.fieldToObject("destinationPort",getDestination()));
        model.addRow(Utils.fieldToObject("sequenceNumber",sequenceNumber));
        model.addRow(Utils.fieldToObject("acknowledgeNumber",acknowledgeNumber));
        model.addRow(Utils.fieldToObject("headerLength",headerLength));
        model.addRow(Utils.fieldToObject("flag",flag));
        model.addRow(Utils.fieldToObject("window",window));
        model.addRow(Utils.fieldToObject("checkSum",checkSum));
        model.addRow(Utils.fieldToObject("urgentPointer",urgentPointer));
        if(option!=null){
            model.addRow(Utils.fieldToObject("option",option));
        }
    }

    private String isSet(char c) {
        return (c == '0'?"Not Set":"Set");
    }

    public long getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(long streamIndex) {
        this.streamIndex = streamIndex;
    }

    public Field getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Field sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Field getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    public void setAcknowledgeNumber(Field acknowledgeNumber) {
        this.acknowledgeNumber = acknowledgeNumber;
    }

    public Field getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(Field headerLength) {
        this.headerLength = headerLength;
    }

    public Field getFlag() {
        return flag;
    }

    public void setFlag(Field flag) {
        this.flag = flag;
    }

    public Field getWindow() {
        return window;
    }

    public void setWindow(Field window) {
        this.window = window;
    }

    public Field getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Field checkSum) {
        this.checkSum = checkSum;
    }

    public Field getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(Field urgentPointer) {
        this.urgentPointer = urgentPointer;
    }

    public Field getOption() {
        return option;
    }

    public void setOption(Field option) {
        this.option = option;
    }
}
