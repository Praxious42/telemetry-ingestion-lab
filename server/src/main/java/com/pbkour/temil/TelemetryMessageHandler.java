package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryDecoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TelemetryMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Common Netty inbound message types are ByteBuf (raw bytes) or already-decoded byte[]
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            try {
                byte[] bytes = new byte[buf.readableBytes()];
                // readBytes advances readerIndex; we copy bytes into a new array
                buf.readBytes(bytes);
                System.out.println(bytes.length);

                ctx.fireChannelRead(bytes.length);
            } finally {
                ReferenceCountUtil.release(buf);
            }
        } else {
            // Unknown message type
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
