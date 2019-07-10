package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 服务器收到一个客户端消息向所有客户端转发消息
 */
public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            selectionKeys.forEach(selectionKey -> {
                final SocketChannel socketChannel1;
                try {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                        socketChannel1 = serverSocketChannel1.accept();
                        socketChannel1.configureBlocking(false);
                        socketChannel1.register(selector, SelectionKey.OP_READ);

                        //用来表示不同的客户端
                        String key = "【" + UUID.randomUUID().toString() + "】";

                        clientMap.put(key, socketChannel1);
                    } else if (selectionKey.isReadable()) {
                        socketChannel1 = (SocketChannel)selectionKey.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        int byteRead = socketChannel1.read(buffer);

                        if (byteRead > 0) {
                            //通道读入buffer后此时pos是内容长度 limit是1024，如果不flip后面decode解析的内容是内容长度末尾往后了到limit之间，都是空的
                            buffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String receivedMessage = String.valueOf(charset.decode(buffer).array());

                            System.out.println(socketChannel1 + ": " + receivedMessage);

                            String sendKey = null;

                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                if (socketChannel1 == entry.getValue()) {
                                    sendKey = entry.getKey();
                                    break;
                                }
                            }

                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                SocketChannel value = entry.getValue();
                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put((sendKey + ": " + receivedMessage).getBytes());
                                writeBuffer.flip();
                                value.write(writeBuffer);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            selectionKeys.clear();
        }
    }
}
