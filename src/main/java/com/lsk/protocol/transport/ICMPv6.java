package com.lsk.protocol.transport;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class ICMPv6 extends Transport{
    //类型
    private Field type;
    //代码
    private Field code;
    //检验和
    private Field checkSum;
    public ICMPv6(char[] packetChars) {
        name = "ICMPv6";
        //0-1:类型
        char[] char1 = Utils.toArray(packetChars[0],packetChars[1]);
        this.type = new Field(Utils.getICMPv6Type(Utils.hexToInt(char1)),char1);
        //2-3:代码
        char[] char2 = Utils.toArray(packetChars[2],packetChars[3]);
        this.code = new Field(Utils.hexToInt(char2)+"",char2);
        //4-7:检验和
        char[] char3 = Utils.slice(packetChars,4,8);
        this.checkSum = new Field("0x"+String.valueOf(char3),char3);

        //应用层
        this.setApplication(Utils.getApplicationName(-1),Utils.slice(packetChars,8));
    }

    public void getUI(){

        model.addRow(Utils.fieldToObject("type",type));
        model.addRow(Utils.fieldToObject("code",code));
        model.addRow(Utils.fieldToObject("checkSum",checkSum));
    }

    public Field getType() {
        return type;
    }

    public void setType(Field type) {
        this.type = type;
    }

    public Field getCode() {
        return code;
    }

    public void setCode(Field code) {
        this.code = code;
    }

    public Field getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Field checkSum) {
        this.checkSum = checkSum;
    }
}
