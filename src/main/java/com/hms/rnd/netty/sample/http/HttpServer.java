package com.hms.rnd.netty.sample.http;

import com.hms.rnd.netty.sample.server.TcpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;


public class HttpServer {

    private static int port = 8080;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new TcpServer("HttpServer", port, new ServerInitializer()).run();
    }
}


class ServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        pipeline.addLast(new HttpServerHandler());
    }

}


class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
        ctx.flush();
    }

}
