package weibo.weibo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import weibo.weibo.dao.NewsDao;
import weibo.weibo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weibo.weibo.dao.CommentDao;
import weibo.weibo.util.JedisUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by tyella on 2019/2/13.
 */
@Service
public class CommentService {
    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private NewsDao newsDao;

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

     public void transCommentCountFromRedis(){
         Set<String> keys=redisTemplate.keys("*");
         for(String str:keys){
             String newsIdStr=str.split(":")[1];
             String name=str.split(":")[0];
             if(name.equals(" COMMENT ")){
                 int count= Integer.parseInt(jedisUtil.get(str));
                 newsDao.updateLikeCount(Integer.parseInt(newsIdStr),count);
             }
         }
     }
    public int addComment(Comment comment){

        return commentDao.addComment(comment);
    }

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        int count=0;
        boolean hasKey = redisTemplate.hasKey(" COMMENT :"+entityId);
        if(hasKey) {
            count = Integer.parseInt(jedisUtil.get(" COMMENT :" + entityId));
        }
        return count;
    }

    public int getCommentCounts(int entityId,int entityType){
         return commentDao.getCommentCount(entityId,entityType);
    }

}
