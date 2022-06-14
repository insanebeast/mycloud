package com.pueeo.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TCPClient {
    public static void startClient() throws IOException {
        InetSocketAddress address =
                new InetSocketAddress("127.0.0.1",18899);
        // 获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        // 切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //不断地自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {
        }
        System.out.println("客户端连接成功");
        // 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world".getBytes());
        byteBuffer.flip();
        //发送到服务器
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
    public static void main(String[] args) throws IOException {
        startClient();
    }
}
