package com.lsk.protocol.network;

import com.lsk.protocol.transport.*;
import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

import static com.lsk.Main.TCPStreamList;

public class Network {
    private Field destination;
    private Field source;
    private Transport transport;
    private Field data;

    public String name;
    public DefaultTableModel model;

    public Network() {
        model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        model.addColumn("报文字节");
    }

    public Field getDestination() {
        return destination;
    }

    public void setDestination(Field destination) {
        this.destination = destination;
    }

    public Field getSource() {
        return source;
    }

    public void setSource(Field source) {
        this.source = source;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }

    public void setTransport(String protocolName, char[] packetChars) {
        if(packetChars.length==0) return;
        if(protocolName.equalsIgnoreCase("TCP")){
            TCP tcp = new TCP(packetChars);
            TCPStream tcpStream = new TCPStream(
                    this.destination.getMeaning()+tcp.getDestination().getMeaning(),
                    this.source.getMeaning()+tcp.getSource().getMeaning(),
                    Long.parseLong(tcp.getSequenceNumber().getMeaning()),
                    Long.parseLong(tcp.getAcknowledgeNumber().getMeaning()));
            //处理数据流标号
            if(TCPStreamList.size()==0){
                TCPStreamList.add(tcpStream);
                tcp.setStreamIndex(0);
            }
            else{
                boolean flag = true;
                for(int i=0 ; i<TCPStreamList.size() ; i++){
                    if(TCPStreamList.get(i).isDead()) continue;
                    if(TCPStreamList.get(i).isAssociation(tcpStream)){
                        //找到上一个的流了，更新序号和确认号
                        tcp.setStreamIndex(i);
                        TCPStreamList.get(i).setAcknowledgeNumber(tcpStream.getAcknowledgeNumber());
                        TCPStreamList.get(i).setSequenceNumber(tcpStream.getSequenceNumber());

                        String binary = Utils.hexToBinary(new String(tcp.getFlag().getBytes()));
                        //如果isFin了，再接收到一个ACK=1就结束了
                        if(TCPStreamList.get(i).isFin() &&binary.charAt(7)=='1') TCPStreamList.get(i).setDead(true);
                        //如果Fin=1，置Fin为1
                        if(binary.charAt(11)=='1')TCPStreamList.get(i).setFin(true);
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    TCPStreamList.add(tcpStream);
                    tcp.setStreamIndex(TCPStreamList.size()-1);
                }
            }
            this.transport = tcp;
        }
        else if(protocolName.equalsIgnoreCase("ICMP")) this.transport = new ICMP(packetChars);
        else if(protocolName.equalsIgnoreCase("ICMPv6")) this.transport = new ICMPv6(packetChars);
        else if(protocolName.equalsIgnoreCase("UDP")) this.transport = new UDP(packetChars);
        //无对应协议或者解析不了，包的余下部分作为数据
        else{
            this.data = new Field(protocolName,packetChars);
            this.model.addRow(Utils.fieldToObject("payload",getData()));
        }
    }

    public void getUI(){
    }
}
