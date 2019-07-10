package protobuftest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestProtoBufHandler extends SimpleChannelInboundHandler<DataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int i = new Random().nextInt(3);
        DataInfo.MyMessage myMessage = null;
        if (i == 0) {
            myMessage = DataInfo.MyMessage.newBuilder().setDataType(DataInfo.MyMessage.DataType.StudentType).setStudent(
                    DataInfo.Student.newBuilder()
                            .setAddress("北京").setAge(20).setName("小刘").build()
            ).build();
        } else if(i == 1) {
            myMessage = DataInfo.MyMessage.newBuilder().setDataType(DataInfo.MyMessage.DataType.DogType).setDog(
                    DataInfo.Dog.newBuilder()
                            .setAddress("北京").setAge(20).setName("小狗").build()
            ).build();
        } else {
            myMessage = DataInfo.MyMessage.newBuilder().setDataType(DataInfo.MyMessage.DataType.CatType).setCat(
                    DataInfo.Cat.newBuilder()
                            .setAddress("北京").setAge(20).setName("小猫").build()
            ).build();
        }


        ctx.channel().writeAndFlush(myMessage);
    }
}
