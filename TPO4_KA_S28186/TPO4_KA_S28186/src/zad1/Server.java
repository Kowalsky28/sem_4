/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Server {
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    String host;
    int port;
    volatile boolean looping;
    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public void runServer() throws IOException {
        try(
                ServerSocketChannel sSC = ServerSocketChannel.open();
                Selector s = Selector.open();
        ) {
            sSC.bind(new InetSocketAddress(host, port));
            sSC.configureBlocking(false);
            sSC.register(selector, SelectionKey.OP_ACCEPT);
            this.selector = s;
            this.serverSocketChannel = sSC;
            while (looping) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                for (SelectionKey key : selectedKeys) {
                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer messageBuffer = ByteBuffer.allocate(1024);
                        int read = client.read(messageBuffer);
                        if (read == -1) {
                            key.cancel();
                            continue;
                        }
                        messageBuffer.flip();
                        System.out.println(StandardCharsets.UTF_8.decode(messageBuffer));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startServer() {
        looping = true;
    }
    public void stopServer() {
        looping = false;
    }
    String getServerLog(){
        return "";
    }

}
