package networking;

import ae.aeEventLoop;
import ae.aeFileProc;
import entity.redisClient;
import entity.redisServer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2017/12/17.
 */
public class sendReplyToClient implements aeFileProc {
    @Override
    public void handler(aeEventLoop el, SelectableChannel fd, redisClient privdata) {
        SocketChannel socketChannel = (SocketChannel) fd;
        List<String> list = privdata.getReply();
        Iterator<String> iterator = list.iterator();
        try {
            while(iterator.hasNext()){
                String content = iterator.next();
                byte[] bytes = content.getBytes("utf-8");
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                int writedNum = socketChannel.write(byteBuffer);
                if(writedNum<bytes.length){//当前字符串还有残余，留着下次继续发
                    String remainStr = new String(bytes,writedNum,bytes.length-writedNum,"utf-8");
                    privdata.addReply(remainStr);
                    break;
                }else{
                    iterator.remove();//这一行发送完了，则移除。
                }
            }
            if(list.size()<=0){//如果全部发送完了，则取消对可写事件的监听
                el.aeDeleteFileEvent(fd, SelectionKey.OP_WRITE);
            }
        }catch (Exception e){

        }
    }
}
