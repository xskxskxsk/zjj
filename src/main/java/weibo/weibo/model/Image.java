package weibo.weibo.model;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author snow create 2021/04/17 14:51
 */
public class Image {
    private int id;
    private int userId;
    private String md5;
    private String name;

    public Image(int userId, String md5, String name) {
        this.userId = userId;
        this.md5 = md5;
        this.name = name;
    }

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public int getUserId(){return userId;}

    public void setUserId(int userId){this.userId=userId;}

    public String getMd5(){return md5;}

    public void setId(String md5){this.md5=md5;}

    public String getName(){return name;}

    public void setName(String name){this.name=name;}
}
