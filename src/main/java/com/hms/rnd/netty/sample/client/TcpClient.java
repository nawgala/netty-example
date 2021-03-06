package com.hms.rnd.netty.sample.client;

import com.hms.rnd.netty.sample.util.DefaultThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient {
    private String name;
    private int port;
    private String host;

    private ChannelInitializer<SocketChannel> channelInitializer;

    public TcpClient(String name, String host, int port, ChannelInitializer<SocketChannel> channelInitializer) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    public void run() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup(new DefaultThreadFactory(name));

        try {
            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioSocketChannel.class); // (3)
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            bootstrap.handler(channelInitializer);
            // Start the client.
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
