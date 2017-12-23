package ae;

import entity.redisClient;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * Created on 2017/12/17.
 */
public class aeFileEvent {
    private int mask;
    private redisClient clientData;
    private aeFileProc rfileProc;
    private aeFileProc wfileProc;
    public aeFileEvent(int mask, aeFileProc aeFileProc, redisClient clientData){
        this.mask = mask;
        this.clientData = clientData;
        if((SelectionKey.OP_READ&mask)!=0 || (SelectionKey.OP_ACCEPT&mask)!=0){
            this.rfileProc = aeFileProc;
        }
        if((SelectionKey.OP_WRITE&mask)!=0){
            this.wfileProc = aeFileProc;
        }
    }

    public void setFileProc(int mask,aeFileProc aeFileProc){
        if((SelectionKey.OP_READ&mask)!=0 || (SelectionKey.OP_ACCEPT&mask)!=0){
            this.rfileProc = aeFileProc;
        }
        if((SelectionKey.OP_WRITE&mask)!=0){
            this.wfileProc = aeFileProc;
        }
    }

    public int mask(){
        return mask;
    }
    public void mask(int mask){
        this.mask = mask;
    }

    public void rfileProc(aeEventLoop eventLoop, SelectableChannel fd,redisClient clientData){
        rfileProc.handler(eventLoop,fd,clientData);
    }

    public void wfileProc(aeEventLoop eventLoop, SelectableChannel fd,redisClient clientData){
        wfileProc.handler(eventLoop,fd,clientData);
    }

    public redisClient getClientData() {
        return clientData;
    }
}
