package com.lsk.protocol.transport;

import com.lsk.model.Field;
import com.lsk.protocol.application.Application;
import com.lsk.protocol.application.DNS;
import com.lsk.protocol.application.HTTP;
import com.lsk.util.Utils;

import javax.swing.table.DefaultTableModel;

public class Transport {
    private Field destination;
    private Field source;
    private Application application;
    private Field data;

    public String name;
    public DefaultTableModel model;

    public Transport() {
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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }

    public void getUI(){

    }

    public void setApplication(String protocolName, char[] packetChars){
        //无对应协议或者解析不了，包的余下部分作为数据
        if(protocolName.equals(Utils.unknown)) {
            this.data = new Field(protocolName,packetChars);
            this.model.addRow(Utils.fieldToObject("payload",getData()));
        }
        else if(protocolName.equals("HTTP")){
            this.application = new HTTP(packetChars);
        }
        else if(protocolName.equals("DNS")){
            this.application = new DNS(packetChars);
        }
        //还没能解的包，先只写名字
        else{
            this.application = new Application(protocolName,packetChars);
        }
    }
}
