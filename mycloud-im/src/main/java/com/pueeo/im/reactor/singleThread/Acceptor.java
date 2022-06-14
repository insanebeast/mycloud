package com.pueeo.im.reactor.singleThread;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接收器
 * 负责建立新连接，并创建Handler负责后续业务处理
 */
public class Acceptor implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public Acceptor(ServerSocketChannel c, Selector s) {
        this.selector = s;
        this.serverSocketChannel = c;
    }

    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("接收到一个新的连接");
            if (socketChannel != null)
                //IO读写事件交给Handler处理，注意⚠：这里的Reator和Handler用的同一个selector
                new Handler(selector, socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
