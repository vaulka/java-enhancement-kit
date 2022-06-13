package com.pongsky.kit.sms.utils;

import com.pongsky.kit.sms.entity.SmsTemplate;
import com.pongsky.kit.sms.exception.TenCentSmsException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 腾讯云 SMS 工具类
 *
 * @author pengsenhao
 */
public class TenCentSmsUtils {

    /**
     * region
     *
     * <a href="https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8">地域列表</a>
     */
    private final String region;

    /**
     * sdkAppId
     */
    private final String sdkAppId;

    /**
     * secretId
     */
    private final String secretId;

    /**
     * secretKey
     */
    private final String secretKey;

    /**
     * SMS client
     */
    private final SmsClient client;

    public TenCentSmsUtils(String region, String sdkAppId, String secretId, String secretKey) {
        this.region = region;
        this.sdkAppId = sdkAppId;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.client = getClient();
    }

    /**
     * 创建 client
     *
     * @return 创建 client
     */
    private SmsClient getClient() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, region, clientProfile);
    }

    public List<SendStatus> sendSms(String signName, SmsTemplate template, String phoneNumbers) {
        return this.sendSms(signName, template, Collections.singletonList(phoneNumbers));
    }

    public List<SendStatus> sendSms(String signName, SmsTemplate template, List<String> phoneNumbers) {
        return this.sendSms(signName, template, phoneNumbers, null, null, null);
    }

    /**
     * 发送短信
     * <p>
     * <a href="https://cloud.tencent.com/document/product/382/55981">发送短信</a>
     *
     * @param signName       短信签名名称
     * @param template       短信模版以及模版参数
     * @param phoneNumbers   手机号列表
     * @param extendCode     短信码号扩展号
     * @param sessionContext 用户的 session 内容，可以携带用户侧 ID 等上下文信息，server 会原样返回
     * @param senderId       国内短信无需填写该项；国际/港澳台短信已申请独立 SenderId 需要填写该字段，默认使用公共 SenderId，无需填写该字段
     * @return 短信发送状态列表
     */
    public List<SendStatus> sendSms(String signName, SmsTemplate template, List<String> phoneNumbers,
                                    String extendCode, String sessionContext, String senderId) {
        SendSmsRequest req = new SendSmsRequest();
        req.setPhoneNumberSet(phoneNumbers.toArray(new String[0]));
        req.setSmsSdkAppId(sdkAppId);
        req.setSignName(signName);
        req.setTemplateId(template.getId());
        req.setTemplateParamSet(template.buildParam());
        req.setExtendCode(extendCode);
        req.setSessionContext(sessionContext);
        req.setSenderId(senderId);
        SendSmsResponse resp;
        try {
            resp = client.SendSms(req);
        } catch (TencentCloudSDKException e) {
            throw new TenCentSmsException(e.getLocalizedMessage(), e);
        }
        return Arrays.stream(resp.getSendStatusSet()).collect(Collectors.toList());
    }

}
