package com.lsk.protocol.datalink;

import com.lsk.model.Field;
import com.lsk.protocol.network.ARP;
import com.lsk.protocol.network.IPv4;
import com.lsk.protocol.network.IPv6;
import com.lsk.protocol.network.Network;
import com.lsk.util.Utils;

import javax.swing.table.DefaultTableModel;

public class DataLink {
    private Field destination;
    private Field source;
    private Field type;
    private Network network;
    private Field data;

    public String name;
    public DefaultTableModel model;

    public DataLink() {
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

    public Field getType() {
        return type;
    }

    public void setType(Field type) {
        this.type = type;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetwork(String protocolName, char[] packetChars){
        if(packetChars.length==0) return;
        if(protocolName.equalsIgnoreCase("Ipv4")) this.network = new IPv4(packetChars);
        else if(protocolName.equalsIgnoreCase("Ipv6")) this.network = new IPv6(packetChars);
        else if(protocolName.equalsIgnoreCase("ARP")) this.network = new ARP(packetChars);
        //无对应协议或者解析不了，包的余下部分作为数据
        else{
            this.data = new Field(protocolName,packetChars);
            this.model.addRow(Utils.fieldToObject("payload",getData()));
        }
    }

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }

    public void getUI(){
        model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        model.addColumn("报文字节");
    }
}
