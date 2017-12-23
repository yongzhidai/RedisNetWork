package main;

import entity.redisClient;

/**
 * Created on 2017/12/23.
 */
public class getCommand implements redisCommandProc {
    @Override
    public void commandProc(redisClient redisClient) {
        String data = readFromDb();
        addReply(redisClient,data);
    }

    private void addReply(redisClient redisClient,String data){
        redisClient.addReply(data);
    }

    //模拟从db中读取数据
    private String readFromDb(){
        return "default reply";
    }
}
