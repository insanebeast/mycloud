package com.pueeo.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class UDPClient {

    public void send() throws IOException {
        //获取 DatagramChannel 数据报通道
        DatagramChannel dChannel = DatagramChannel.open();
        //设置为非阻塞
        dChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        System.out.println("UDP 客户端启动成功！");
        System.out.println("请输入发送内容:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((System.currentTimeMillis() + " >>" + next).getBytes());
            buffer.flip();
            //通过 DatagramChannel 数据报通道发送数据
            dChannel.send(buffer,
                    new InetSocketAddress("127.0.0.1", 18899));
            buffer.clear();
        }
        //操作四：关闭 DatagramChannel 数据报通道
        dChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }
}