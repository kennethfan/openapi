package io.github.kennethfan.openapi.client.client;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientConfig {

    private String appKey;

    private String secret;

    private String keyPath;
}
