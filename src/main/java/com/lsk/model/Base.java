package com.lsk.model;

import com.lsk.util.Utils;
import org.jnetpcap.packet.PcapPacket;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class Base {
    //当前包所在该次嗅探中的index
    private Long number;
    //到达主机的时间
    private Time time;
    //源mac地址
    private String sourceMac;
    //目的mac地址
    private String destinationMac;
    //源ip地址
    private String sourceIp;
    //目的ip地址
    private String destinationIp;
    //最高层协议类型
    private String protocol;
    //包大小(byte)
    private Integer length;
    //详细信息
    private String info;
    private String protocols;

    public Base() {
    }

    public Base(PcapPacket packet,Long number){
        this.number = number;
        this.time = new Time(packet.getCaptureHeader().timestampInMillis());
        this.sourceMac = Utils.getSourceMac(packet);
        this.destinationMac = Utils.getDestinationMac(packet);
        this.sourceIp = Utils.getSourceIp(packet);
        this.destinationIp = Utils.getDestinationIp(packet);
        this.protocol = Utils.getProtocol(packet);
        this.length = packet.getTotalSize();
        //后续获取
        this.info = "";
    }

    public Object[] getVector(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return new Object[]{number+"",sdf.format(time),sourceMac,destinationMac,sourceIp,destinationIp,protocol,protocols,length+""};
    }
    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public void setSourceMac(String sourceMac) {
        this.sourceMac = sourceMac;
    }

    public String getDestinationMac() {
        return destinationMac;
    }

    public void setDestinationMac(String destinationMac) {
        this.destinationMac = destinationMac;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }
}
