package com.acgist.health.manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.configuration.ManagerProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StunManager {

    private static int MAGIC_COOKIE = 0x2112A442;

    private final ManagerProperties managerProperties;

    public boolean localAddress(String ip) {
        try {
            final InetAddress address = InetAddress.getByName(ip);
            return
                // 通配地址：0.0.0.0
                address.isAnyLocalAddress()  ||
                // 环回地址：127.0.0.1
                address.isLoopbackAddress()  ||
                // 链接地址：虚拟网卡
                address.isLinkLocalAddress() ||
                // 组播地址
                address.isMulticastAddress() ||
                // 本地地址：A/B/C类本地地址
                address.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            log.error("解析IP异常：{}", ip, e);
        }
        return false;
    }

    public String getIp() {
        if(this.managerProperties.getStun() == null) {
            return null;
        }
        for (final String stun : this.managerProperties.getStun()) {
            final int index = stun.indexOf(':');
            if(index < 0) {
                continue;
            }
            final String ip = this.getIp(stun.substring(0, index), Integer.valueOf(stun.substring(index + 1)));
            if(ip != null) {
                return ip;
            }
        }
        return null;
    }

    public String getIp(String host, Integer port) {
        try (
            final DatagramSocket socket = new DatagramSocket();
        ) {
            socket.setSoTimeout(5000);
            final Random random = new Random();
            final byte[] transactionId = new byte[12];
            random.nextBytes(transactionId);
            final ByteBuffer requestBody = ByteBuffer.allocate(20);
            requestBody.putShort((short) 0x0001);
            requestBody.putShort((short) 0);
            requestBody.putInt(MAGIC_COOKIE);
            requestBody.put(transactionId);
            final DatagramPacket request = new DatagramPacket(requestBody.array(), 20, new InetSocketAddress(host, port));
            socket.send(request);
            final ByteBuffer responseBody = ByteBuffer.allocate(512);
            final DatagramPacket response = new DatagramPacket(responseBody.array(), 512);
            socket.receive(response);
            responseBody.position(20);
            while (responseBody.hasRemaining()) {
                final short type   = responseBody.getShort();
                final short length = responseBody.getShort();
                if (type == 0x0001) {
                    responseBody.get(); // header
                    responseBody.get(); // family
                    final int outerPort = Short.toUnsignedInt(responseBody.getShort());
                    final int outerIp   = requestBody.getInt();
                    final InetAddress address = InetAddress.getByAddress(ByteBuffer.allocate(4).putInt(outerIp).array());
                    log.info("外网地址：{} - {}", address.getHostAddress(), outerPort);
                    return address.getHostAddress();
                } else if(type == 0x0020) {
                    responseBody.get(); // header
                    responseBody.get(); // family
                    final int outerPort = Short.toUnsignedInt((short) (responseBody.getShort() ^ (MAGIC_COOKIE >> 16)));
                    final int outerIp   = responseBody.getInt() ^ MAGIC_COOKIE;
                    final InetAddress address = InetAddress.getByAddress(ByteBuffer.allocate(4).putInt(outerIp).array());
                    log.info("外网地址：{} - {}", address.getHostAddress(), outerPort);
                    return address.getHostAddress();
                } else {
                    responseBody.position(responseBody.position() + length);
                }
            }
        } catch (Exception e) {
            log.error("STUN映射异常", e);
        }
        return null;
    }

}
