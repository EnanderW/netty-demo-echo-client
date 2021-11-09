package me.code;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            Channel channel = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new EchoChannelInitializer())
                    .connect(this.host, this.port).sync().channel();

            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            while (!line.equalsIgnoreCase("exit")) {
                ByteBuf buf = Unpooled.buffer();

                buf.writeBytes(line.getBytes());

                channel.writeAndFlush(buf);

                line = scanner.nextLine();
            }

        } catch (Exception ignored) {}
        finally {
            group.shutdownGracefully();
        }
    }
}
