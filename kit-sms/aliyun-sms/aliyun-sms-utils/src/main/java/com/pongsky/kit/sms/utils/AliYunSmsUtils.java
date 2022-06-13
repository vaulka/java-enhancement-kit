package com.pongsky.kit.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.pongsky.kit.sms.entity.SmsTemplate;
import com.pongsky.kit.sms.exception.AliYunSmsBizException;
import com.pongsky.kit.sms.exception.AliYunSmsClientException;

import java.util.Collections;
import java.util.List;

/**
 * 阿里云 SMS 工具类
 *
 * @author pengsenhao
 */
public class AliYunSmsUtils {

    /**
     * regionId
     *
     * <a href="https://help.aliyun.com/document_detail/419270.html">服务接入点</a>
     */
    private final String regionId;

    /**
     * accessKeyId
     */
    private final String accessKeyId;

    /**
     * accessKeySecret
     */
    private final String accessKeySecret;

    /**
     * SMS client
     */
    private final IAcsClient client;

    public AliYunSmsUtils(String regionId, String accessKeyId, String accessKeySecret) {
        this.regionId = regionId;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.client = getClient();
    }

    /**
     * 创建 client
     *
     * @return 创建 client
     */
    private IAcsClient getClient() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    public void sendSms(String signName, SmsTemplate<?> template, String phoneNumbers) {
        this.sendSms(signName, template, Collections.singletonList(phoneNumbers));
    }

    public void sendSms(String signName, SmsTemplate<?> template, List<String> phoneNumbers) {
        this.sendSms(signName, template, phoneNumbers, null, null);
    }

    /**
     * 成功 请求状态码
     */
    private static final String SUCCESS_CODE = "OK";

    /**
     * 发送短信
     * <p>
     * <a href="https://help.aliyun.com/document_detail/419273.html">发送短信</a>
     *
     * @param signName        短信签名名称
     * @param template        短信模版以及模版参数
     * @param phoneNumbers    手机号列表
     * @param smsUpExtendCode 上行短信扩展码
     * @param outId           外部流水扩展字段
     */
    public void sendSms(String signName, SmsTemplate<?> template, List<String> phoneNumbers,
                        String smsUpExtendCode, String outId) {
        SendSmsRequest request = new SendSmsRequest();
        request.setSignName(signName);
        request.setTemplateCode(template.getCode());
        request.setTemplateParam(template.buildParam());
        request.setPhoneNumbers(String.join(",", phoneNumbers));
        request.setSmsUpExtendCode(smsUpExtendCode);
        request.setOutId(outId);
        SendSmsResponse response;
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            throw new AliYunSmsClientException(e.getErrMsg(), e);
        }
        if (!SUCCESS_CODE.equals(response.getCode())) {
            throw new AliYunSmsBizException(response.getMessage());
        }
    }

}
