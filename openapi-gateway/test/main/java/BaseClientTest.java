import cn.dreamkey.chain.brain.sdk.client.BaseClient;
import cn.dreamkey.chain.brain.sdk.client.ClientConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseClientTest {

    public static void main(String[] args) throws IOException {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setAppKey("shanglian");
        clientConfig.setSecret("5c3d186dcf07d6fe87f8d5a0a0856798");
        clientConfig.setKeyPath("/Users/kenneth/Downloads/private.pem");

        BaseClient baseClient = new BaseClient(clientConfig);

        Map<String, Object> data = new HashMap<>();
        data.put("name", "abc");
        data.put("age", 10);

        Map<String, Object> postResult = baseClient.post("http://127.0.0.1:8421", "/api/test/post", data, null, new TypeReference<Map<String, Object>>() {
        });
        log.info("post result={}", JSON.toJSONString(postResult));

        Map<String, Object> getResult = baseClient.get("http://127.0.0.1:8421", "/api/test/get", null, new TypeReference<Map<String, Object>>() {
        });
        log.info("get result={}", JSON.toJSONString(getResult));
    }
}
