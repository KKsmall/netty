package nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        Selector selector = Selector.open();

        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isConnectable()) {
                    SocketChannel client = (SocketChannel)selectionKey.channel();
                    if (client.isConnectionPending()) {
                        client.finishConnect(); //这里客户端才真正连接成功

                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                        writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                        writeBuffer.flip();
                        client.write(writeBuffer);

                        //由于键盘输入时阻塞的所以最好不要放主线程中处理，开线程去处理
                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                        executorService.submit(() -> {
                            writeBuffer.clear();
                            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            try {
                                String sendMessage = bufferedReader.readLine();
                                writeBuffer.put(sendMessage.getBytes());
                                writeBuffer.flip();
                                client.write(writeBuffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        //监听读事件
                        client.register(selector, SelectionKey.OP_READ);
                    }
                } else if (selectionKey.isReadable()) {
                    //当客户端通道监听到服务器写入的数据时，读出来
                    SocketChannel client = (SocketChannel)selectionKey.channel();

                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(readBuffer);

                    if (count > 0) {
                        readBuffer.flip();
                        String receivedMessage = new String(readBuffer.array(), 0, count);
                        System.out.println(receivedMessage);
                    }

                }

            }

            selectionKeys.clear();
        }
    }
}
