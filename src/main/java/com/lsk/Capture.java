package com.lsk;

import com.lsk.handler.HandlerModel;
import com.lsk.handler.MyHandler;
import com.lsk.model.MyPacket;
import com.lsk.protocol.transport.TCPStream;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;


public class Capture implements Runnable{
    private PcapIf device;
    private HandlerModel handlerModel;



    public Capture() {
    }
    public Capture(PcapIf device, HandlerModel handlerModel) {
        this.device = device;
        this.handlerModel = handlerModel;
    }

    //捕获
    public void run() {
        System.out.println("开始运行");
        StringBuilder error = new StringBuilder();
        //启动
        Pcap pcap = Pcap.openLive(
                device.getName(),
                Pcap.DEFAULT_JPACKET_BUFFER_SIZE,//数据包大小
                Pcap.MODE_PROMISCUOUS,//网卡模式
                60 * 1000,//超时(ms)
                error);
        if (pcap == null){
            System.err.println("获取数据包失败：" + error.toString());
            return;
        }
        MyHandler<Object> myHandler = new MyHandler<Object>();
        //-1为无限循环
        pcap.loop(-1,myHandler,handlerModel);
        //System.out.println(handlerModel.getRawPacketList().get(0));
    }


}
