package ae;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created on 2017/12/17.
 */
public class aeEpollApiState implements aeApiState {
    private Selector selector;
    public aeEpollApiState(){
        try {
            selector = Selector.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int aeApiPoll(aeEventLoop eventLoop,long timeout) {
        try {
            int num = selector.select(timeout);
            if(num<=0){
                return 0;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                SelectableChannel channel = selectionKey.channel();
                int mask=0;
                if(selectionKey.isAcceptable()){
                    mask |= SelectionKey.OP_ACCEPT;
                }
                if(selectionKey.isReadable()){
                    mask |= SelectionKey.OP_READ;
                }
                if(selectionKey.isWritable()){
                    mask |= SelectionKey.OP_WRITE;
                }
                aeFiredEvent firedEvent = new aeFiredEvent(channel,mask);
                eventLoop.addFiredEvent(firedEvent);
                iterator.remove();
            }
            return num;
        }catch (Exception e){

        }
        return 0;
    }

    @Override
    public void aeApiAddEvent(SelectableChannel selectableChannel, int mask) {
        try {
            selectableChannel.register(selector,mask);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void aeApiDelEvent(SelectableChannel selectableChannel, int mask) {
        try {
            selectableChannel.register(selector,mask);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
