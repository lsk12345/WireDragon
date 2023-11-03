package com.lsk.model;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;

public class Device {
    //寻找所有网卡
    public List<PcapIf> findAllDev(){
        List<PcapIf> devList=new ArrayList<PcapIf>();
        StringBuilder error = new StringBuilder();
        int status = Pcap.findAllDevs(devList,error);
        if(devList.size() == 0 || status == -1) System.out.println("获取网卡失败: "+error);
        return devList;
    }

    public String[][] deviceToString(List<PcapIf> deviceList){
        String[][] deviceString = new String[deviceList.size()][3];
        for(int i=0;i<deviceList.size();i++){
            deviceString[i][0]=(i+1)+"";
            deviceString[i][1]=deviceList.get(i).getDescription();
            deviceString[i][2]=deviceList.get(i).getAddresses().get(0).getAddr().toString();
        }
        return deviceString;
    }
}
