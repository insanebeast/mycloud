package com.pueeo.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TCPServer {

    public static void startServer() throws IOException{
        // 获取选择器
        Selector selector = Selector.open();
        // 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(18899));
        System.out.println("服务器启动成功");
        // 将通道注册的"接收新连接"IO事件，注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 轮询感兴趣的IO就绪事件（选择键集合）
        while(selector.select() > 0){
            // 获取选择键集合
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()){
                // 获取单个选择键，并处理
                SelectionKey selectionKey = selectedKeys.next();
                if (selectionKey.isAcceptable()){
                    // 若选择键的IO事件是"连接就绪"，就获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 将新连接切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 将该新连接通道的可读事件注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }else if (selectionKey.isReadable()){
                    // 若选择键的IO事件是"读就绪"，则读取数据
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int length;
                    while((length = socketChannel.read(buffer)) > 0){
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, length));
                        buffer.clear();
                    }
                    socketChannel.close();
                }
                // 移除选择键，防止重复处理
                selectedKeys.remove();
            }
        }
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
