package io.github.kennethfan.openapi.client.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResponseData<E> implements Serializable {
    private int code;
    private String msg;
    private E data;

    public static <E> ResponseData<E> success(E data) {
        ResponseData<E> response = new ResponseData<>();
        response.setCode(200);
        response.setMsg("ok");
        response.setData(data);
        return response;
    }

    public static <E> ResponseData<E> error(int code, String msg) {
        ResponseData<E> response = new ResponseData<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}

