package test4;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.UUID;

/**
 * 这里不继承SimpleChannelInboundHandler而是继承他的父类，然后重新方法对用户时间被触发时的处理
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这里用来重写，处理用户触发空闲状态时的操作
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;

            String eventType = null;

            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
            ctx.channel().close();
        }

    }
}
