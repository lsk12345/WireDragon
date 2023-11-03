package com.lsk;

import com.lsk.app.MainFrame;
import com.lsk.app.WelcomeFrame;
import com.lsk.filter.Filter;
import com.lsk.model.MyPacket;
import com.lsk.protocol.transport.TCPStream;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<MyPacket> packetList = new ArrayList<MyPacket>();
    public static boolean isPaused = false;
    //TCP流
    public static List<TCPStream> TCPStreamList = new ArrayList<TCPStream>();
    public static void main(String[] args) {
        //System.loadLibrary("jnetpcap");
        //System.out.println(System.getProperty("java.library.path"));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new WelcomeFrame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        List<PcapIf> devList = new Device().findAllDev();
//        System.out.println(devList);
//        PcapIf device =devList.get(3);
//        new Capture(device,new HandlerModel(device)).capture();
//        //System.out.println(devList);
    }
}
