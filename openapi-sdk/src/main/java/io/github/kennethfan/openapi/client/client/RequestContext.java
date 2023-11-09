package io.github.kennethfan.openapi.client.client;

import cn.hutool.http.Method;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
public class RequestContext<B> {

    private String host;

    private String uri;

    private Method method;

    private Map<String, Object> header;

    private B body;

    private static <B> RequestContext<B> of(String host, String uri, Method method, Map<String, Object> header, B body) {
        RequestContext<B> context = new RequestContext<>();
        return context.setHost(host)
                .setUri(uri)
                .setMethod(method)
                .setHeader(header)
                .setBody(body);
    }

    public static <B> RequestContext<B> post(String host, String uri, Map<String, Object> header, B body) {
        return RequestContext.of(host, uri, Method.POST, header, body);
    }

    public static <B> RequestContext<B> get(String host, String uri, Map<String, Object> header) {
        return RequestContext.of(host, uri, Method.GET, header, null);
    }
}
