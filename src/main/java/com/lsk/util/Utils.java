package com.lsk.util;

import com.lsk.model.Field;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final Ethernet eth = new Ethernet();
    //网络层
    public static final Ip4 ip4 = new Ip4();
    public static final Ip6 ip6 = new Ip6();
    public static final String unknown = "未知";
    //获取源mac
    public static String getSourceMac(PcapPacket packet) {
        // 如果packet有eth头部
        if (packet.hasHeader(eth)) return FormatUtils.mac(eth.source());
        return unknown;
    }
    //获取目的mac
    public static String getDestinationMac(PcapPacket packet) {
        // 如果packet有eth头部
        if (packet.hasHeader(eth)) return FormatUtils.mac(eth.destination());
        return unknown;
    }
    //获取源ip
    public static String getSourceIp(PcapPacket packet) {
        // 优先返回ipv4
        if (packet.hasHeader(ip4)) return FormatUtils.ip(ip4.source());
        if (packet.hasHeader(ip6)) return FormatUtils.ip(ip6.source());
        return unknown;
    }
    //获取目的ip
    public static String getDestinationIp(PcapPacket packet) {
        // 优先返回ipv4
        if (packet.hasHeader(ip4)) return FormatUtils.ip(ip4.destination());
        if (packet.hasHeader(ip6)) return FormatUtils.ip(ip6.destination());
        return unknown;
    }
    //获得顶层协议
    public static String getProtocol(PcapPacket packet) {
        //逆向遍历协议表找到最精确（最高层）的协议名
        JProtocol[] protocols = JProtocol.values();
        for (int i = protocols.length - 1; i >= 0; i--) {
            if (packet.hasHeader(protocols[i].getId())) {
                return protocols[i].name();
            }
        }
        return unknown;
    }
    //packet的所有byte转换成char[]
    public static char[] getPacketCharList(PcapPacket packet){
        byte[] bytes = packet.getByteArray(0, packet.size());
        //一个byte两个hex
        char[] byteChar = new char[bytes.length*2];
        for(int i=0;i<bytes.length;i++){
            String raw = Integer.toHexString(bytes[i]);
            //长度1，补0
            if(raw.length()== 1){
                byteChar[i*2] = '0';
                byteChar[i*2+1] = raw.charAt(0);
            }
            //长度大于2，取最后两位
            else if(raw.length()> 2){
                raw = raw.substring(raw.length() - 2);
                byteChar[i*2] = raw.charAt(0);
                byteChar[i*2+1] = raw.charAt(1);
            }
            else{
                byteChar[i*2] = raw.charAt(0);
                byteChar[i*2+1] = raw.charAt(1);
            }
        }
        return byteChar;
    }

    public static String getICMPType(long type) {
        String result = type+"";
        switch ((int) type) {
            case 0:
                result += "(Echo Reply)";
                break;
            case 3:
                result += "(Destination Unreachable)";
                break;
            case 8:
                result += "(Echo Request)";
                break;
            case 11:
                result += "(Time Exceeded)";
                break;
            case 12:
                result += "(Parameter Problem)";
                break;
            case 13:
                result += "(Timestamp Request)";
                break;
            case 14:
                result += "(Timestamp Reply)";
                break;
            case 17:
                result += "(Address Mask Request)";
                break;
            case 18:
                result += "(Address Mask Reply)";
                break;
            default:
                result = unknown;
        }
        return result;
    }

    public static String getICMPv6Type(long type) {
        String result = type+"";
        switch ((int) type) {
            case 1:
                result += "(Destination Unreachable)";
                break;
            case 2:
                result += "(Packet Too Big)";
                break;
            case 3:
                result += "(Time Exceeded)";
                break;
            case 4:
                result += "(Parameter Problem)";
                break;
            case 128:
                result += "(Echo Request)";
                break;
            case 129:
                result += "(Echo Reply)";
                break;
            case 135:
                result += "(Neighbor Solicitation)";
                break;
            case 136:
                result += "(Neighbor Advertisement)";
                break;
            case 137:
                result += "(Router Solicitation)";
                break;
            case 138:
                result += "(Router Advertisement)";
                break;
            case 139:
                result += "(Redirect)";
                break;
            case 130:
                result += "(Multicast Listener Query)";
                break;
            case 131:
                result += "(Multicast Listener Report)";
                break;
            case 132:
                result += "(Multicast Listener Done)";
                break;
            default:
                result = unknown;
        }
        return result;
    }

    public static String getApplicationName(int port) {
        String result;
        switch (port) {
            case 21:
                result = "FTP";
                break;
            case 22:
                result = "SSH";
                break;
            case 23:
                result = "Telnet";
                break;
            case 25:
                result = "DNS";
                break;
            case 69:
                result = "TFTP";
                break;
            case 80:
                result = "HTTP";
                break;
            case 110:
                result = "POP3";
                break;
            case 123:
                result = "NTP";
                break;
            case 143:
                result = "IMAP";
                break;
            case 161:
                result = "SNMP";
                break;
            case 389:
                result = "LDAP";
                break;
            case 443:
                result = "HTTPS";
                break;
            case 989:
            case 990:
                result = "FTPS";
                break;
            case 3389:
                result = "RDP ";
                break;
            default:
                result = unknown;
        }
        return result;
    }
    public static String[] supportedProtocol = {
            "全部",//默认选择全部
            "Ethernet","IPv4","IPv6","ARP","ICMP","ICMPv6","TCP","UDP",
            "HTTP","DNS","FTP", "SSH", "Telnet", "TFTP", "POP3", "NTP", "IMAP", "SNMP", "LDAP", "HTTPS", "FTPS", "RDP"
    };
    //用来渲染
    public static Object[] fieldToObject(String name, Field field) {
        return new Object[]{name,field.getMeaning(),charToString(field.getBytes())};
    }
    //TCP流单独处理
    public static Object[] fieldToObject(String name, long streamIndex) {
        return new Object[]{name,streamIndex,""};
    }

    public static String charToString(char[] charArray) {
        if (charArray == null || charArray.length == 0) {
            return ""; // 返回空字符串，因为没有输入
        }

        StringBuilder stringBuilder = new StringBuilder(charArray.length + charArray.length / 10);

        for (int i = 0; i < charArray.length; i++) {
            stringBuilder.append(charArray[i]);
            if ((i + 1) % 10 == 0) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public static String charToASCII(char[] packetChars) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < packetChars.length; i += 2) {
            if (i + 1 < packetChars.length) {
                // Combine two hexadecimal digits to form a byte
                String hexByte = "" + packetChars[i] + packetChars[i + 1];
                try {
                    int decimalValue = Integer.parseInt(hexByte, 16);
                    if (decimalValue >= 32 && decimalValue <= 126) {
                        // If the decimal value corresponds to a printable ASCII character, append it to the result
                        result.append((char) decimalValue);
                    } else {
                        // If the decimal value does not correspond to a printable character, use a placeholder
                        result.append('.');
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid hexadecimal digits
                    result.append('.');
                }
            } else {
                // Handle an odd number of characters in packetChars (append a placeholder)
                result.append('.');
            }
        }
        return result.toString();
    }

    public static String charToASCIIFor8(char[] packetChars) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < packetChars.length; i += 2) {
            if (i + 1 < packetChars.length) {
                // Combine two hexadecimal digits to form a byte
                String hexByte = "" + packetChars[i] + packetChars[i + 1];
                try {
                    int decimalValue = Integer.parseInt(hexByte, 16);
                    if (decimalValue >= 32 && decimalValue <= 126) {
                        // If the decimal value corresponds to a printable ASCII character, append it to the result
                        result.append((char) decimalValue);
                    } else {
                        // If the decimal value does not correspond to a printable character, use a placeholder
                        result.append('.');
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid hexadecimal digits
                    result.append('.');
                }
            } else {
                // Handle an odd number of characters in packetChars (append a placeholder)
                result.append('.');
            }

            if ((i / 2 + 1) % 8 == 0) {
                // Add a newline character every 8 characters
                result.append('\n');
            }
        }

        return result.toString();
    }



    public static void mapToModel(DefaultTableModel model, Map<String, String> map, String pre) {
        if(!pre.equals("")) pre = "("+pre+")";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            model.addRow(new Object[]{pre+entry.getKey(),entry.getValue()});
        }
    }

    public static List<String> getProtocolList(String protocols) {
        List<String> result = new ArrayList<String>();

        // 使用 "->" 分割字符串
        String[] parts = protocols.split("->");

        // 将分割的部分添加到结果列表
        Collections.addAll(result, parts);
        return result;
    }


    private char[] hexToAscii(String[] bytes) {
        char[] ascii= new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            // 将每个十六进制字符串转换为对应的整数值
            int decimalValue = Integer.parseInt(bytes[i], 16);

            // 将整数值转换为对应的ASCII字符
            ascii[i] = (char) decimalValue;
        }
        return ascii;
    }

    // 将十六进制数转换为二进制数
    public static String hexToBinary(String hex) {
        int decimal = Integer.parseInt(hex, 16);
        StringBuilder binary = new StringBuilder(Integer.toBinaryString(decimal));
        int n = hex.length()*4;
        // 补足前导零，使其成为n位二进制数
        while (binary.length() < n) {
            binary.insert(0, "0");
        }
        return binary.toString();
    }
    // 将二进制转换成十进制数
    public static long binaryToInt(String binary){
        return Long.parseLong(binary,2);
    }
    //将十六进制数转换为十进制数
    public static long hexToInt(String hex){
        return Long.parseLong(hex,16);
    }

    public static long hexToInt(char[] hex){
        return Long.parseLong(String.valueOf(hex),16);
    }

    public static long hexToInt(char hex){
        return Long.parseLong(hex+"",16);
    }

    public static char[] toArray(char... chars) {
        return chars;
    }
    //切片，start-end（不含end）
    public static char[] slice(char[] chars,int start,int end){
        if(start>=end || start < 0 || end > chars.length){
            throw new IllegalArgumentException("Invalid index");
        }
        int newArrayLength = end - start;
        char[] newArray = new char[newArrayLength];
        System.arraycopy(chars, start, newArray, 0, newArrayLength);
        return newArray;
    }
    //切片，从start切到尾
    public static char[] slice(char[] chars,int start){
        if(chars == null || start == chars.length) return null;
        if(chars.length == 0) return null;
        if(start < 0 || start > chars.length){
            throw new IllegalArgumentException("Invalid index");
        }
        int newArrayLength = chars.length - start;
        char[] newArray = new char[newArrayLength];
        System.arraycopy(chars, start, newArray, 0, newArrayLength);
        return newArray;
    }
    //mac地址加分号
    public static String getMac(char[] chars) {
        StringBuilder result = new StringBuilder("" + chars[0] + chars[1]);
        for(int i = 2 ; i<chars.length; i = i+2){
            result = new StringBuilder(result + ":" + chars[i] + chars[i + 1]);
        }
        return result.toString();
    }
    //ipv4地址加.
    public static String getIPv4(char[] chars) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i += 2) {
            String hexPair = String.valueOf(chars, i, 2);
            int decimalValue = Integer.parseInt(hexPair, 16);
            sb.append(decimalValue);

            if (i < 6) sb.append('.');
        }

        return sb.toString();
    }
    //ipv6地址加分号
    public static String getIPv6(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i += 4) {
            String hexPair = String.valueOf(chars, i, 4);
            sb.append(hexPair);
            if (i < 28) sb.append(':');
        }
        return sb.toString();
    }

    public static String getNetworkProtocol(String type) {
        if ("0800".equalsIgnoreCase(type)) return "IPv4";
        else if ("0806".equalsIgnoreCase(type)) return "ARP";
        else if ("86DD".equalsIgnoreCase(type)) return "IPv6";
        else if ("8100".equalsIgnoreCase(type)) return "VLAN";
        return unknown;
    }

    public static String getTransportProName(long pro) {
        if (pro == 1) {
            return "ICMP";
        } else if (pro == 2) {
            return "IGMP";
        } else if (pro == 6) {
            return "TCP";
        } else if (pro == 8) {
            return "EGP";
        } else if (pro == 17) {
            return "UDP";
        } else if (pro == 41) {
            return "IPv6";
        } else if (pro == 50) {
            return "ESP";
        } else if (pro == 51) {
            return "AH";
        } else if (pro == 58) {
            return "ICMPv6";
        } else if (pro == 89) {
            return "OSPF";
        }
        return unknown;
    }
}
