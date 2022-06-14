package com.pueeo.im.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class DuplexTester {
    /**
     * 测试整数编码器
     */
    @Test
    public void testIntegerDuplexHandler() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new IntegerDuplexHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        //测decoder入站
        for (int j = 0; j < 5; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        //测encoder出站
        for (int j = 0; j < 10; j++) {
            channel.write(j);
        }
        channel.flush();

        //取得通道的出站数据帧
        ByteBuf buf = channel.readOutbound();
        while (null != buf) {
            System.out.println("o = " + buf.readInt());
            buf = channel.readOutbound();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
