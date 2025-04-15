package com.acgist.health;

import org.junit.jupiter.api.Test;

import com.acgist.health.manager.StunManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class StunTests {

    @Test
    void testStun() {
        final StunManager stunManager = new StunManager(null);
        log.info("{}", stunManager.getIp("stun1.l.google.com", 19302));
        log.info("{}", stunManager.getIp("stun2.l.google.com", 19302));
        log.info("{}", stunManager.getIp("stun3.l.google.com", 19302));
        log.info("{}", stunManager.getIp("stun4.l.google.com", 19302));
    }

}
