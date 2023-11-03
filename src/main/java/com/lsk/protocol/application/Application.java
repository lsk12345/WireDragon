package com.lsk.protocol.application;

import com.lsk.model.Field;
import com.lsk.util.Utils;

import javax.swing.table.DefaultTableModel;

public class Application {
    private Field data;


    public String name;
    public DefaultTableModel model;

    public Application() {
        model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        model.addColumn("报文字节");
    }
    //知道协议还没解的包
    public Application(String protocolName,char[] packetChars) {
        name = protocolName;
        model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        model.addColumn("报文字节");
        this.data = new Field(protocolName,packetChars);
        this.model.addRow(Utils.fieldToObject("payload",getData()));
    }

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }

    public void getUI(){
        
    }

}
