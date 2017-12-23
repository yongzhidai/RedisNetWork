package networking;

import ae.aeEventLoop;
import ae.aeFileEvent;
import ae.aeFileProc;
import entity.redisClient;
import entity.redisServer;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * Created on 2017/12/17.
 */
public class acceptTcpHandler implements aeFileProc {
    @Override
    public void handler(aeEventLoop el, SelectableChannel fd, redisClient privdata) {
        ServerSocketChannel serverSocketChannel =(ServerSocketChannel)fd;
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            acceptCommonHandler(socketChannel);
        }catch (Exception e){

        }
    }

    private void acceptCommonHandler(SelectableChannel fd){
        createClient(fd);
    }

    private redisClient createClient(SelectableChannel fd){
        redisClient redisClient = new redisClient(fd);
        try {
            fd.configureBlocking(false);
            redisServer.eventLoop.aeCreateFileEvent(fd,SelectionKey.OP_READ,new readQueryFromClient(),redisClient);
        }catch (Exception e){
        }
        return redisClient;
    }
}
