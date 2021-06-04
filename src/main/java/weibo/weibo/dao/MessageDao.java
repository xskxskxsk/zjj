package weibo.weibo.dao;


import org.apache.ibatis.annotations.*;
import weibo.weibo.model.Message;

import java.util.List;

/**
 * Created by tyella on 2019/2/12.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id,to_id,content,create_time,has_read,conversation_id";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{fromId},#{toId},#{content},#{createTime},#{hasRead},#{conversationId})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addMessage(Message message);

    //TODO
    @Select({"select ", SELECT_FIELDS, " from " +
            "( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt " +
            "group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(int userId, int offset, int limit);

    @Select({"select count(id) from ", TABLE_NAME, "where to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, "where to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            "where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);
}