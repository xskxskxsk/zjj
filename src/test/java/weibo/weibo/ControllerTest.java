package weibo.weibo;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import weibo.weibo.controller.LoginController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WeiboApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTest {
    @Autowired
    LoginController loginController;

    @Autowired
    private MockMvc mvc;


    @Test
    public void registerTest()throws Exception{

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(post("/reg?username=xliiin&password=123456"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"注册成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try{
            responseString=this.mvc.perform(get("/login?username=xliiin&password=123456"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"登录成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try{
            responseString=this.mvc.perform(post("/user/addNews?image=123.jpg&title=1&link=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"登录成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
