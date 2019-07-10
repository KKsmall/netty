package test;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        //获取通道绑定自己真正处理逻辑的handler
        channelPipeline.addLast("HttpServerCodec", new HttpServerCodec())
                .addLast("TestHttpServerHandler", new TestHttpServerHandler());
    }
}
