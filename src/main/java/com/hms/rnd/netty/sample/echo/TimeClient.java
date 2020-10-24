package com.hms.rnd.netty.sample.echo;

import com.hms.rnd.netty.sample.client.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        new TcpClient("TimeClient", "localhost", 9000, new TimeClientHandler()).run();
    }

}

class  TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("Message received");

        ByteBuf m = (ByteBuf) msg; // (1)
        try {
            logger.info("Message: {}", m.readInt());
            ctx.close();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
