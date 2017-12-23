package ae;

import entity.redisClient;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * Created on 2017/12/17.
 */
public interface aeFileProc {

    void handler(aeEventLoop el, SelectableChannel fd, redisClient privdata);
}
