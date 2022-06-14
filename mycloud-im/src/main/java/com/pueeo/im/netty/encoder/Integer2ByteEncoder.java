package com.pueeo.im.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {
   @Override
   public void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out){
     out.writeInt(msg);
     System.out.println("encoder Integer = " + msg);
   }
 }