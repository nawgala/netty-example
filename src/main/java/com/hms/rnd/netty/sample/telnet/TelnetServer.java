package com.hms.rnd.netty.sample.telnet;

import com.hms.rnd.netty.sample.server.TcpServer;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;

public class TelnetServer {
    public static void main(String[] args) throws InterruptedException {
        new TcpServer("TelnetServer", 9000, new TelnetServerInitializer())
                .run();
    }
}

class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    private static final TelnetServerHandler SERVER_HANDLER = new TelnetServerHandler();


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                .addLast(DECODER)
                .addLast(ENCODER)
                .addLast(SERVER_HANDLER);
    }
}

class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(TelnetServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Channel is active");

        ctx.write(String.format("Welcome to %s", InetAddress.getLocalHost().getHostName()));
        ctx.write(String.format("Now : %s", new Date()));

        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        String response;
        boolean close = false;

        if (msg == null || msg.isEmpty()) {
            response = "Please type something ...\r\n";
        } else if (msg.equalsIgnoreCase("bye")) {
            response = "Have a nice day \r\n";
            close = true;
        } else {
            response = String.format("Did you say ? [%s]%s", msg, "\r\n");
        }

        final ChannelFuture future = ctx.write(response);

        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error while processing", cause);

        ctx.close();
    }
}
