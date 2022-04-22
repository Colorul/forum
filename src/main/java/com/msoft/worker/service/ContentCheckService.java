package com.msoft.worker.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msoft.worker.config.WechatCodeConfig;
import com.msoft.worker.repository.model.CheckVm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class ContentCheckService {
    private final WechatService wechatService;
    private final WechatCodeConfig wechatCodeConfig;

    public void check(String accessCode, String openId, String content) {
        CheckVm vm = new CheckVm();
        vm.setContent(content);
        vm.setOpenid(openId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.exchange("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + getAccessCode(accessCode),
                HttpMethod.POST, new HttpEntity<>(vm), new ParameterizedTypeReference<>() {
                });
        JSONObject demoObj = response.getBody();
        Assert.notNull(demoObj, "内容审计失败！");
        log.info("审计结果:{}", demoObj);
        if (!("0".equals(demoObj.get("errcode").toString()) && "pass".equals(((LinkedHashMap)demoObj.get("result")).get("suggest")))) {
            log.error("发言存在敏感词:{}", demoObj);
            throw new RuntimeException("发言存在敏感词!");
        }
    }

    private String getAccessCode(String code) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+this.wechatCodeConfig.getAppId()+"&secret=" + this.wechatCodeConfig.getAppSecret();
        JSONObject jsonFromWeb = getJsonFromWeb(url);
        if (jsonFromWeb.containsKey("errcode")) {
            log.error("getAccessCode登陆失败,请联系管理员处理!{}", jsonFromWeb);
            throw new RuntimeException("登陆失败,请联系管理员处理!");
        }
        return jsonFromWeb.getString("access_token");
    }

    /**
     * 调用第三方get接口 并返回结果
     *
     * @param apiUrl 调用地址 含参数
     * @return 结果
     */
    private JSONObject getJsonFromWeb(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            return JSON.parseObject(sb.toString());
        } catch (Exception e) {
            log.error(String.format("获取%s内容失败,message:%s", apiUrl, e.getMessage()), e);
        }
        return new JSONObject();
    }
}
