package com.hms.rnd.netty.sample.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {

        logger.info("Channel activated");

        ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt(999999999);
//        time.writeBytes("Hell".getBytes());

        final ChannelFuture channelFuture = ctx.writeAndFlush(time);
        logger.info("Response sent asynchronously");

//        channelFuture.addListener(ChannelFutureListener.CLOSE);
//        channelFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                assert channelFuture == future;
//
////                ctx.close();
//                logger.info("Channel closed");
//            }
//        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error while handling time response ", cause);
    }
}
