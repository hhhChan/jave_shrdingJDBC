package com.can.limiter;

import com.google.common.io.Files;
import org.springframework.core.io.ClassPathResource;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class JedisRateLimiterSeter implements AutoCloseable{
    private String luaScript;

    private Timer timer;

    private final Jedis jedis = new Jedis("localhost", 6379);

    public JedisRateLimiterSeter(String scriptFile, String key, String limit){
        super();
        try {
            luaScript = Files.asCharSource(new ClassPathResource(scriptFile).getFile(), Charset.defaultCharset())
                    .read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer();

        // 放入令牌的时间间隔
        long period = 1000L / Long.valueOf(limit);

        // 通过定时器，定时放入令牌
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + " 放入令牌：" + ((Long) jedis.eval(luaScript, 1, key, limit) == 1L));
            }
        }, period, period);
    }

    @Override
    public void close() throws Exception {
        this.jedis.close();
        this.timer.cancel();
    }
}
