package com.hms.rnd.netty.sample.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    private int count;

    private Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            count++;
            logger.info("Message received [ message: {}, handler: {}, count:{}]", new Object[]{msg, this, count});

//            if (count % 3 == 0) {
//                throw new Exception("I don't like this request");
//            }

            ByteBuf buf = (ByteBuf) msg;

            StringBuilder builder = new StringBuilder();
            while (buf.isReadable()) {
                builder.append((char) buf.readByte());
            }
            logger.info("[{}]Done", builder.toString());

            ctx.write("Hello, I', Server");
            ctx.flush();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error while reading", cause);
    }
}
