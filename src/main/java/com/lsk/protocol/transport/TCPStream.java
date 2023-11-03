package com.lsk.protocol.transport;

public class TCPStream {
    //将某一次连接的所有包归类，主要观察IP地址和port，对于序号和确认号存在关联的也是同一个，最后FIN=1后的ACK=1表示关闭
    //表示源或者目的ipPort
    private String ipPort1;
    private String ipPort2;
    private long sequenceNumber;
    private long acknowledgeNumber;
    private boolean isFin;
    private boolean isDead;

    public TCPStream(String ipPort1, String ipPort2, long sequenceNumber, long acknowledgeNumber) {
        this.ipPort1 = ipPort1;
        this.ipPort2 = ipPort2;
        this.sequenceNumber = sequenceNumber;
        this.acknowledgeNumber = acknowledgeNumber;
        this.isFin = false;
        this.isDead = false;
    }

    public boolean isAssociation(TCPStream newOne){
        //源和目的都对的上
        if( (this.ipPort1.equals(newOne.ipPort1) && this.ipPort2.equals(newOne.ipPort2)) ||  (this.ipPort1.equals(newOne.ipPort2) && this.ipPort2.equals(newOne.ipPort1)) ){
            //对比序号和确认号
            return isEqual(this.sequenceNumber, this.acknowledgeNumber, newOne.sequenceNumber, newOne.acknowledgeNumber);
        }
        return false;
    }

    private boolean isEqual(long a1, long a2, long b1, long b2) {
        return a1==b1 || a1==b1+1 || a1==b1-1 ||
               a1==b2 || a1==b2+1 || a1==b2-1 ||
               a2==b1 || a2==b1+1 || a2==b1-1 ||
               a2==b2 || a2==b2+1 || a2==b2-1;
    }

    public String getIpPort1() {
        return ipPort1;
    }

    public void setIpPort1(String ipPort1) {
        this.ipPort1 = ipPort1;
    }

    public String getIpPort2() {
        return ipPort2;
    }

    public void setIpPort2(String ipPort2) {
        this.ipPort2 = ipPort2;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    public void setAcknowledgeNumber(long acknowledgeNumber) {
        this.acknowledgeNumber = acknowledgeNumber;
    }

    public boolean isFin() {
        return isFin;
    }

    public void setFin(boolean fin) {
        isFin = fin;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

}
