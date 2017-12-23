package main;

import entity.redisClient;

/**
 * Created on 2017/12/22.
 */
public interface redisCommandProc {
    void commandProc(redisClient redisClient);
}
