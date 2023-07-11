package com.sven.rainbowbeachlib.adblib;

import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class ServiceCommand {

    public static boolean excCommand(AdbConnection adbConnection, byte[] fileBase64, long size, long bitrate) {
        String localip = wifiIpAddress();
        final StringBuilder commandBuild = new StringBuilder();
        commandBuild.append(" CLASSPATH=/data/local/tmp/mycarserver.jar app_process / com.sven.mycarserver.Server ");
        commandBuild.append(" /" + localip + " " + size + " " + bitrate + ";");
        String command = commandBuild.toString();
        AdbStream stream = null;
        try {
            stream = adbConnection.open("shell:");
            if (stream == null) {
                return false;
            }
            stream.write("" + '\n');
            waiteForRunOver(stream);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


        if (stream != null) {
            int len = fileBase64.length;
            byte[] filePart = new byte[4056];
            int sourceOffset = 0;
            try {
                stream.write(" cd data/local/tmp " + '\n');
                waiteForRunOver(stream);
                while (sourceOffset < len) {
                    if (len - sourceOffset >= 4056) {
                        System.arraycopy(fileBase64, sourceOffset, filePart, 0, 4056);  //Writing in 4KB pieces. 4096-40  ---> 40 Bytes for actual command text.
                        sourceOffset = sourceOffset + 4056;
                        String ServerBase64part = new String(filePart, "US-ASCII");
                        Log.e("xie", "waiteForRunOver: 1");
                        stream.write(" echo " + ServerBase64part + " >> serverBase64" + '\n');
                        waiteForRunOver(stream);
                        Log.e("xie", "waiteForRunOver: 2");
                    } else {
                        int rem = len - sourceOffset;
                        byte[] remPart = new byte[rem];
                        System.arraycopy(fileBase64, sourceOffset, remPart, 0, rem);
                        sourceOffset = sourceOffset + rem;
                        String ServerBase64part = new String(remPart, "US-ASCII");
                        Log.e("xie", "waiteForRunOver: 3");
                        stream.write(" echo " + ServerBase64part + " >> serverBase64" + '\n');
                        waiteForRunOver(stream);
                        Log.e("xie", "waiteForRunOver: 4");
                    }
                }
                Log.e("xie", "waiteForRunOver: 5");
                stream.write(" base64 -d < serverBase64 > mycarserver.jar && rm serverBase64" + '\n');
                Log.e("xie", "waiteForRunOver: 7");
                stream.write(command + '\n');
                Log.e("xie", "waiteForRunOver: 8");
                Thread.sleep(1000);
                Log.e("xie", "excCommand: run  success");
                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    private static String wifiIpAddress() {
        try {
            InetAddress ipv4 = null;
            InetAddress ipv6 = null;

            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet6Address) {
                        ipv6 = inetAddress;
                        continue;
                    }
                    if (inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipv4 = inetAddress;
                        continue;
                    }
                    return inetAddress.getHostAddress();
                }
            }
            if (ipv6 != null) {
                return ipv6.getHostAddress();
            }
            if (ipv4 != null) {
                return ipv4.getHostAddress();
            }
            return null;

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;

    }


    public static void waiteForRunOver(AdbStream adbStream) {
        try {
            while (true) {
                byte[] responseBytes = adbStream.read();
                if (responseBytes != null && responseBytes.length > 0) {
                    String response = new String(responseBytes, "US-ASCII");
                    if (response.endsWith("$ ") || response.endsWith("# ")) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean excCommand(AdbConnection adbConnection, String command) {
        AdbStream stream = null;
        try {
            stream = adbConnection.open("shell:");
            if (stream == null) {
                return false;
            }
            stream.write("" + '\n');
            waiteForRunOver(stream);
            stream.write(command + '\n');
            return true;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
