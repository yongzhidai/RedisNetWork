package main;

import ae.*;
import entity.redisServer;
import networking.acceptTcpHandler;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2017/12/17.
 */
public class redis {

    public static void main(String[] args) {
        initServer();
        aeMain(redisServer.eventLoop);
    }

    private static void initServer(){
        redisServer.eventLoop = aeCreateEventLoop();
        if(listenToPort(redisServer.port)==-1){
            System.exit(1);
        }
        redisServer.eventLoop.aeCreateFileEvent(redisServer.serverSocketChannel,SelectionKey.OP_ACCEPT,new acceptTcpHandler(),null);

    }

    private static aeEventLoop aeCreateEventLoop(){
        //redis通过aeApiState来屏蔽不同平台的实现，linux使用了epoll模型
        aeApiState apiData = new aeEpollApiState();
        return new aeEventLoop(apiData);
    }

    private static int listenToPort(int port){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            redisServer.serverSocketChannel = serverSocketChannel;
        }catch (Exception e){
            return -1;
        }
        return 0;
    }

    private static void aeMain(aeEventLoop eventLoop){
        eventLoop.stop = 0;
        while(eventLoop.stop==0){
            aeProcessEvents(eventLoop);
        }
    }

    private static void aeProcessEvents(aeEventLoop eventLoop){
        eventLoop.getApiData().aeApiPoll(eventLoop,0);
        List<aeFiredEvent> firedEvents = eventLoop.getFired();
        Iterator<aeFiredEvent> iterator = firedEvents.iterator();
        while (iterator.hasNext()){
            aeFiredEvent aeFiredEvent = iterator.next();
            aeFileEvent fileEvent = eventLoop.getFileEvent(aeFiredEvent.getChannel());
            if((aeFiredEvent.getMask() & SelectionKey.OP_ACCEPT)!=0 ||
                    (aeFiredEvent.getMask() & SelectionKey.OP_READ)!=0){
                fileEvent.rfileProc(eventLoop,aeFiredEvent.getChannel(),fileEvent.getClientData());
            }
            if((aeFiredEvent.getMask() & SelectionKey.OP_WRITE)!=0){
                fileEvent.wfileProc(eventLoop,aeFiredEvent.getChannel(),fileEvent.getClientData());
            }
            iterator.remove();
        }
    }
}
