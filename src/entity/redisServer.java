package entity;

import ae.aeEventLoop;

import java.nio.channels.ServerSocketChannel;

/**
 * Created on 2017/12/17.
 */
public class redisServer {
    public static aeEventLoop eventLoop;

    public static int port = 10003;

    public static ServerSocketChannel serverSocketChannel;

}
