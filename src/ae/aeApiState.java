package ae;

import entity.redisClient;

import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * Created on 2017/12/17.
 */
public interface aeApiState {

    int aeApiPoll(aeEventLoop eventLoop,long timeout);

    void aeApiAddEvent(SelectableChannel selectableChannel, int mask);

    void aeApiDelEvent(SelectableChannel selectableChannel,int mask);

}
