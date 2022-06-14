package com.pueeo.im.reactor.multiThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;


class EchoServer {
    ServerSocketChannel serverSocketChannel;
    //boss selector：处理新连接（OP_ACCEPT）
    Selector bossSelector;
    //boss reactor:处理OP_ACCEPT事件
    Reactor bossReactor;
    //work selectors：处理IO读写（OP_READ、OP_WRITE）
    Selector[] workSelectors = new Selector[2];
    //引入多个子反应器
    Reactor[] workReactors;

    EchoServer() throws IOException {

        //初始化多个selector选择器
        bossSelector = Selector.open();// 用于监听新连接事件
        workSelectors[0] = Selector.open(); // 用于监听read、write事件
        workSelectors[1] = Selector.open(); // 用于监听read、write事件
        serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress("127.0.0.1", 18899);
        serverSocketChannel.socket().bind(address);
        serverSocketChannel.configureBlocking(false);//非阻塞
        System.out.println("服务端已经开始监听："+address);

        //bossSelector,负责监控新连接事件, 将 serverSocketChannel注册到bossSelector
        SelectionKey sk = serverSocketChannel.register(bossSelector, SelectionKey.OP_ACCEPT);

        //绑定Acceptor接收器到SelectionKey（选择键）
        sk.attach(new Acceptor(serverSocketChannel, workSelectors));

        //bossReactor反应器，处理新连接的bossSelector
        bossReactor = new Reactor(bossSelector);

        //第一个子反应器，一子反应器负责一个worker选择器
        Reactor workReactor1 = new Reactor(workSelectors[0]);
        //第二个子反应器，一子反应器负责一个worker选择器
        Reactor workReactor2 = new Reactor(workSelectors[1]);
        workReactors = new Reactor[]{workReactor1, workReactor2};
    }

    private void start() {
        // 一子反应器对应一条线程
        Thread boss = new Thread(bossReactor);
        Thread work1 = new Thread(workReactors[0]);
        Thread work2 = new Thread(workReactors[1]);
        boss.setName("Thread-boss");
        work1.setName("Thread-work1");
        work2.setName("Thread-work2");
        boss.start();
        work1.start();
        work2.start();
    }

    public static void main(String[] args) throws IOException {
        EchoServer server = new EchoServer();
        server.start();
    }
}
