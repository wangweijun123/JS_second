package com.huawei.health.bloodsugar.slice;

import com.huawei.health.bloodsugar.ResourceTable;
import com.huawei.health.bloodsugar.util.LogUtil;

import ohos.aafwk.ability.AbilityForm;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.OnClickListener;
import ohos.aafwk.ability.ViewsStatus;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

public class FourthPageSlice extends AbilitySlice implements Component.ClickedListener {
    private static final String TAG = "FourthPageSlice";
    private static final int eventId1 = 0;
    private static final int eventId2 = 1;

    private class MyEventHandler extends EventHandler {

        public MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        /**
         * 回调已经被切换到了子线程
         * @param event
         */
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            LogUtil.info(TAG, "processEvent tid:" +
                Thread.currentThread().getId()+", tname:"+Thread.currentThread().getName());
            LogUtil.info(TAG, "processEvent :" +  event.eventId);

            if (event == null) {
                return;
            }
            int eventId = event.eventId;
            long param = event.param;
            Object object = event.object;
            switch (eventId | param) {
                case eventId1:
                    // 待执行的操作，由开发者定义
                    break;
                case eventId2:
                    // 将原先线程的EventRunner实例投递给新创建的线程
                    if (object instanceof EventRunner) {
                        EventRunner runner2 = (EventRunner)object;
                    }
                    // 将原先线程的EventRunner实例与新创建的线程的EventHandler绑定
                    EventHandler myHandler2 = new EventHandler(runner2) {
                        @Override
                        public void processEvent(InnerEvent event) {
                            //需要在原先线程执行的操作
                        }
                    };
                    int eventId = 1;
                    long param = 0;
                    Object object = null;
                    InnerEvent event2 = InnerEvent.get(eventId, param, object);
                    myHandler2.sendEvent(event2); // 投递事件到原先的线程
                    break;
                default:
                    break;
            }
        }
    }
    MyEventHandler myHandler;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_fourth_page);
        findComponentById(ResourceTable.Id_threadTest).setClickedListener(this);
        findComponentById(ResourceTable.Id_postTaskTest).setClickedListener(this);
        //create()的参数是 true时或者名字，则为托管模式，内部创建新的线程
        EventRunner runner = EventRunner.create("downloadRunner");
        // 需要对 EventRunner 的实例进行校验，因为创建 EventRunner 可能失败，如创建线程失败时，创建 EventRunner 失败。
        if (runner == null) {
            return;
        }
        myHandler = new MyEventHandler(runner);
    }

    @Override
    public void onClick(Component component) {
        int id = component.getId();
        switch (id) {
            case ResourceTable.Id_threadTest:
                threadTest();
                break;
            case ResourceTable.Id_postTaskTest:
                postTaskTest();
                break;

        }
    }

    private void threadTest() {
        LogUtil.info(TAG, "call tid:" +
            Thread.currentThread().getId()+", tname:"+Thread.currentThread().getName());
        // 获取事件实例，其属性 eventId, param, object 由开发者确定，代码中只是示例。

        long param = 0;
        Object object = null;
        InnerEvent event1 = InnerEvent.get(eventId1, param, object);
        InnerEvent event2 = InnerEvent.get(eventId2, param, object);
        // 优先级 immediate，投递之后立即处理，延时为 0ms，该语句等价于
        // 同步投递sendSyncEvent(event1，EventHandler.Priority.immediate);
        myHandler.sendEvent(event1, 0, EventHandler.Priority.IMMEDIATE);
        myHandler.sendEvent(event2, 2, EventHandler.Priority.IMMEDIATE); // 延时 2ms 后立即处理
    }

    /**
     * 两个runnable跑在了同一个线程
     */
    private void postTaskTest() {
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                // 待执行的操作，由开发者定义
                LogUtil.info(TAG, "task1 tid:" +
                    Thread.currentThread().getId()+", tname:"+Thread.currentThread().getName());
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                // 待执行的操作，由开发者定义
                LogUtil.info(TAG, "task2 tid:" +
                    Thread.currentThread().getId()+", tname:"+Thread.currentThread().getName());
            }
        };
        //优先级为 immediate，延时 0ms，该语句等价于
        // 同步投递myHandler.postSyncTask(task1，EventHandler.Priority.immediate);
        myHandler.postTask(task1, 0, EventHandler.Priority.IMMEDIATE);
        // 延时2ms后立即执行
        myHandler.postTask(task2, 2, EventHandler.Priority.IMMEDIATE);
    }

    private DirectionalLayout addLayout() {
        Context context = getContext();
        // 步骤1 声明布局
        DirectionalLayout directionalLayout = new DirectionalLayout(context);
        // 步骤2 设置布局大小
        directionalLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        directionalLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
        // 步骤3 设置布局属性及ID（ID视需要设置即可）
        directionalLayout.setOrientation(Component.VERTICAL);
        directionalLayout.setPadding(32, 32, 32, 32);

        Text text = new Text(context);
        text.setText("My name is Text.");
        text.setTextSize(50);
        text.setId(100);
        // 步骤4.1 为组件添加对应布局的布局属性
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(
            ComponentContainer.LayoutConfig.MATCH_CONTENT,
            ComponentContainer.LayoutConfig.MATCH_CONTENT);
        layoutConfig.alignment = LayoutAlignment.HORIZONTAL_CENTER;
        text.setLayoutConfig(layoutConfig);

        // 步骤4.2 将Text添加到布局中
        directionalLayout.addComponent(text);
        return directionalLayout;
        /*// 类似的添加一个Button
        Button button = new Button(context);
        layoutConfig.setMargins(0, 50, 0, 0);
        button.setLayoutConfig(layoutConfig);
        button.setText("My name is Button.");
        button.setTextSize(50);
        button.setId(100);
        ShapeElement background = new ShapeElement();
        background.setRgbColor(new RgbColor(0, 125, 255));
        background.setCornerRadius(25);
        button.setBackground(background);
        button.setPadding(10, 10, 10, 10);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            // 在组件中增加对点击事件的检测
            public void onClick(Component Component) {
                // 此处添加按钮被点击需要执行的操作
            }
        });
        directionalLayout.addComponent(button);*/
    }
}
