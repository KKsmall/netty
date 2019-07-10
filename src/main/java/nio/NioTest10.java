package nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NioTest10 {
    public static void main(String[] args) throws Exception {
        String inputFile = "NioTest10_in.txt";
        String outputFile = "NioTest10_out.txt";

        long inputLength = new File(inputFile).length();

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        FileChannel inputChannel = inputRandomAccessFile.getChannel();
        FileChannel outputChannel = outputRandomAccessFile.getChannel();

        //内存映射获取文件内容
        MappedByteBuffer byteBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);

        Charset charset = Charset.forName("utf-8");
        //解码器
        CharsetDecoder decoder = charset.newDecoder();
        //编码器
        CharsetEncoder encoder = charset.newEncoder();

        //先把MappedByteBuffer中的内容解码成字符buffer,再编码成字节buffer然后写入输出channel
        CharBuffer charBuffer = decoder.decode(byteBuffer);
        ByteBuffer buffer = encoder.encode(charBuffer);

        //如果不进行上面的转化直接outputChannel.write(byteBuffer); 输出文件内容是空的
        outputChannel.write(buffer);

        inputChannel.close();
        outputChannel.close();
    }
}
