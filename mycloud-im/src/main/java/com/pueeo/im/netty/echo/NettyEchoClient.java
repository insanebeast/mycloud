package com.pueeo.im.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class NettyEchoClient {

    private final int serverPort;
    private final String serverIp;
    Bootstrap b = new Bootstrap();

    public NettyEchoClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        //创建reactor 线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

            //5 装配通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    // ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                    ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture f = null;

            boolean connected = false;
            while (!connected) {
                f = b.connect();
                f.addListener((ChannelFuture futureListener) ->{
                    if (futureListener.isSuccess()) {
                        System.out.println("EchoClient客户端连接成功!");
                    } else {
                        System.out.println("EchoClient客户端连接失败!");
                    }
                });

                // 阻塞,直到连接完成
                // f.sync();
                f.awaitUninterruptibly();

                if (f.isCancelled()) {
                    System.out.println("用户取消连接:");
                    return;
                } else if (f.isSuccess()) {
                    connected = true;
                }
            }
            Channel channel = f.channel();

            GenericFutureListener sendCallBack = new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("发送成功!");
                    } else {
                        System.out.println("发送失败!");
                    }
                }
            };

            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入发送内容:");
            while (scanner.hasNext()) {
                //获取输入的内容
                String next = scanner.next();
                byte[] bytes = (System.currentTimeMillis() + " >>" + next).getBytes(StandardCharsets.UTF_8);
                //发送ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(bytes);
                ChannelFuture writeAndFlushFuture = channel.writeAndFlush(buffer);
                writeAndFlushFuture.addListener(sendCallBack);
                System.out.println("请输入发送内容:");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 19988;
        String ip = "127.0.0.1";
        new NettyEchoClient(ip, port).runClient();
    }
}