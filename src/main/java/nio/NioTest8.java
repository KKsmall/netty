package nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NioTest8 {
    public static void main(String[] args) throws Exception {
        //打开连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建连接地址和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8899);
        //绑定Ip地址和端口
        serverSocketChannel.socket().bind(inetSocketAddress);

        int messageLentgh = 2 + 3 + 4;

        ByteBuffer[] byteBuffers = new ByteBuffer[3];

        byteBuffers[0]  = ByteBuffer.allocate(2);
        byteBuffers[1]  = ByteBuffer.allocate(3);
        byteBuffers[2]  = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true){
            int byteRead = 0;

            while (byteRead < messageLentgh) {
                long r = socketChannel.read(byteBuffers);
                byteRead += r;
                System.out.println("byteRead: " + r);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "positon: " + byteBuffer.position() + ", limit: " + byteBuffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            long byteWritten = 0;
            while (byteWritten < messageLentgh) {
                long r = socketChannel.write(byteBuffers);
                byteWritten += r;
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
        }
    }
}
