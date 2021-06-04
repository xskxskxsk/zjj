package weibo.weibo.util;

public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String RK_LIKE=" LIKE ";
    private static String RK_COMMENT=" COMMENT ";
    private static String RK_EVENT=" EVENT ";

    public static String getLikeKey(int entityId){
        //return RK_LIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
        return RK_LIKE+SPLIT+String.valueOf(entityId);
    }

    public static String getCommentKey(int entityId){
        return RK_COMMENT+SPLIT+String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityId,int entityType){
        return RK_COMMENT+SPLIT+String.valueOf(entityId);
    }

    public static String getEventQueueKey(){
        return RK_EVENT;
    }
}
