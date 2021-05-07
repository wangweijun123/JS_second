package com.example.js_second;

import ohos.ace.ability.AceInternalAbility;
// ohos相关接口包
import ohos.app.AbilityContext;
import ohos.hiviewdfx.HiLog;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * PA 端（Internal Ability方式）
 */
public class CalcInternalAbility extends AceInternalAbility {
    private static final String TAG = CalcInternalAbility.class.getSimpleName();
    private static final String BUNDLE_NAME = "com.huawei.hiaceservice";
    private static final String ABILITY_NAME = "CalcInternalAbility";
    private static final int ERROR = -1;
    private static final int SUCCESS = 0;
    private static final int PLUS = 1001;
    private static final int CHENG = 1002;
    private static final int CHU = 1003;

    private static CalcInternalAbility instance;
    private AbilityContext abilityContext;

    // 如果多个Ability实例都需要注册当前InternalAbility实例，需要更改构造函数，设定自己的bundleName和abilityName
    public CalcInternalAbility() {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        HiLog.info(MainAbility.LABEL_LOG, "MainAbility::CalcInternalAbility onRemoteRequest code:"+code +
                ", tid:"+Thread.currentThread().getId() + ", " + Thread.currentThread().getName());

        switch (code) {
            case PLUS: {
                if (plus(data, reply, option, 2000)) return false;
                break;
            }
            case CHENG: {
                if (plus(data, reply, option, 3000)) return false;
                break;
            }
            case CHU: {
                if (plus(data, reply, option, 4000)) return false;
                break;
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

    private boolean plus(MessageParcel data, MessageParcel reply,
                         MessageOption option, long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String zsonStr = data.readString();
        RequestParam param = ZSONObject.stringToClass(zsonStr, RequestParam.class);
        // 返回结果当前仅支持String，对于复杂结构可以序列化为ZSON字符串上报
        Map<String, Object> zsonResult = new HashMap<String, Object>();
        zsonResult.put("code", SUCCESS);
        zsonResult.put("abilityResult", param.getFirstNum() + param.getSecondNum());
        // SYNC
        if (option.getFlags() == MessageOption.TF_SYNC) {
            HiLog.info(MainAbility.LABEL_LOG, "MainAbility::CalcInternalAbility 同步调用:");
            reply.writeString(ZSONObject.toZSONString(zsonResult));
        } else {
            // ASYNC
            HiLog.info(MainAbility.LABEL_LOG, "MainAbility::CalcInternalAbility 异步调用:");
            MessageParcel reponseData = MessageParcel.obtain();
            reponseData.writeString(ZSONObject.toZSONString(zsonResult));
            IRemoteObject remoteReply = reply.readRemoteObject();
            try {
                remoteReply.sendRequest(0, reponseData, MessageParcel.obtain(), new MessageOption());
                reponseData.reclaim();
            } catch (RemoteException exception) {
                return true;
            }
        }
        return false;
    }


    /**
     * Internal ability registration.
     */
    public static void register(AbilityContext abilityContext) {
        instance = new CalcInternalAbility();
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