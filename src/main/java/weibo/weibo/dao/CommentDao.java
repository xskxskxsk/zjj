package weibo.weibo.dao;


import org.apache.ibatis.annotations.*;
import weibo.weibo.model.Comment;

import java.util.List;

/**
 * Created by tyella on 2019/2/12.
 */
@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " content,user_id,entity_id,entity_type,create_time,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{content},#{userId},#{entityId},#{entityType},#{createTime},#{status})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) ", " from ", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(int entityId, int entityType);
}
