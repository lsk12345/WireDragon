package com.lsk.app;

import com.lsk.Main;
import com.lsk.model.Frame;
import com.lsk.model.MyPacket;
import com.lsk.protocol.application.Application;
import com.lsk.protocol.datalink.DataLink;
import com.lsk.protocol.network.Network;
import com.lsk.protocol.transport.Transport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PacketFrame extends JFrame {
    public PacketFrame(int index){
        init(index);
        //展示文字信息
        //showMeaning(index);
    }

    //初始化

    int width;
    int height;
    JSplitPane verticalSplitPane;
    JSplitPane upperHorizontalSplitPane;
    JSplitPane lowerHorizontalSplitPane;
    //选择大类
    JScrollPane upperLeftPanel;
    //每一个类的解包信息
    JScrollPane upperRightPanel;
    //byte信息
    JScrollPane lowerLeftPanel;
    //ASCII码信息
    JScrollPane lowerRightPanel;
    //赋值一个MyPacket
    MyPacket packet;
    public void init(int index){
        //赋值
        packet = Main.packetList.get(index);
        setLayout(new BorderLayout());
        setTitle("WireDragon-包"+(index));
        //自适应窗口大小
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setVisible(true);



        // 创建上下分割的JSplitPane
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerLocation(height/2/3*2); // 上下分割比例为2:1

        // 创建上部分左右分割的JSplitPane
        upperHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        upperHorizontalSplitPane.setDividerLocation(width/6); // 上部分左右分割比例为1:5

        // 创建下部分左右分割的JSplitPane
        lowerHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        lowerHorizontalSplitPane.setDividerLocation(width/2); // 下部分左右分割比例为1:1

        upperLeftPanel = new JScrollPane();
        upperRightPanel = new JScrollPane();
        lowerLeftPanel = new JScrollPane();
        lowerRightPanel = new JScrollPane();
        //选择大类:upperLeftPanel
        getLayerTable(index);
        getBytes();

        //刷新页面
        flush();

        // 将上下分割面板添加到窗口的内容面板中
        getContentPane().add(verticalSplitPane, BorderLayout.CENTER);

        // 添加ComponentListener来监听窗口大小变化
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // 获取窗口的高宽
                width = getWidth();
                height = getHeight();
                // 设置分隔位置为窗口宽度的比例
                verticalSplitPane.setDividerLocation(height/2/3*2); // 上下分割比例为2:1
                upperHorizontalSplitPane.setDividerLocation(width/6); // 上部分左右分割比例为1:5
                lowerHorizontalSplitPane.setDividerLocation(width/2); // 下部分左右分割比例为1:1
            }
        });
    }

    //获取字节
    JTextPane textPane;
    JTextPane textPane2;
    private void getBytes() {
        //byte
        textPane = new JTextPane();
        textPane.setText(packet.getPacketString());
        textPane.setEditable(false);
        Style style = textPane.addStyle("MyStyle", null);
        StyleConstants.setFontFamily(style, "Arial"); // 设置字体
        StyleConstants.setFontSize(style, 20); // 设置字体大小
        StyleConstants.setBold(style, true); // 设置粗体
        lowerLeftPanel = new JScrollPane(textPane);

        //ASCII
        textPane2 = new JTextPane();
        textPane2.setText(packet.getPacketASCII());
        textPane2.setEditable(false);
        Style style2 = textPane2.addStyle("MyStyle", null);
        StyleConstants.setFontFamily(style2, "Arial"); // 设置字体
        StyleConstants.setFontSize(style2, 20); // 设置字体大小
        StyleConstants.setBold(style2, true); // 设置粗体
        lowerRightPanel = new JScrollPane(textPane2);
    }


    //展示大类
    DefaultTableModel layerTableModel;
    JTable layerTable;
    Frame frame;
    DataLink dataLink;
    Network network;
    Transport transport;
    Application application;
    private void getLayerTable(int index) {
        // 创建左侧成员类列表
        layerTableModel = new DefaultTableModel();
        layerTableModel.addColumn("包 "+index+" 的解包信息");


        layerTableModel.addRow(new Object[]{"Frame"});
        frame = packet.getFrame();
        dataLink = packet.getDataLink();
        //层层寻找
        if(dataLink!=null){
            layerTableModel.addRow(new Object[]{dataLink.name});
            network = dataLink.getNetwork();
            if(network!=null){
                layerTableModel.addRow(new Object[]{network.name});
                transport =network.getTransport();
                if(transport!=null){
                    layerTableModel.addRow(new Object[]{transport.name});
                    application = transport.getApplication();
                    if(application!=null){
                        layerTableModel.addRow(new Object[]{application.name});
                    }else{
                        if(transport.getData()!=null) layerTableModel.addRow(new Object[]{"payload"});
                    }
                }else{
                    if(network.getData()!=null) layerTableModel.addRow(new Object[]{"payload"});
                }
            }else{
                if(dataLink.getData()!=null) layerTableModel.addRow(new Object[]{"payload"});
            }
        }else{
            if(packet.getData()!=null) layerTableModel.addRow(new Object[]{"payload"});
        }


        layerTable = ComponentUtils.getMyTable(layerTableModel);

        //添加点击条件
        layerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = layerTable.getSelectedRow();
                if (selectedRow != -1) {
                    //String value = (String) layerTableModel.getValueAt(selectedRow, 0); // 假设第一列是字符串数据
                    getDetailTable(selectedRow);
                }
            }
        });

        upperLeftPanel = new JScrollPane(layerTable);
    }

    //具体内容
    //DefaultTableModel detailTableModel;
    JTable detailTable;
    private void getDetailTable(int selectedRow){
        // 实现获取所选类的详细信息的逻辑
        if(selectedRow==0){
            frame.getUI();
            detailTable = ComponentUtils.getMyTable(frame.model);
        }
        else if(selectedRow==1){
            dataLink.getUI();
            detailTable = ComponentUtils.getMyTable(dataLink.model);
        }
        else if(selectedRow==2){
            network.getUI();
            detailTable = ComponentUtils.getMyTable(network.model);
        }
        else if(selectedRow==3){
            transport.getUI();
            detailTable = ComponentUtils.getMyTable(transport.model);
        }
        else if(selectedRow==4){
            application.getUI();
            detailTable = ComponentUtils.getMyTable(application.model);
        }
        upperRightPanel = new JScrollPane(detailTable);
        flush();
    }

    private void flush(){
        // 将面板添加到左右分割面板中
        upperHorizontalSplitPane.setLeftComponent(upperLeftPanel);
        upperHorizontalSplitPane.setRightComponent(upperRightPanel);

        lowerHorizontalSplitPane.setLeftComponent(lowerLeftPanel);
        lowerHorizontalSplitPane.setRightComponent(lowerRightPanel);

        // 将左右分割面板添加到上下分割面板中
        verticalSplitPane.setTopComponent(upperHorizontalSplitPane);
        verticalSplitPane.setBottomComponent(lowerHorizontalSplitPane);
        // 获取窗口的高宽
        width = getWidth();
        height = getHeight();
        // 设置分隔位置为窗口宽度的比例
        verticalSplitPane.setDividerLocation(height/2/3*2); // 上下分割比例为2:1
        upperHorizontalSplitPane.setDividerLocation(width/6); // 上部分左右分割比例为1:5
        lowerHorizontalSplitPane.setDividerLocation(width/2); // 下部分左右分割比例为1:1
    }
}
