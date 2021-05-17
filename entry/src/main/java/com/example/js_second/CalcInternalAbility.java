package com.huawei.health.bloodsugar;

import com.huawei.health.bloodsugar.util.LogUtil;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceInternalAbility;
// ohos相关接口包
import ohos.app.AbilityContext;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PA 端（Internal Ability方式）
 */
public class CalcInternalAbility extends AceInternalAbility {
    private static final String TAG = CalcInternalAbility.class.getSimpleName();
    private static final String BUNDLE_NAME = "com.huawei.hiaceservice";
    private static final String ABILITY_NAME = "com.huawei.hiaceservice.CalcInternalAbility";
    private static final int ERROR = -1;
    private static final int SUCCESS = 0;
    private static final int PLUS = 1001;
    private static final int REGISTER_JS_CALLBACK = 10000;
    private static final int START_ABILITY = 9999;

    private static CalcInternalAbility instance;
    private AbilityContext abilityContext;

    // private Set<IRemoteObject> jsCallbacks = new HashSet<>();
    private IRemoteObject jsCallback;

    // 如果多个Ability实例都需要注册当前InternalAbility实例，需要更改构造函数，设定自己的bundleName和abilityName
    public CalcInternalAbility(AbilityContext abilityContext) {
        super(BUNDLE_NAME, ABILITY_NAME);
        this.abilityContext = abilityContext;
    }


    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        LogUtil.info(TAG, "MainAbility::CalcInternalAbility onRemoteRequest code:"+code + " option:"+option);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (code) {
            case PLUS: {
                String zsonStr = data.readString();
                LogUtil.info(TAG,  "MainAbility::服务端接收到的数据zsonStr:"+zsonStr);
                RequestParam param = ZSONObject.stringToClass(zsonStr, RequestParam.class);
                LogUtil.info(TAG,  "MainAbility::param:"+param);
                // 返回结果当前仅支持String，对于复杂结构可以序列化为ZSON字符串上报
                Map<String, Object> zsonResult = new HashMap<String, Object>();
                zsonResult.put("code", SUCCESS);
                zsonResult.put("abilityResult", param.getFirstNum() + param.getSecondNum());
                // SYNC
                if (option.getFlags() == MessageOption.TF_SYNC) {
                    LogUtil.info(TAG,  "MainAbility::CalcInternalAbility 同步调用:");
                    reply.writeString(ZSONObject.toZSONString(zsonResult));
                } else {
                    // ASYNC
                    LogUtil.info(TAG,  "MainAbility::CalcInternalAbility 异步调用:");
                    MessageParcel reponseData = MessageParcel.obtain();
                    reponseData.writeString(ZSONObject.toZSONString(zsonResult));
                    IRemoteObject remoteReply = reply.readRemoteObject();
                    try {
                        remoteReply.sendRequest(0, reponseData, MessageParcel.obtain(), new MessageOption());
                        reponseData.reclaim();
                    } catch (RemoteException exception) {
                        return false;
                    }
                }
                break;
            }
            case REGISTER_JS_CALLBACK: {
                // 保存js callback 对象
                jsCallback = data.readRemoteObject();

                // 返回js
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", SUCCESS);
                result.put("msg", "gister success");
                // js 同步 调用
                if (option.getFlags() == MessageOption.TF_SYNC) {
                    LogUtil.info(TAG,  "MainAbility jsapp 注册jscallback 同步调用:");
                    reply.writeMap(result);
                } else { // js 异步 调用
                    LogUtil.info(TAG,  "MainAbility jsapp 注册jscallback 异步调用:");
                    MessageParcel reponseData = MessageParcel.obtain();
                    reponseData.writeString(ZSONObject.toZSONString(result));
                    IRemoteObject remoteReply = reply.readRemoteObject();
                    try {
                        remoteReply.sendRequest(0, reponseData, MessageParcel.obtain(), new MessageOption());
                        reponseData.reclaim();
                    } catch (RemoteException exception) {
                        return false;
                    }
                }

                // startTimer();
                break;
            }
            case START_ABILITY:{
                startAbility(data, reply, option);
                return true;
            }
            default: {
                Map<String, Object> zsonResult = new HashMap<String, Object>();
                zsonResult.put("abilityError", ERROR);
                reply.writeString(ZSONObject.toZSONString(zsonResult));
                return false;
            }
        }
        return true;
    }

    private boolean startAbility(MessageParcel data, MessageParcel reply, MessageOption option) {
        String zsonStr = data.readString();
        LogUtil.info(TAG,  "MainAbility::服务端接收到的数据zsonStr:"+zsonStr);
        // {"bundleName":"com.huawei.health.bloodsugar","abilityName":"com.huawei.health.bloodsugar.FourthPageAbility"}
        ZSONObject dataObject = ZSONObject.stringToZSON(zsonStr);
        String bundleName = dataObject.getString("bundleName");
        String abilityName = dataObject.getString("abilityName");

        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
            .withBundleName(bundleName)
            .withAbilityName(abilityName)
            .build();
        intent.setOperation(operation);
        abilityContext.startAbility(intent, 0);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", SUCCESS);
        result.put("msg", "gister success");
        if (option.getFlags() == MessageOption.TF_SYNC) {
            LogUtil.info(TAG,  "MainAbility 同步调用:");
            reply.writeMap(result);
        } else { // js 异步 调用
            LogUtil.info(TAG,  "MainAbility 异步调用:");
            MessageParcel reponseData = MessageParcel.obtain();
            reponseData.writeString(ZSONObject.toZSONString(result));
            IRemoteObject remoteReply = reply.readRemoteObject();
            try {
                remoteReply.sendRequest(0, reponseData, MessageParcel.obtain(), new MessageOption());
                reponseData.reclaim();
            } catch (RemoteException exception) {
                LogUtil.info(TAG,  " is error " + exception);
                return true;
            }
        }
        return false;
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", SUCCESS);
                result.put("msg", "this is callback");
                MessageParcel reponseData = MessageParcel.obtain();
                reponseData.writeString(ZSONObject.toZSONString(result));
                try {
                    jsCallback.sendRequest(0, reponseData, MessageParcel.obtain(), new MessageOption());
                    reponseData.reclaim();
                } catch (RemoteException exception) {
                    exception.printStackTrace();
                }
            }
        }, 3000);
    }

    /**
     * Internal ability registration.
     */
    public static void register(AbilityContext abilityContext) {
        instance = new CalcInternalAbility(abilityContext);
        instance.onRegister(abilityContext);
    }

    private void onRegister(AbilityContext abilityContext) {
        this.abilityContext = abilityContext;
        this.setInternalAbilityHandler((code, data, reply, option) -> {
            return this.onRemoteRequest(code, data, reply, option);
        });
    }

    /**
     * Internal ability deregistration.
     */
    public static void deregister() {
        instance.onDeregister();
    }

    private void onDeregister() {
        abilityContext = null;
        this.setInternalAbilityHandler(null);
    }
}
