package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest4 {
    public static void main(String[] args) throws  Exception {
        FileInputStream fileInputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        while (true) {
            //这里clear的作用是重置position和limit属性值到初始位置，每次读写完都要重置，否则最终数据会有问题
            buffer.clear();

            int read = inputChannel.read(buffer);

            if (-1 == read) {
                break;
            }

            buffer.flip();

            outputChannel.write(buffer);
        }

        fileInputStream.close();
        outputStream.close();
    }
}
