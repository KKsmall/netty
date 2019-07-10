package test3chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyChatInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        //DelimiterBasedFrameDecoder是用来识别消息中的分隔符的
        channelPipeline.addLast("delimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                .addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8))
                .addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new MyChatHandler());
    }
}
