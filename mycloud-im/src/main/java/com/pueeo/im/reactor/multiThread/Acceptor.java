package com.pueeo.im.reactor.multiThread;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接收器
 */
class Acceptor implements Runnable {

    AtomicInteger next = new AtomicInteger(0);
    private final ServerSocketChannel serverSocketChannel;
    private Selector[] workSelectors;

    public Acceptor(ServerSocketChannel c, Selector[] s) {
        this.workSelectors = s;
        this.serverSocketChannel = c;
    }

    public void run() {
        try {
            SocketChannel channel = serverSocketChannel.accept();
            System.out.println("接收到一个新的连接");

            if (channel != null) {
                int index = next.get();
                System.out.println("选择器的编号：" + index);
                Selector selector = workSelectors[index];
                //创建handler，负责后续业务处理
                new Handler(selector, channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (next.incrementAndGet() == workSelectors.length) {
            next.set(0);
        }
    }
}