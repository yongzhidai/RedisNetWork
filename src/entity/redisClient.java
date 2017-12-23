package entity;


import main.redisCommand;

import java.nio.channels.SelectableChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/12/17.
 */
public class redisClient {
    private SelectableChannel selectableChannel;
    private String querybuf;
    private List<String> buf;
    private redisCommand cmd;
    public redisClient(SelectableChannel selectableChannel){
        this.selectableChannel = selectableChannel;
        buf = new ArrayList<>();
    }

    public String getQuery(){
        return querybuf;
    }

    public void setQuery(String query){
        this.querybuf = query;
    }

    public SelectableChannel fd(){
        return selectableChannel;
    }

    public void addReply(String content){
        buf.add(content);
    }

    public List<String> getReply(){
        return this.buf;
    }

    public redisCommand getCmd() {
        return cmd;
    }

    public void setCmd(redisCommand cmd) {
        this.cmd = cmd;
    }
}
