package com.hms.rnd.netty.sample.discard;


import com.hms.rnd.netty.sample.server.TcpServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DiscardServer {
    public static void main(String[] args) throws Exception {
        int port = 9000;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new TcpServer("DiscardServer", port, new DiscardInitializer()).run();
    }
}


class DiscardInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new DiscardServerHandler());
    }
}


