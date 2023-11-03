package com.lsk.protocol.network;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class IPv6 extends Network{
    //版本,这里肯定是v6
    private Field version;
    //流量类别
    private Field trafficClass;
    //流标签
    private Field flowLabel;
    //负载长度
    private Field payloadLength;
    //下一个协议
    private Field protocol;
    //跳数限制
    private Field hopLimit;

    public IPv6(char[] packetChars) {
        name = "IPv6";
        //0:版本
        this.version = new Field(packetChars[0]+"", Utils.toArray(packetChars[0]));
        //1-2:流量类别
        String binary = Utils.hexToBinary(packetChars[1]+""+packetChars[2]);
        long DSCP = Utils.binaryToInt(binary.substring(0, 6));
        long ECN = Utils.binaryToInt(binary.substring(6));
        this.trafficClass = new Field("DSCP:"+DSCP+"/ECN:"+ECN,Utils.toArray(packetChars[1],packetChars[2]));
        //3-7:流标签，全0
        char [] char1 =Utils.slice(packetChars,3,8);
        this.flowLabel = new Field("0x"+String.valueOf(char1),char1);
        //8-11:负载长度
        char[] char2 = Utils.slice(packetChars,8,12);
        this.payloadLength = new Field(Utils.hexToInt(char2)+"",char2);
        //12-13:传输层协议
        char[] char3 = Utils.toArray(packetChars[12],packetChars[13]);
        long pro = Utils.hexToInt(char3);
        String proName = Utils.getTransportProName(pro);
        this.protocol = new Field(proName+"("+pro+")",char3);
        //14-15:跳数限制
        char[] char4 = Utils.toArray(packetChars[14],packetChars[15]);
        this.hopLimit = new Field(Utils.hexToInt(char4)+"",char4);
        //16-47:源地址
        char[] char5 = Utils.slice(packetChars,16,48);
        this.setSource(new Field(Utils.getIPv6(char5),char5));
        //48-80:目的ip
        char[] char6 = Utils.slice(packetChars,48,80);
        this.setDestination(new Field(Utils.getIPv6(char6),char6));
        //进传输层
        this.setTransport(proName,Utils.slice(packetChars,80));

    }

    public void getUI(){

        model.addRow(Utils.fieldToObject("version",version));
        model.addRow(Utils.fieldToObject("trafficClass",trafficClass));
        model.addRow(Utils.fieldToObject("flowLabel",flowLabel));
        model.addRow(Utils.fieldToObject("payloadLength",payloadLength));
        model.addRow(Utils.fieldToObject("protocol",protocol));
        model.addRow(Utils.fieldToObject("hopLimit",hopLimit));
        model.addRow(Utils.fieldToObject("sourceIp",getSource()));
        model.addRow(Utils.fieldToObject("destinationIp",getDestination()));
    }
    public Field getVersion() {
        return version;
    }

    public void setVersion(Field version) {
        this.version = version;
    }

    public Field getTrafficClass() {
        return trafficClass;
    }

    public void setTrafficClass(Field trafficClass) {
        this.trafficClass = trafficClass;
    }

    public Field getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(Field flowLabel) {
        this.flowLabel = flowLabel;
    }

    public Field getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(Field payloadLength) {
        this.payloadLength = payloadLength;
    }

    public Field getProtocol() {
        return protocol;
    }

    public void setProtocol(Field protocol) {
        this.protocol = protocol;
    }

    public Field getHopLimit() {
        return hopLimit;
    }

    public void setHopLimit(Field hopLimit) {
        this.hopLimit = hopLimit;
    }
}
