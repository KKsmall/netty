package test2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client received:" + msg);
        //这里只有在收到消息后才会发送这条消息
        ctx.channel().writeAndFlush("from client:" + LocalDateTime.now());

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接成功后可以在这里先发送第一条消息
        ctx.writeAndFlush("你好");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
