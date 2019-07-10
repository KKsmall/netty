package nio;

import java.nio.ByteBuffer;

public class NioTest6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }
        //读写buffer可转为只读buffer,只读不可转读写
        ByteBuffer readBuffer = buffer.asReadOnlyBuffer();

        readBuffer.position(0);
        //会报错
        readBuffer.put((byte)2);
    }
}
