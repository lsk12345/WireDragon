package com.lsk.protocol.network;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class ARP extends Network{
    //硬件类型（这里的默认都是1）
    private Field hardwareType;
    //使用协议
    private Field protocol;
    //硬件长度
    private Field hardwareSize;
    //协议长度
    private Field protocolSize;
    //操作码
    private Field opCode;
    //发送方的mac
    private Field senderMac;
    //接收方的mac
    private Field targetMac;

    public ARP(char[] packetChars) {
        name = "ARP";
        //0-3:默认都是1以及Ethernet
        char[] char1 = Utils.slice(packetChars,0,4);
        this.hardwareType = new Field(String.valueOf(char1).equals("0001")?"Ethernet":Utils.unknown,char1);

        //4-7:协议类型
        char[] char2 = Utils.slice(packetChars,4,8);
        String protocol = Utils.getNetworkProtocol(String.valueOf(char2));
        this.protocol = new Field(protocol,char2);

        //8-9:硬件长度
        char[] char3 = Utils.toArray(packetChars[8],packetChars[9]);
        this.hardwareSize = new Field(Utils.hexToInt(char3)+"",char3);

        //10-11:协议长度
        char[] char4 = Utils.toArray(packetChars[10],packetChars[11]);
        this.protocolSize = new Field(Utils.hexToInt(char4)+"",char4);

        //12-15:操作码
        char[] char5 = Utils.slice(packetChars,12,16);
        this.opCode = new Field(Utils.hexToInt(char5)+"",char5);

        //16-27:源mac
        char[] char6 = Utils.slice(packetChars,16,28);
        this.senderMac = new Field(Utils.getMac(char6),char6);

        if(protocol.equals("IPv4")){
            //28-35:源ip
            char[] char7 = Utils.slice(packetChars,28,36);
            this.setSource(new Field(Utils.getIPv4(char7),char7));

            //36-47:目的mac
            char[] char8 = Utils.slice(packetChars,36,48);
            this.targetMac = new Field(Utils.getMac(char8),char8);

            //48-55:目的ip
            char[] char9 = Utils.slice(packetChars,48,56);
            this.setDestination(new Field(Utils.getIPv4(char9),char9));

        }
        else if(protocol.equals("IPv6")){
            //28-59:源ip
            char[] char7 = Utils.slice(packetChars,28,60);
            this.setSource(new Field(Utils.getIPv6(char7),char7));
            //60-71:目的mac
            char[] char8 = Utils.slice(packetChars,60,72);
            this.targetMac = new Field(Utils.getMac(char8),char8);
            //72-103:目的ip
            char[] char9 = Utils.slice(packetChars,72,104);
            this.setDestination(new Field(Utils.getIPv6(char9),char9));
        }
    }

    public void getUI(){
        model.addRow(Utils.fieldToObject("hardwareType",hardwareType));
        model.addRow(Utils.fieldToObject("protocol",protocol));
        model.addRow(Utils.fieldToObject("hardwareSize",hardwareSize));
        model.addRow(Utils.fieldToObject("protocolSize",protocolSize));
        model.addRow(Utils.fieldToObject("opCode",opCode));
        model.addRow(Utils.fieldToObject("senderMac",senderMac));
        model.addRow(Utils.fieldToObject("sourceIP",getSource()));
        model.addRow(Utils.fieldToObject("targetMac",targetMac));
        model.addRow(Utils.fieldToObject("destinationIP",getDestination()));
    }

    public Field getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(Field hardwareType) {
        this.hardwareType = hardwareType;
    }

    public Field getProtocol() {
        return protocol;
    }

    public void setProtocol(Field protocol) {
        this.protocol = protocol;
    }

    public Field getHardwareSize() {
        return hardwareSize;
    }

    public void setHardwareSize(Field hardwareSize) {
        this.hardwareSize = hardwareSize;
    }

    public Field getProtocolSize() {
        return protocolSize;
    }

    public void setProtocolSize(Field protocolSize) {
        this.protocolSize = protocolSize;
    }

    public Field getOpCode() {
        return opCode;
    }

    public void setOpCode(Field opCode) {
        this.opCode = opCode;
    }

    public Field getSenderMac() {
        return senderMac;
    }

    public void setSenderMac(Field senderMac) {
        this.senderMac = senderMac;
    }

    public Field getTargetMac() {
        return targetMac;
    }

    public void setTargetMac(Field targetMac) {
        this.targetMac = targetMac;
    }
}
