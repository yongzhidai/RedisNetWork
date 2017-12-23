package networking;

import ae.aeEventLoop;
import ae.aeFileProc;
import entity.redisClient;
import entity.redisServer;
import main.redisCommand;
import main.redisCommandTable;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2017/12/17.
 */
public class readQueryFromClient implements aeFileProc {
    @Override
    public void handler(aeEventLoop el, SelectableChannel fd, redisClient privdata) {
        redisClient redisClient = privdata;
        SocketChannel socketChannel = (SocketChannel) fd;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int len = socketChannel.read(byteBuffer);
            if(len<0){
                close(el,socketChannel);
                return;
            }
            String query = new String(byteBuffer.array(),0,len,"utf-8");
            redisClient.setQuery(query);
            processCommand(redisClient);
        }catch (Exception e){
            close(el,socketChannel);
        }
    }

    private void close(aeEventLoop el,SocketChannel socketChannel){
        try {
            socketChannel.close();
            el.removeFileEvent(socketChannel);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void processCommand(redisClient redisClient){
        String query = redisClient.getQuery();
        redisCommand redisCommand = redisCommandTable.lookupCommand(query);
        redisClient.setCmd(redisCommand);
        addReply(redisClient);
        call(redisClient);
    }

    private void addReply(redisClient redisClient){
        prepareClientToWrite(redisClient);
    }

    private void prepareClientToWrite(redisClient redisClient){
        redisServer.eventLoop.aeCreateFileEvent(redisClient.fd(),SelectionKey.OP_WRITE,new sendReplyToClient(),redisClient);
    }

    private void call(redisClient redisClient){
        redisCommand redisCommand = redisClient.getCmd();
        if(redisCommand!=null){
            redisCommand.getProc().commandProc(redisClient);
        }
    }
}
