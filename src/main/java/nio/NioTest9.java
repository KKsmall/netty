package nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 一个线程处理多个socke连接，通过selector实现
 */
public class NioTest9 {

    public static void main(String[] args) throws Exception {
        int ports[] = new int[5];

        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        //打开selector
        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            //绑定
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ports[i]);
            serverSocket.bind(inetSocketAddress);

            //绑定完后吧selector注册到channel中，注册我们感兴趣的监听事件,返回值为SelectionKey
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("监听端口： " + ports[i]);
        }

        while (true) {
            //select是阻塞方法，阻塞到至少有一个通道在你注册的事件上就绪了。返回的int是有多少通道就绪了
            int numbers = selector.select();

            System.out.println("就绪通道数： " + numbers);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("SelectionKeys: " + selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {
                    //这里才真正处理连接,下面就建立了连接
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    //处理连接后的监听事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除,因为上面是while循环
                    iterator.remove();

                    System.out.println("客户端获得连接： " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    int byteRead = 0;

                    while (true) {
                        ByteBuffer buffer = ByteBuffer.allocate(512);

                        buffer.clear();

                        int read = socketChannel.read(buffer);

                        if (read <= 0 ) {
                            break;
                        }

                        buffer.flip();

                        socketChannel.write(buffer);

                        byteRead += read;
                    }
                    System.out.println("读取： " + byteRead + ", 来自于：" + socketChannel);
                    iterator.remove();
                }
            }
        }
    }
}
