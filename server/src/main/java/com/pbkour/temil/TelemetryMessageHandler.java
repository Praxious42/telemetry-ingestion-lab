package com.pbkour.temil;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.telemetry.TelemetryDecoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TelemetryMessageHandler extends ChannelInboundHandlerAdapter {
    private final AggregateStore aggregateStore;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf buf) {
            try {
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                TelemetryMessage telemetryMessage = TelemetryDecoder.decode(bytes);
                aggregateStore.upsert(telemetryMessage);

                ctx.writeAndFlush(Unpooled.buffer(4).writeInt(200))
                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
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
