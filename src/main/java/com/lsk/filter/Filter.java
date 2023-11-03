package com.lsk.filter;

import com.lsk.Main;
import com.lsk.app.SnifferFrame;
import com.lsk.model.MyPacket;
import com.lsk.protocol.network.Network;
import com.lsk.protocol.transport.TCP;
import com.lsk.protocol.transport.Transport;
import com.lsk.util.Utils;

import java.util.List;

public class Filter {
    public String protocol;
    public String IP;
    public String port;
    public String mac;
    public String streamIndex;

    public Filter(){
        protocol = "全部";
        IP = "";
        port = "";
        mac = "";
        streamIndex = "";
    }

    public static boolean filter(MyPacket packet) {
        boolean isProtocol = false;
        boolean isIP = false;
        boolean isPort = false;
        boolean isMac = false;
        boolean isStream = false;
        List<String> protocolList = Utils.getProtocolList(packet.getBase().getProtocols());
        //协议
        String protocol = SnifferFrame.filter.protocol;
        if(protocol.equals("全部")) isProtocol = true;
        else{
            for(String protocol1:protocolList){
                if(protocol.equals(protocol1)){
                    isProtocol = true;
                    break;
                }
            }
        }
        if(!isProtocol) return false;

        //IP地址
        String IP = SnifferFrame.filter.IP;
        if(IP.equals("")) isIP = true;
        else{
            if(IP.equals(packet.getBase().getSourceIp()) || IP.equals(packet.getBase().getDestinationIp())) isIP = true;
        }
        if(!isIP) return false;

        //port
        String port = SnifferFrame.filter.port;
        if(port.equals("")) isPort = true;
        else{
            Network network = packet.getDataLink().getNetwork();
            if(network != null){
                Transport transport = network.getTransport();
                if(transport != null){
                    if(transport.getDestination() != null && transport.getSource() != null){
                        if(port.equals(transport.getDestination().getMeaning()) || port.equals(transport.getSource().getMeaning())) isPort = true;
                    }
                }
            }
        }
        if(!isPort) return false;

        //mac
        String mac = SnifferFrame.filter.mac;
        if(mac.equals("")) isMac = true;
        else{
            if(mac.equals(packet.getBase().getSourceMac()) || mac.equals(packet.getBase().getDestinationMac())) isMac = true;
        }
        if(!isMac) return false;

        //TCP流
        String tcpStream = SnifferFrame.filter.streamIndex;
        if(tcpStream.equals("")) isStream = true;
        Network network = packet.getDataLink().getNetwork();
        if(network != null){
            Transport transport = network.getTransport();
            if(transport != null){
                if(transport.name.equals("TCP")){
                    TCP tcp = (TCP) transport;
                    if(String.valueOf(tcp.getStreamIndex()).equals(tcpStream)) isStream = true;
                }
            }
        }
        if(!isStream) return false;

        return true;
    }

    public static void filterAll(){
        for(MyPacket packet: Main.packetList){
            if(filter(packet)) SnifferFrame.packetModel.addRow(packet.getBase().getVector());
        }
    }
}
