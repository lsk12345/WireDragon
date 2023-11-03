package com.lsk.app;

import com.lsk.model.Device;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainFrame extends JFrame {


    public MainFrame() {
        init();
        getDevice();
    }

    //首页
    Container mainPane = getContentPane();
    //初始化
    public void init(){
        mainPane.setLayout(new BorderLayout());
        setTitle("WireDragon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //自适应窗口大小
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);
    }

    //网卡的列表
    List<PcapIf> deviceList;
    DefaultTableModel deviceModel;
    JTable deviceTable;
    String[] columnNames = {"网卡", "描述", "地址"};
    //获取网卡信息
    private void getDevice() {
        Device device =new Device();
        deviceList = device.findAllDev();
        Object[][] data = device.deviceToString(deviceList);
        deviceModel = new DefaultTableModel(data, columnNames);
        deviceTable = ComponentUtils.getMyTable(
                deviceModel,
                new Font("", Font.PLAIN, 16),
                30,
                new Font("", Font.PLAIN, 16),
                30);

        mainPane.add(new JScrollPane(deviceTable), BorderLayout.CENTER);
        //绑定双击跳转
        gotoSniffer();
    }


    //点击网卡跳转
    private void gotoSniffer() {
        deviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = deviceTable.getSelectedRow();
                if (selectedRow != -1 && e.getClickCount() == 2) {
                    String value = (String) deviceModel.getValueAt(selectedRow, 0);
                    final int index=Integer.parseInt(value)-1;
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                setVisible(false);
                                new SnifferFrame(index,deviceList.get(index));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

}
