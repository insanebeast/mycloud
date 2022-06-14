package com.pueeo.im.netty.codec;

import com.pueeo.im.netty.decoder.Byte2IntegerDecoder;
import com.pueeo.im.netty.encoder.Integer2ByteEncoder;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class IntegerDuplexHandler extends CombinedChannelDuplexHandler<Byte2IntegerDecoder, Integer2ByteEncoder> {
    public IntegerDuplexHandler() {
        super(new Byte2IntegerDecoder(), new Integer2ByteEncoder());
    }
}
