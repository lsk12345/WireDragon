package com.lsk.app;

import com.lsk.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class WelcomeFrame extends JFrame {
    //JPanel panel;
    JLabel imageLabel;
    JLabel textLabel;
    String textToShow = "Wire-Dragon,中国人自己的嗅探器";
    private int currentIndex = 0;

    public WelcomeFrame() {
        setTitle("WireDragon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //自适应窗口大小
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);
        //add(panel);
        // 图片部分
        ClassLoader classLoader = getClass().getClassLoader();
        java.net.URL imageURL = classLoader.getResource("welcome.jpg");
        ImageIcon originalImage=new ImageIcon();
        if (imageURL != null) {
            // 创建图标对象
            originalImage= new ImageIcon(imageURL);
        }
        //ImageIcon originalImage = new ImageIcon("src/main/resources/welcome.jpg"); // 替换为您的图像路径
        Image scaledImage = getScaledImage(originalImage.getImage(), getWidth()/2, getHeight()/2); // 调整图像大小
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        imageLabel = new JLabel(scaledIcon);
        add(imageLabel, BorderLayout.NORTH);

        // 文本部分
        JPanel panel = new JPanel(new GridLayout(2, 1));
        textLabel = new JLabel();
        textLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER); // 文本居中
        panel.add(textLabel);

        //开始按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("启动!");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MainFrame();
                setVisible(false);
            }
        });
        button.setPreferredSize(new Dimension(getWidth()/4, getHeight()/8));
        button.setFont(new Font("Serif", Font.PLAIN, 30));
        buttonPanel.add(button);
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);


        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < textToShow.length()) {
                    textLabel.setText(textLabel.getText() + textToShow.charAt(currentIndex));
                    currentIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }
    private Image getScaledImage(Image src, int maxWidth, int maxHeight) {
        int originalWidth = src.getWidth(null);
        int originalHeight = src.getHeight(null);

        // 计算缩放比例，确保宽度和高度都不超过目标尺寸
        double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        return resizedImage;
    }
}
