package com.lsk.protocol.network;

import com.lsk.util.Utils;
import com.lsk.model.Field;

import javax.swing.table.DefaultTableModel;

public class IPv4 extends Network{
    //版本,这里肯定是v4
    private Field version;
    //报文头长度,单位为4bytes
    private Field headerLength;
    //区分服务,前6b区分优先级DSCP,后2b是ECN
    private Field differentiatedService;
    // IP 报文的长度，包括报文头部和数据部分
    private Field totalSize;
    //用于唯一标识一个 IP 数据包，通常用于分片和重组
    private Field identification;
    //标志,3b,用2b,第2位DF决定是否分片(1为不分片，0为分片),第3位MF该数据包后面是否还有分片(0为没有，1为有)
    private Field flag;
    //指示分片数据包在原始数据包中的位置
    private Field fragmentOffset;
    //生存时间
    private Field timeToLife;
    //下一个协议
    private Field protocol;
    //头部校验和
    private Field checkSum;

    public IPv4(char[] packetChars) {
        name = "IPv4";
        //0:版本
        this.version = new Field(packetChars[0]+"",Utils.toArray(packetChars[0]));

        //1:报文头长度
        this.headerLength = new Field(""+Utils.hexToInt(packetChars[1])*4+"bytes",Utils.toArray(packetChars[1]));

        //2-3:区分服务
        String binary = Utils.hexToBinary(packetChars[2]+""+packetChars[3]);
        long DSCP = Utils.binaryToInt(binary.substring(0, 6));
        long ECN = Utils.binaryToInt(binary.substring(6));
        this.differentiatedService = new Field("DSCP:"+DSCP+"/ECN:"+ECN,Utils.toArray(packetChars[2],packetChars[3]));

        //4-7:报文长度
        char[] char1 = Utils.slice(packetChars,4,8);
        long value = Utils.hexToInt(char1);
        this.totalSize = new Field(""+value,char1);

        //8-11:标识
        char[] char2 = Utils.slice(packetChars,8,12);
        value = Utils.hexToInt(char2);
        this.identification = new Field("0x"+String.valueOf(char2)+"("+value+")",char2);

        //12:标志:其实只有前两位，但方便表示
        binary = Utils.hexToBinary(packetChars[12]+"");
        String DF =( binary.charAt(1) == '1') ? "不分片":"分片";
        String MF = binary.charAt(2) == '1' ? "还有分片":"没有分片了";
        this.flag = new Field("DF:"+binary.charAt(1)+"("+DF+")/"+"MF:"+binary.charAt(2)+"("+MF+")",Utils.toArray(packetChars[12]));

        //13-15:切片偏移，还要加上上面的第4位
        char[] char3 = Utils.slice(packetChars,13,16);
        value = Utils.hexToInt(binary.charAt(3)+String.valueOf(char3));
        this.fragmentOffset = new Field(""+value,char3);

        //16-17:生存时间
        char[] char4 = Utils.toArray(packetChars[16],packetChars[17]);
        value = Utils.hexToInt(char4);
        this.timeToLife = new Field(""+value,char4);

        //18-19:传输层协议
        char[] char5 = Utils.toArray(packetChars[18],packetChars[19]);
        long pro = Utils.hexToInt(char5);
        String proName = Utils.getTransportProName(pro);
        this.protocol = new Field(proName+"("+pro+")",char5);

        //20-23:首部校验和
        char[] char6 = Utils.slice(packetChars,20,24);
        this.checkSum = new Field("0x"+String.valueOf(char6),char6);
        //24-31:源ip
        char[] char7 = Utils.slice(packetChars,24,32);
        this.setSource(new Field(Utils.getIPv4(char7),char7));
        //32-39:目的ip
        char[] char8 = Utils.slice(packetChars,32,40);
        this.setDestination(new Field(Utils.getIPv4(char8),char8));

        //进传输层
        this.setTransport(proName,Utils.slice(packetChars,40));
    }

    public void getUI(){

        model.addRow(Utils.fieldToObject("version",version));
        model.addRow(Utils.fieldToObject("headerLength",headerLength));
        model.addRow(Utils.fieldToObject("differentiatedService",differentiatedService));
        model.addRow(Utils.fieldToObject("totalSize",totalSize));
        model.addRow(Utils.fieldToObject("identification",identification));
        model.addRow(Utils.fieldToObject("flag",flag));
        model.addRow(Utils.fieldToObject("fragmentOffset",fragmentOffset));
        model.addRow(Utils.fieldToObject("timeToLife",timeToLife));
        model.addRow(Utils.fieldToObject("protocol",protocol));
        model.addRow(Utils.fieldToObject("checkSum",checkSum));
        model.addRow(Utils.fieldToObject("sourceIp",getSource()));
        model.addRow(Utils.fieldToObject("destinationIp",getDestination()));
    }

    public Field getVersion() {
        return version;
    }

    public void setVersion(Field version) {
        this.version = version;
    }

    public Field getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(Field headerLength) {
        this.headerLength = headerLength;
    }

    public Field getDifferentiatedService() {
        return differentiatedService;
    }

    public void setDifferentiatedService(Field differentiatedService) {
        this.differentiatedService = differentiatedService;
    }

    public Field getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Field totalSize) {
        this.totalSize = totalSize;
    }

    public Field getIdentification() {
        return identification;
    }

    public void setIdentification(Field identification) {
        this.identification = identification;
    }

    public Field getFlag() {
        return flag;
    }

    public void setFlag(Field flag) {
        this.flag = flag;
    }

    public Field getFragmentOffset() {
        return fragmentOffset;
    }

    public void setFragmentOffset(Field fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
    }

    public Field getTimeToLife() {
        return timeToLife;
    }

    public void setTimeToLife(Field timeToLife) {
        this.timeToLife = timeToLife;
    }

    public Field getProtocol() {
        return protocol;
    }

    public void setProtocol(Field protocol) {
        this.protocol = protocol;
    }

    public Field getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Field checkSum) {
        this.checkSum = checkSum;
    }
}
