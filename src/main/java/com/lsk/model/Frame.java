package com.lsk.model;

import com.lsk.util.Utils;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;

import javax.swing.table.DefaultTableModel;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Frame {
    //当前包所在该次嗅探中的index
    private long number;
    //网卡
    private PcapIf device;
    //封装类型，链路层协议
    private String encapsulationType;
    //到达时间
    private Time time;
    //包的大小（字节为单位）
    private Integer length;
    //顶层协议
    private String protocol;

    public DefaultTableModel model;

    public Frame() {
    }

    public Frame(PcapPacket packet,PcapIf device) {
        this.number = packet.getFrameNumber();
        this.device = device;
        this.encapsulationType = "Ethernet";
        this.time = new Time(packet.getCaptureHeader().timestampInMillis());
        this.length = packet.getTotalSize();
        this.protocol = Utils.getProtocol(packet);
    }

    public void getUI(){
        this.model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        this.model.addRow(new Object[]{"number",getNumber()});
        this.model.addRow(new Object[]{"device",getDevice().getName()});
        this.model.addRow(new Object[]{"encapsulationType",getEncapsulationType()});
        this.model.addRow(new Object[]{"time",getTime().toString()});
        this.model.addRow(new Object[]{"length",getLength().toString()});
        this.model.addRow(new Object[]{"protocol",getProtocol()});
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public PcapIf getDevice() {
        return device;
    }

    public void setDevice(PcapIf device) {
        this.device = device;
    }

    public String getEncapsulationType() {
        return encapsulationType;
    }

    public void setEncapsulationType(String encapsulationType) {
        this.encapsulationType = encapsulationType;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
