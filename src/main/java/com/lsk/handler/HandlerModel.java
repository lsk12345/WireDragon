package com.lsk.handler;

import com.lsk.model.MyPacket;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;

import java.util.ArrayList;

public class HandlerModel {
    private PcapIf device;
    private ArrayList<PcapPacket> rawPacketList;
    private ArrayList<MyPacket> myPacketList;

    public HandlerModel() {
        this.rawPacketList=new ArrayList<PcapPacket>();
        this.myPacketList=new ArrayList<MyPacket>();
    }

    public HandlerModel(PcapIf device) {
        this.device = device;
        this.rawPacketList=new ArrayList<PcapPacket>();
        this.myPacketList=new ArrayList<MyPacket>();
    }

    public HandlerModel(PcapIf device, ArrayList<PcapPacket> rawPacketList, ArrayList<MyPacket> myPacketList) {
        this.device = device;
        this.rawPacketList = rawPacketList;
        this.myPacketList = myPacketList;
    }

    public PcapIf getDevice() {
        return device;
    }

    public void setDevice(PcapIf device) {
        this.device = device;
    }

    public ArrayList<MyPacket> getMyPacketList() {
        return myPacketList;
    }

    public void setMyPacketList(ArrayList<MyPacket> myPacketList) {
        this.myPacketList = myPacketList;
    }

    public ArrayList<PcapPacket> getRawPacketList() {
        return rawPacketList;
    }

    public void setRawPacketList(ArrayList<PcapPacket> rawPacketList) {
        this.rawPacketList = rawPacketList;
    }

    public ArrayList<MyPacket> getHandlerPacketList() {
        return myPacketList;
    }

    public void setHandlerPacketList(ArrayList<MyPacket> handlerPacketList) {
        this.myPacketList = handlerPacketList;
    }
}
