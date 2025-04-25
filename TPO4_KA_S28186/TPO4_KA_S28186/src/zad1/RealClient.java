package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RealClient {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(()->{
                try (SocketChannel socket = SocketChannel.open(new InetSocketAddress("Localhost", 7777))) {
                    socket.configureBlocking(false);
                    socket.write(ByteBuffer.wrap(("HelloWorld" + finalI).getBytes(StandardCharsets.UTF_8)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}

