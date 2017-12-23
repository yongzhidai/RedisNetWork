package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/12/23.
 */
public class redisCommandTable {
    private static Map<String,redisCommand> redisCmdMap = new HashMap<>();
    static {
        redisCmdMap.put("get",redisCommand.newInstance("get",new getCommand()));
        redisCmdMap.put("set",redisCommand.newInstance("set",new setCommand()));
    }

    public static redisCommand lookupCommand(String name){
        return redisCmdMap.get(name);
    }
}
