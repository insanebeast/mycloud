package com.pueeo.im.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.Status> {

    //定义内部枚举，标识ReplayingDecoder的state
    enum Status {
        PARSE_1, PARSE_2
    }

    private int first;
    private int second;

    public IntegerAddDecoder() {
        //构造函数中，需要初始化父类的state 属性，表示当前阶段
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case PARSE_1:
                //从装饰器ByteBuf 中读取数据
                first = in.readInt();
                //进入第二步，并且设置“读指针断点”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                second = in.readInt();
                //提取到第二个整数，提取后还需要结算相加的结果
                //并且将和作为解码的结果输出
                Integer sum = first + second;
                out.add(sum);
                //进入下一轮解码的第一步，设置“state”为第一阶段
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}