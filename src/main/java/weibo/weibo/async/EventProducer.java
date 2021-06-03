package weibo.weibo.async;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import weibo.weibo.util.JedisUtil;
import weibo.weibo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.GarbageCollectorMXBean;

/**
 * 事件生产者，负责把事件放入消息队列
 */
@Service
public class EventProducer {
    private static final Logger logger= LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisUtil jedisUtil;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisUtil.lpush(key,json);
//            logger.error();
            return true;
        }catch(Exception e){
            logger.error("事件放入队列失败"+e.getMessage());
            return false;
        }
    }
}
