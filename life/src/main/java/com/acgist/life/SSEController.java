package com.acgist.life;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SSEController {

    @RequestMapping("/sse")
    public SseEmitter sse() {
        final AtomicBoolean finish = new AtomicBoolean(false);
        final SseEmitter sse = new SseEmitter();
        sse.onError((e) -> {
            System.out.println("异常");
            finish.set(true);
        });
        sse.onCompletion(() -> {
            System.out.println("完成");
            finish.set(true);
        });
        new Thread(() -> {
            while(!finish.get()) {
                System.out.println("发送 = " + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                try {
                    sse.send("{\"name\":\"acgist\"}");
//                  sse.send(SseEmitter.event().id("1").name("acgist").data("1234"));
                } catch (Exception e) {
                }
            }
        }).start();
        return sse;
    }
    
}
