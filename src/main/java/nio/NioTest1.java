package nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest1 {
    public static void main(String[] args) {
        //创建buffer对象,容量为10
        IntBuffer intBuffer = IntBuffer.allocate(10);

        for (int i = 0; i <intBuffer.capacity(); i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            //给buffer赋值
            intBuffer.put(randomNumber);
        }

        //读写反转
        intBuffer.flip();

        while (intBuffer.remaining() > 0) {
            System.out.println(intBuffer.get());
        }
    }
}
