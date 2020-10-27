package com.hms.rnd.netty.sample.echo;

import com.hms.rnd.netty.sample.client.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        new TcpClient("TimeClient", "localhost", 9000, new TimeClientInitializer()).run();
    }

}

class TimeClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TimeClientHandler());
    }
}

class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("Handler added");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("Handler removed");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Message received [{}]", msg);
    }

    //    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf byteBuf = (ByteBuf) msg; // (1)
//
//        logger.info("Message received [{}] bytes", byteBuf.readableBytes());
//
//        try {
//            logger.info("Message: {}", byteBuf.readInt());
//            logger.info("Readable bytes [{}] bytes", byteBuf.readableBytes());
//            ctx.close();
//        } finally {
//            byteBuf.release();
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

class TimeDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(TimeDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("Message is decoding [ readable: {}]", in.readableBytes());

        try {
            if (in.readableBytes() < 4) {
                logger.warn("Not enough data. Waiting till more data come");
                return;
            }
            out.add(in.readInt());
            logger.info("Decode is done");
        } finally {
            ReferenceCountUtil.release(in);
        }
    }
}
