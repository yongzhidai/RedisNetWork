package ae;

import entity.redisClient;

import java.nio.channels.SelectableChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/17.
 */
public class aeEventLoop {
    private Map<SelectableChannel,aeFileEvent> events;
    private aeApiState apiData;
    public  int stop;
    private List<aeFiredEvent> fired;
    public aeEventLoop(aeApiState apiData){
        events = new HashMap<>();
        this.apiData = apiData;
        fired = new ArrayList<>();
    }

    public void aeCreateFileEvent(SelectableChannel socketChannel, int mask, aeFileProc aeFileProc, redisClient clientData){
        aeFileEvent aeFileEvent = events.get(socketChannel);
        if(aeFileEvent == null){//不存在则创建
            aeFileEvent = new aeFileEvent(mask,aeFileProc,clientData);
            events.put(socketChannel,aeFileEvent);
        }else{//存在则更新处理对象
            aeFileEvent.setFileProc(mask,aeFileProc);
        }
        aeFileEvent.mask(aeFileEvent.mask() | mask);
        apiData.aeApiAddEvent(socketChannel,aeFileEvent.mask());
    }

    public void aeDeleteFileEvent(SelectableChannel selectableChannel,int mask){
        aeFileEvent aeFileEvent = events.get(selectableChannel);
        if(aeFileEvent == null){
            return;
        }
        aeFileEvent.mask(aeFileEvent.mask()&(~mask));

        apiData.aeApiDelEvent(selectableChannel,aeFileEvent.mask());
    }


    public aeApiState getApiData(){
        return apiData;
    }

    public void addFiredEvent(aeFiredEvent firedEvent){
        fired.add(firedEvent);
    }

    public List<aeFiredEvent> getFired() {
        return fired;
    }

    public aeFileEvent getFileEvent(SelectableChannel channel){
        return events.get(channel);
    }

    public void removeFileEvent(SelectableChannel channel){
        events.remove(channel);
    }
}
