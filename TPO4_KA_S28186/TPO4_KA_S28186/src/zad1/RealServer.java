package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class RealServer {
    public static void main(String[] args) {
        try (
                ServerSocketChannel sala = ServerSocketChannel.open();
                Selector teacher = Selector.open()
        ) {
            sala.bind(new InetSocketAddress("localhost", 7777));
            sala.configureBlocking(false);
            sala.register(teacher, SelectionKey.OP_ACCEPT);

            while (true) {
                teacher.select();
                Set<SelectionKey> studentEvents = teacher.selectedKeys();

                for (SelectionKey studentEvent : studentEvents) {
                    if (studentEvent.isAcceptable()) {
                        SocketChannel student = sala.accept();
                        student.configureBlocking(false);
                        student.register(teacher, SelectionKey.OP_READ);
                    }
                    if (studentEvent.isReadable()) {
                        SocketChannel student = (SocketChannel) studentEvent.channel();
                        ByteBuffer messageBuffer = ByteBuffer.allocate(1024);
                        int read = student.read(messageBuffer);
                        if (read == -1) {
                            studentEvent.cancel();
                            continue;
                        }
                        messageBuffer.flip();
                        System.out.println(StandardCharsets.UTF_8.decode(messageBuffer));
                    }
                }
                studentEvents.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

