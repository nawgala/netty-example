package com.hms.rnd.netty.sample.echo;


import com.hms.rnd.netty.sample.server.TcpServer;

public class TimeServer {
    public static void main(String[] args) throws Exception {
        int port = 9000;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new TcpServer("TimeServer", port, new TimeServerHandler()).run();
    }
}

