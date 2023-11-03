package com.lsk.model;

import com.lsk.protocol.datalink.DataLink;
import com.lsk.protocol.datalink.Ethernet;
import com.lsk.util.Utils;

public class MyPacket {
    private Base base;
    private Frame frame;
    private DataLink dataLink;
    private Field data;
    private char[] packetChars;
    public MyPacket(char[] packetChars) {
        this.packetChars = packetChars;
    }



    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public DataLink getDataLink() {
        return dataLink;
    }

    public void setDataLink(String protocolName) {
        if(protocolName.equalsIgnoreCase("Ethernet")) this.dataLink = new Ethernet(packetChars);
        //无对应协议，包的余下部分作为数据
        else this.data = new Field("",packetChars);
        //添加最高协议
        String highProtocol=Utils.unknown,protocols="";
        if(dataLink!=null){
            protocols += (dataLink.name);
            if(dataLink.getNetwork()!=null){
                protocols += ("->"+dataLink.getNetwork().name);
                if(dataLink.getNetwork().getTransport()!=null){
                    protocols += ("->"+dataLink.getNetwork().getTransport().name);
                    if(dataLink.getNetwork().getTransport().getApplication()!=null){
                        protocols += ("->"+dataLink.getNetwork().getTransport().getApplication().name);
                        highProtocol = dataLink.getNetwork().getTransport().getApplication().name;
                    }else{
                        highProtocol = dataLink.getNetwork().getTransport().name;
                    }
                }else{
                    highProtocol = dataLink.getNetwork().name;
                }
            }else{
                highProtocol = dataLink.name;
            }
        }
        this.getBase().setProtocol(highProtocol);
        this.getBase().setProtocols(protocols);
    }

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }

    public void setDataLink(DataLink dataLink) {
        this.dataLink = dataLink;
    }

    public char[] getPacketChars() {
        return packetChars;
    }

    public void setPacketChars(char[] packetChars) {
        this.packetChars = packetChars;
    }

    public String getPacketString(){
        return Utils.charToString(packetChars);
    }
    public String getPacketASCII(){
        return Utils.charToASCIIFor8(packetChars);
    }
}
