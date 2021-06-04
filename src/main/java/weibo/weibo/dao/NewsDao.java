package weibo.weibo.dao;

import weibo.weibo.model.Image;
import weibo.weibo.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by tyella on 2019/2/11.
 */
@Mapper
public interface NewsDao {
    String TABLE_NAME = "news";
    String TABLE_NAME1 = "image";
    String INSERT_FIELDS = " title,link,image,create_time,user_id,comment_count,like_count";
    String INSERT_FIELDS1 = " user_id,md5,name";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "values(#{title},#{link},#{image},#{createTime},#{userId},#{commentCount},#{likeCount})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void addNews(News news);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id=#{id}"})
    News selectById(int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"order by id desc"})
    List<News> selectNews();

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where user_id=#{userId} order by id desc"})
    List<News> selectMyNews(@Param("userId") int userId);

    @Update({"update ",TABLE_NAME,"set commentCount=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ",TABLE_NAME,"set like_count=#{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id,@Param("likeCount") int likeCount);

    @Insert({"insert into ",TABLE_NAME1,"(",INSERT_FIELDS1,") " +
            "values(#{userId},#{md5},#{name})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    String insertImage(Image image);

    //TODO
    //@Select({})
    List<News> selectByUserIdAndOffset(@Param("id") int userId,@Param("offset") int offset,@Param("limit") int limit);

}
