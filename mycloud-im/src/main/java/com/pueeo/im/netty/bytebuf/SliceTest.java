package com.pueeo.im.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

public class SliceTest {
    @Test
    public  void testSlice() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBuf slice = buffer.slice();
        try {
            slice.retain();
            byte[] array = new byte[2];
            slice.readBytes(array);
            System.out.println(array);
        }finally {
            slice.release();
        }
    }
}