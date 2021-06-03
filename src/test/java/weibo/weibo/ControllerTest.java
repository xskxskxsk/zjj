package weibo.weibo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import weibo.weibo.controller.LoginController;
import weibo.weibo.util.JwtHelper;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WeiboApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(false)
public class ControllerTest {
    @Autowired
    LoginController loginController;

    @Autowired
    private MockMvc mvc;


    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        return token;
    }

    @Test
    public void registerTest()throws Exception{
//        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(get("/login?username=xliiin&password=123456"))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"登录成功\"}";

        JSONObject jsonObect = new JSONObject(responseString);
        String token=jsonObect.getString("data");

//        try{
//            JSONAssert.assertEquals(expectedResponse,responseString,false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        try{
            responseString=this.mvc.perform(post("/user/addNews?image=123.jpg&title=1&link=1").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发布成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void uploadImgTest()throws Exception{
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        File file = new File("."+File.separator + "src" + File.separator + "test" + File.separator+"resources" + File.separator + "img" + File.separator+"timg.png");
        MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png" , "multipart/form-data", new FileInputStream(file));


        try{
            responseString=this.mvc.perform(MockMvcRequestBuilders
                    .multipart("/image")
                    .file(firstFile)
                    .header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发布成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
