package com.acgist.health.manager.action.dns;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.StunManager;
import com.acgist.health.manager.action.IDns;
import com.acgist.health.manager.configuration.ManagerProperties.Dns;
import com.acgist.health.manager.configuration.ManagerProperties.Server;
import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponseBody.DescribeSubDomainRecordsResponseBodyDomainRecordsRecord;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * https://api.aliyun.com/product/Alidns
 * https://help.aliyun.com/document_detail/378657.html
 * https://api.aliyun.com/api/Alidns/2015-01-09/UpdateDomainRecord?RegionId=
 * https://api.aliyun.com/api/Alidns/2015-01-09/DescribeSubDomainRecords?RegionId=
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunDns implements IDns {

    private final StunManager stunManager;

    @Override
    public String type() {
        return IDns.TYPE_ALIYUN;
    }

    @Override
    public String detail(Dns dns) {
        final Config config = new Config()
            .setAccessKeyId(dns.getAccessId())
            .setAccessKeySecret(dns.getAccessKey())
            .setEndpoint(dns.getEndpoint());
        final RuntimeOptions runtime = new RuntimeOptions();
        final DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest()
            .setSubDomain(dns.getDomain());
        try {
            final Client client = new Client(config);
            final DescribeSubDomainRecordsResponse response = client.describeSubDomainRecordsWithOptions(request, runtime);
            if (response.getStatusCode() != 200) {
                return null;
            }
            final DescribeSubDomainRecordsResponseBodyDomainRecordsRecord record = response.getBody().getDomainRecords().getRecord().stream().filter(v -> Objects.equals(dns.getDomainType(), v.getType())).findFirst().orElse(null);
            dns.setDomainId(record.getRecordId());
            return record.getValue();
        } catch (Exception e) {
            log.error("解析DNS异常", e);
        }
        return null;
    }

    @Override
    public boolean modify(Dns dns, Server server) {
        String ip = server.getHost();
        if(this.stunManager.localAddress(ip)) {
            ip = this.stunManager.getIp();
            log.info("内网地址自动映射外网地址：{} - {}", server.getHost(), ip);
        }
        final Config config = new Config()
            .setAccessKeyId(dns.getAccessId())
            .setAccessKeySecret(dns.getAccessKey())
            .setEndpoint("alidns.cn-hangzhou.aliyuncs.com");
        final RuntimeOptions runtime = new RuntimeOptions();
        final UpdateDomainRecordRequest request = new com.aliyun.alidns20150109.models.UpdateDomainRecordRequest()
            .setRecordId(dns.getDomainId())
            .setRR(dns.getDomainRr())
            .setTTL(dns.getDomainTtl())
            .setType(dns.getDomainType())
            .setValue(ip);
        try {
            final Client client = new Client(config);
            final UpdateDomainRecordResponse response = client.updateDomainRecordWithOptions(request, runtime);;
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            log.error("修改DNS异常", e);
        }
        return false;
    }

}
