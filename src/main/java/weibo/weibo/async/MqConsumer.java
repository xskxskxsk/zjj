package weibo.weibo.async;
import com.alibaba.fastjson.JSON;
import weibo.weibo.dao.NewsDao;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class MqConsumer {
    private DefaultMQPushConsumer consumer;

    @Resource
    private NewsDao newsDao;

    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value("${mq.topicname}")
    private String topicName;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("event_consumer_group");
        // consumer连接nameserver
        consumer.setNamesrvAddr(nameAddr);
        // consumer订阅所有stock topic消息
        consumer.subscribe(topicName, "*");
        // 当消息推送过来之后的处理方式
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 实现库存真正到数据库内扣减的逻辑
                Message msg = msgs.get(0);
                String jsonString = new String(msg.getBody());
                Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
                Integer newsId = (Integer) map.get("newsId");
                Integer amount = (Integer) map.get("amount");
                newsDao.updateLikeCount(newsId, amount);
                // 说明该消息已经被消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}

