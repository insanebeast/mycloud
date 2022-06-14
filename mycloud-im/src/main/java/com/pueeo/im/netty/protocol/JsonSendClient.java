package com.pueeo.im.netty.protocol;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.Data;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class JsonSendClient {
    static String content = "疯狂创客圈：高性能学习社群!";

    private final int serverPort;
    private final String serverIp;
    Bootstrap b = new Bootstrap();

    public JsonSendClient(String ip, int port) {
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

            //5 装配通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //初始化客户端channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 客户端channel流水线添加2个handler处理器
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) -> {
                if (futureListener.isSuccess()) {
                    System.out.println("EchoClient客户端连接成功!");
                } else {
                    System.out.println("EchoClient客户端连接失败!");
                }
            });

            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();

            //发送 Json 字符串对象
            for (int i = 0; i < 1000; i++) {
                User user = build(i, i + "->" + content);
                channel.writeAndFlush(user.convertToJson());
                System.out.println("发送报文：" + user.convertToJson());
            }
            channel.flush();

            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }
    }

    @Data
    class User {
        Integer uid;
        String name;

        JsonMsg parseFromJson(String json) {
            return JSONObject.parseObject(json, JsonMsg.class);
        }

        String convertToJson() {
            return JSONObject.toJSONString(this);
        }
    }

    //构建Json对象
    public User build(int uid, String name) {
        User user = new User();
        user.setName(name);
        user.setUid(uid);
        return user;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 18899;
        String ip = "127.0.0.1";
        new JsonSendClient(ip, port).runClient();
    }
}