package com.pueeo.im.reactor.singleThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 服务端
 */
class EchoServer {
    Reactor reactor;

    EchoServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel  serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress("127.0.0.1",18899);
        serverSocketChannel.socket().bind(address);
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务端已经开始监听："+address);

        //接收accept事件
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //绑定 AcceptorHandler 新连接处理器到 selectKey 选择键
        sk.attach(new Acceptor(serverSocketChannel, selector));
        //Reactor初始化
        reactor = new Reactor(selector);
    }

    private void start() {
        new Thread(reactor).start();
    }

    public static void main(String[] args) throws IOException {
        EchoServer echoServer = new EchoServer();
        echoServer.start();
    }
}