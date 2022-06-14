package com.pueeo.im.netty.bytebuf;

import com.pueeo.im.utils.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class BufferTypeTest {
    final static Charset UTF_8 = Charset.forName("UTF-8");

    //堆缓冲区测试用例
    @Test
    public void testHeapBuffer() {
        //取得堆内存
        ByteBuf heapBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (heapBuf.hasArray()) {
            //取得内部数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() +
                    heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            Logger.info(new String(array, offset, length, UTF_8));
        }
        heapBuf.release();
    }

    //直接缓冲区测试用例
    @Test
    public void testDirectBuffer() {
        ByteBuf directBuf = ByteBufAllocator.DEFAULT.directBuffer();
        directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            //把数据读取到堆内存 array 中，再进行 Java 处理
            directBuf.getBytes(directBuf.readerIndex(), array);
            Logger.info(new String(array, UTF_8));
        }
        directBuf.release();
    }
}