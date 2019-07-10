package nio;

import java.nio.ByteBuffer;

public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        buffer.position(2);
        buffer.limit(6);
        //设置完位置和limit，调用slice方法获得新的一个buffer,范围在2-6，不包括6，和旧buffer之间也能共享这范围数据
        ByteBuffer sliceBuffer = buffer.slice();

        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (sliceBuffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

    }
}
