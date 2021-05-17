
const MESSAGE_CODE_START_ABILITY = 9999;
export default {

    openBluetoothAdapter() {
        console.log('openBluetoothAdapter ...')
    },

    startAbility: async function(bundleName, abilityName ,startAbilityCallback) {
        let param = {};
        param.bundleName = bundleName;
        param.abilityName = abilityName;
        param.messageCode = MESSAGE_CODE_START_ABILITY;
        param.abilityType = 1;
        // 0: Synchronous mode (default value) 1: Asynchronous
        param.syncOption = 1;

        let actionData = {};
        actionData.withBundleName = "com.huawei.health.bloodsugar";
        actionData.withAbilityName = "com.huawei.health.bloodsugar.FourthPageAbility";
        param.data = actionData;

        let resultStr = await FeatureAbility.subscribeAbilityEvent(param, startAbilityCallback);
                let resultObj = JSON.parse(resultStr);
        console.info('Start Ability result is:' + JSON.stringify(resultObj));
    },

    startAbility2: async function(bundleName, abilityName ,startAbilityCallback) {
        let actionData = {};
        actionData.bundleName = "com.huawei.health.bloodsugar";
        actionData.abilityName = "com.huawei.health.bloodsugar.FourthPageAbility";
//        actionData.params = params;
        let action = {};
        action.bundleName = bundleName;
        action.abilityName = abilityName;
        action.abilityType = 1;
        action.syncOption = 1;
        action.messageCode = MESSAGE_CODE_START_ABILITY;
        action.data = actionData;
        let resultStr = await FeatureAbility.subscribeAbilityEvent(action, startAbilityCallback);
        let resultObj = JSON.parse(resultStr);
        console.info('Start Ability result is:' + JSON.stringify(resultObj));
    },
    startAbility3: async function(bundleName, abilityName ,startAbilityCallback) {
        let actionData = {};
        actionData.bundleName = "com.huawei.health.bloodsugar";
        actionData.abilityName = "com.huawei.health.bloodsugar.FourthPageAbility";
        //        actionData.params = params;
        let action = {};
        action.bundleName = bundleName;
        action.abilityName = abilityName;
        action.abilityType = 1;
        action.syncOption = 1;
        action.messageCode = MESSAGE_CODE_START_ABILITY;
        action.data = actionData;
        let resultStr = await FeatureAbility.callAbility(action);
//        let resultStr = await FeatureAbility.subscribeAbilityEvent(action, startAbilityCallback);
        console.info('Start Ability result is:' + resultStr);
        let resultObj = JSON.parse(resultStr);
        console.info('Start Ability result is:' + JSON.stringify(resultObj));
    }
}
