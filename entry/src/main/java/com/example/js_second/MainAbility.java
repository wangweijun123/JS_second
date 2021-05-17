package com.huawei.health.bloodsugar;

import com.huawei.health.bloodsugar.CalcInternalAbility;

import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * MainAbility 负责装载js资源,如果这里需要java提供服务给js调用，必须在这里注册
 */
public class MainAbility extends AceAbility {
    public static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        // 注册
        HiLog.info(LABEL_LOG, "MainAbility::onBackground");
        System.out.println("MainAbility register ...");
        CalcInternalAbility.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "MainAbility::deregister");
        // 去注册
        System.out.println("MainAbility deregister ...");
        CalcInternalAbility.deregister();
    }

}
