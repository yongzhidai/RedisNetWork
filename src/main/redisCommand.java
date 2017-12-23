package main;

/**
 * Created on 2017/12/22.
 */
public class redisCommand {
    private String name;
    private redisCommandProc proc;

    private redisCommand(String name, redisCommandProc proc) {
        this.name = name;
        this.proc = proc;
    }

    public static redisCommand newInstance(String name, redisCommandProc proc){
        return new redisCommand(name,proc);
    }

    public redisCommandProc getProc() {
        return proc;
    }
}
