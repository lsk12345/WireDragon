package com.lsk.app;

import com.lsk.Capture;
import com.lsk.Main;
import com.lsk.filter.Filter;
import com.lsk.handler.HandlerModel;
import com.lsk.model.MyPacket;
import com.lsk.protocol.network.Network;
import com.lsk.protocol.transport.TCP;
import com.lsk.protocol.transport.Transport;
import com.lsk.util.Utils;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SnifferFrame extends JFrame {
    public static Filter filter = new Filter();
    int deviceIndex;
    PcapIf device;
    public SnifferFrame(int deviceIndex,PcapIf device){
        this.deviceIndex = deviceIndex;
        this.device = device;
        init(deviceIndex);
        getPacket(device);
    }


    //首页
    Container snifferPane = getContentPane();
    //初始化
    public void init(int deviceIndex){
        snifferPane.setLayout(new BorderLayout());
        setTitle("WireDragon-网卡"+(deviceIndex+1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //自适应窗口大小
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);
        //添加菜单栏
        menuBar();
    }
    //菜单栏
    JMenuBar menuBar = new JMenuBar();
    //暂停启动条
    JButton startButton = new JButton("暂停");
    //下拉选择协议
    JComboBox<String> protocolBox = new JComboBox<String>(Utils.supportedProtocol);
    //IP端口筛选
    JLabel label1 = new JLabel("IP筛选：");
    JTextField IP=new JTextField("");
    JLabel label2 = new JLabel("端口筛选：");
    JTextField port=new JTextField("");
    JLabel label3 = new JLabel("mac筛选：");
    JTextField mac=new JTextField("");
    JLabel label4 = new JLabel("TCP流编号：");
    JTextField stream=new JTextField("");
    JButton confirmButton = new JButton("确认筛选");
    //TCP流处理用的
    JPopupMenu popupMenu = new JPopupMenu();
    String streamIndex;

    //加载条
    JProgressBar progressBar = new JProgressBar();
    private void menuBar() {
        // 启动按钮
        ActionListener startButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!Main.isPaused){
                    startButton.setText("启动");
                    progressBar.setString("暂停中");
                    Main.isPaused = true;
                }
                else{
                    startButton.setText("暂停");
                    progressBar.setString("抓包中");
                    Main.isPaused = false;
                }

            }
        };
        startButton.addActionListener(startButtonListener);
        menuBar.add(startButton);

        //协议过滤
        protocolBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理选择事件的代码
                filter.protocol = (String) protocolBox.getSelectedItem();
                flushPacket();
            }
        });
        menuBar.add(protocolBox);

        //ip,port,mac过滤
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filter.mac = mac.getText();
                filter.IP = IP.getText();
                filter.port = port.getText();
                filter.streamIndex = stream.getText();
                flushPacket();
            }
        });
        menuBar.add(label1);
        menuBar.add(IP);
        menuBar.add(label2);
        menuBar.add(port);
        menuBar.add(label3);
        menuBar.add(mac);
        menuBar.add(label4);
        menuBar.add(stream);
        menuBar.add(confirmButton);

        JMenuItem item = new JMenuItem("TCP流");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filter.streamIndex = streamIndex;
                stream.setText(streamIndex);
                flushPacket();
            }
        });
        //TCP流，如果是TCP协议，右键弹出框来
        popupMenu.add(item);
        packetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = packetTable.rowAtPoint(e.getPoint());
                    //获取StreamIndex
                    String value= (String) packetModel.getValueAt(row, 0);
                    final int index=Integer.parseInt(value);
                    MyPacket mypacket = Main.packetList.get(index);
                    Network network= mypacket.getDataLink().getNetwork();
                    if(network!=null){
                        Transport transport = network.getTransport();
                        if(transport!=null){
                            if(transport.name.equals("TCP")){
                                packetTable.getSelectionModel().setSelectionInterval(row, row);
                                popupMenu.show(packetTable, e.getX(), e.getY());
                                TCP tcp = (TCP) transport;
                                streamIndex = String.valueOf(tcp.getStreamIndex());
                            }
                        }
                    }
                }
            }
        });


        //加载框
        progressBar.setIndeterminate(true); // 设置为不确定进度
        progressBar.setStringPainted(true); // 显示进度文本
        progressBar.setString("初始化中...");
        //menuBar.add(progressBar);
        snifferPane.add(new JScrollPane(progressBar), BorderLayout.NORTH);
        setJMenuBar(menuBar);
    }


    //用来渲染的表格模型
    public static DefaultTableModel packetModel = new DefaultTableModel();
    JTable packetTable = ComponentUtils.getMyTable(
            packetModel,
            new Font("", Font.PLAIN, 16),
            30);
    String[] baseNames = {"NO.", "时间", "源mac","目的mac","源IP","目的IP","最高协议","协议链","大小(bytes)"};
    //抓包
    Thread thread;
    Timer timer;

    private void getPacket(PcapIf device) {
        //多线程抓包
        Capture capture= new Capture(device,new HandlerModel(device));
        thread = new Thread(capture);
        // 创建一个定时器，每隔一定时间更新表格数据
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 刷新表格数据
                if(Main.packetList.size()!=0 && progressBar.getString().equals("初始化中...")){
                    progressBar.setIndeterminate(false);
                    progressBar.setString("抓包中");
                    //System.out.println("出包了!");
                }
                //System.out.println(Main.packetList);
                //System.out.println(packetModel.getRowCount());
            }
        });
        packetModel.setColumnIdentifiers(baseNames);
        snifferPane.add(new JScrollPane(packetTable), BorderLayout.CENTER);
        //绑定跳转
        gotoPacket();
        //设置为守护进程
        thread.setDaemon(true);
        thread.start();
        timer.start();
    }


    private void flushPacket() {
        //删除所有信息先
        packetModel.setRowCount(0);
        Filter.filterAll();
    }

    private void gotoPacket() {
        packetTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = packetTable.getSelectedRow();
                if (selectedRow != -1 && e.getClickCount() == 2) {
                    String value = (String) packetModel.getValueAt(selectedRow, 0);
                    final int index=Integer.parseInt(value);
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                new PacketFrame(index);
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
