package nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件
 */
public class NioTest7 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("intput3.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();
        //确定映射模型和可以操作的范围
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        //操作完在这里的文件打开看还是没有变化，但自己去磁盘中访问内容是已经变化了
        mappedByteBuffer.put(0, (byte)'a');
        mappedByteBuffer.put(2, (byte)'b');

        randomAccessFile.close();
    }
}
