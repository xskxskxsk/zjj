package weibo.weibo.service;

import weibo.weibo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weibo.weibo.dao.CommentDao;

import java.util.List;

/**
 * Created by tyella on 2019/2/13.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

}
