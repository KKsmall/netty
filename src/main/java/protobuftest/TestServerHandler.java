package protobuftest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * DataInfo.MyMessage用message封装3中不同的对象，实现多协议的传输，相比之前的DataInfo.Student更灵活
 */
public class TestServerHandler extends SimpleChannelInboundHandler<DataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.MyMessage msg) throws Exception {
        DataInfo.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == DataInfo.MyMessage.DataType.StudentType) {
            DataInfo.Student student = msg.getStudent();
            System.out.println(student.getAge());
            System.out.println(student.getName());
        } else if(dataType == DataInfo.MyMessage.DataType.CatType) {
            DataInfo.Cat cat = msg.getCat();
            System.out.println(cat.getAge());
            System.out.println(cat.getName());
        } else {
            DataInfo.Dog dog = msg.getDog();
            System.out.println(dog.getAge());
            System.out.println(dog.getName());
        }
    }
}
