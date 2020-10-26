package com.hms.rnd.netty.sample.server;

import com.hms.rnd.netty.sample.util.DefaultThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpServer {

    private String name;

    private int port;

    private ChannelInitializer channelInitializer;

    public TcpServer(String name, int port, ChannelInitializer channelInitializer) {
        this.name = name;
        this.port = port;
        this.channelInitializer = channelInitializer;
    }


    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(String.format("%s-boss", name))); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup(10, new DefaultThreadFactory(String.format("%s-worker", name)));

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap(); // (2)
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture future = serverBootstrap.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
