package ae;

import java.nio.channels.SelectableChannel;

/**
 * Created on 2017/12/18.
 */
public class aeFiredEvent {
    private SelectableChannel channel;
    private int mask;

    public aeFiredEvent(SelectableChannel channel,int mask){
        this.channel = channel;
        this.mask = mask;
    }

    public SelectableChannel getChannel() {
        return channel;
    }

    public int getMask() {
        return mask;
    }
}
