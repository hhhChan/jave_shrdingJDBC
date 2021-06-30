package com.can.limiter;

import com.google.common.io.Files;
import org.springframework.core.io.ClassPathResource;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.Charset;

public class JedisRateLimiter {
    private String luaScript;

    private String key;

    public JedisRateLimiter(String scriptFile, String key) {
        super();
        this.key = key;
        try {
            luaScript = Files.asCharSource(new ClassPathResource(scriptFile).getFile(), Charset.defaultCharset())
                    .read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean acquire() {
        try (Jedis jedis = new Jedis("localhost", 6379);) {
            return (Long) jedis.eval(luaScript, 1, key) == 1L;
        }
    }
}
