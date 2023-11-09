package io.github.kennethfan.openapi.gateway.web.controller.api;

import io.github.kennethfan.openapi.gateway.anno.ResourceAuth;
import io.github.kennethfan.openapi.gateway.enums.ResourceCodeEnum;
import io.github.kennethfan.openapi.client.model.response.ResponseData;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class TestController {

    @ResourceAuth(value = ResourceCodeEnum.LQT)
    @PostMapping("test/post")
    public ResponseData<Map<String, Object>> postTest(@RequestBody Map<String, Object> data) {
        return ResponseData.success(data);
    }

    @ResourceAuth(value = ResourceCodeEnum.PARKING_COUPON)
    @GetMapping("test/get")
    public ResponseData<Map<String, Object>> getTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "hello");
        data.put("age", 1000);
        return ResponseData.success(data);
    }
}
