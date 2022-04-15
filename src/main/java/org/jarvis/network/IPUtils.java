package org.jarvis.network;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public abstract class IPUtils {

    private static final Pattern IP_PATTERN = Pattern.compile("((25[0-5]\\.|2[0-4]\\d\\.|1\\d{2}\\.|[1-9]?\\d\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d))");


    /**
     * 判断ip是否合法
     *
     * @param ip
     * @return
     */
    public static boolean isValidIp(String ip) {
        return ip != null && IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 字符串类型ip转long
     *
     * @param ip
     * @return
     */
    public static long ipToLong(String ip) {
        String[] split = ip.split("\\.");
        return (Long.parseLong(split[0]) << 24) + (Long.parseLong(split[1]) << 16) +
                (Long.parseLong(split[2]) << 8) + (Long.parseLong(split[3]));
    }

    public static int ipToInt(String ip) {
        String[] split = ip.split("\\.");
        return (Integer.parseInt(split[0]) << 24) + (Integer.parseInt(split[1]) << 16) +
                (Integer.parseInt(split[2]) << 8) + (Integer.parseInt(split[3]));
    }

    public static String ipToString(int ip) {
        return String.format("%d.%d.%d.%d", ip >>> 24, (ip >>> 16) & 0xFF, (ip >>> 8) & 0xFF, ip & 0xFF);
    }

    /**
     * long类型ip转字符串类型
     *
     * @param ip
     * @return
     */
    public static String ipToString(long ip) {
        return String.format("%d.%d.%d.%d", ip >>> 24, (ip >>> 16) & 0xFF, (ip >>> 8) & 0xFF, ip & 0xFF);
    }

    /**
     * 尽可能地获取本机Ip（非回环地址）
     *
     * @return
     */
    public static String getLocalIp() {
        try {
            // 得到当前机器上在局域网内所有的网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历所有的网络接口
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 获取当前接口下绑定到该网卡的所有的 IP地址。
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();
                    // 只需要ipv4地址 排除ipv6地址和127.0.0.1 取ipv4地址
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                        // 获取网卡接口地址(ip)
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {

        }
        return null;
    }

    /**
     * 尽可能地获取请求(用户端)Ip地址
     * <p>
     * https://www.cnblogs.com/lukelook/p/11079372.html
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


}
