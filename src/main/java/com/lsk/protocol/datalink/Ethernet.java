package com.lsk.protocol.datalink;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

public class Ethernet extends DataLink {

    public Ethernet(char[] packetChars) {
        name = "Ethernet";
        // 0-11:目的MAC地址
        char[] char1 = Utils.slice(packetChars,0,12);
        setDestination(new Field(Utils.getMac(char1),char1));

        // 12-23:源MAC地址
        char[] char2 = Utils.slice(packetChars,12,24);
        setSource(new Field(Utils.getMac(char2),char2));

        // 24-27:type,当这两个字节的值小于1536 (十六进制为0x0600)时代表该以太网中数据段的长度；如果这两个字节的值大于1536，则表示与以太网帧相关的MAC客户端协议的类型
        String type = String.valueOf(packetChars,24,4);
        int typeInt = Integer.parseInt(type, 16);
        char[] typeChar = Utils.slice(packetChars,24,28);
        String pro=Utils.unknown;
        if(typeInt<1536) this.setType(new Field("长度:"+ typeInt,typeChar));
        else{
            pro = Utils.getNetworkProtocol(type);
            this.setType(new Field(pro,typeChar));
        }
        //设置网络层
        this.setNetwork(pro,Utils.slice(packetChars,28));

    }

    public void getUI(){

        model.addRow(Utils.fieldToObject("destinationMac",getDestination()));
        model.addRow(Utils.fieldToObject("sourceMac",getSource()));
        model.addRow(Utils.fieldToObject("type",getType()));
    }

}
