package com.lsk.protocol.application;

import com.lsk.model.Field;
import com.lsk.util.Utils;

import javax.swing.table.DefaultTableModel;

public class DNS extends Application{
    public DNS(char[] packetChars) {
        this.setData(new Field("data",packetChars));
        name = "DNS";
    }

    public void getUI(){
        char[] packetChars = getData().getBytes();
        if(packetChars==null ||packetChars.length==0) return;

        model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");
        model.addRow(new Object[]{"transactionId","0x"+String.valueOf(Utils.slice(packetChars,0,4))});
        model.addRow(new Object[]{"flags","0x"+String.valueOf(Utils.slice(packetChars,4,8))});
        model.addRow(new Object[]{"questions",Utils.hexToInt(Utils.slice(packetChars,8,12))});
        model.addRow(new Object[]{"answerRRs",Utils.hexToInt(Utils.slice(packetChars,12,16))});
        model.addRow(new Object[]{"authorityRRs",Utils.hexToInt(Utils.slice(packetChars,16,20))});
        model.addRow(new Object[]{"additionalRRs",Utils.hexToInt(Utils.slice(packetChars,20,24))});
        model.addRow(new Object[]{"Queries",Utils.slice(packetChars,24)});
    }
}
