package com.lsk.handler;

import com.lsk.Main;
import com.lsk.app.SnifferFrame;
import com.lsk.filter.Filter;
import com.lsk.util.Utils;
import com.lsk.model.Base;
import com.lsk.model.Frame;
import com.lsk.model.MyPacket;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class MyHandler<T> implements PcapPacketHandler<T> {

    public void nextPacket(PcapPacket pcapPacket, T object) {
        if (pcapPacket != null && !Main.isPaused) {
            HandlerModel handlerModel= (HandlerModel) object;
            //原始包
            handlerModel.getRawPacketList().add(pcapPacket);
            MyPacket myPacket = analyze(pcapPacket,handlerModel.getDevice());
            Main.packetList.add(myPacket);
            //System.out.println("抓到包了");
            //添加渲染的model(要过滤)
            if(Filter.filter(myPacket)){
                SnifferFrame.packetModel.addRow(myPacket.getBase().getVector());
                //System.out.println("有包");
            }
        }
    }
    //分析
    public MyPacket analyze(PcapPacket packet, PcapIf device){
        char[] packetChars = Utils.getPacketCharList(packet);
        MyPacket myPacket=new MyPacket(packetChars);
        myPacket.setBase(new Base(packet, (long) (Main.packetList.size())));
        myPacket.setFrame(new Frame(packet,device));
        myPacket.setDataLink("Ethernet");
        //TODO:特殊处理
        //1.ARP的Base没有IP地址
        if(myPacket.getBase().getProtocol().equalsIgnoreCase("ARP")){
            myPacket.getBase().setDestinationIp(myPacket.getDataLink().getNetwork().getDestination().getMeaning());
            myPacket.getBase().setSourceIp(myPacket.getDataLink().getNetwork().getSource().getMeaning());
        }

        return myPacket;
    }
}
