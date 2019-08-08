package com.example.customerserviceim;

import android.content.Context;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.util.EMLog;

import java.util.List;

public class CustomerIMHelper {

    protected static final String TAG = "CustomerIMHelper";
    private static CustomerIMHelper instance = null;
    private UIProvider uiProvider;

    private CustomerIMHelper() {

    }

    public synchronized static CustomerIMHelper getInstance() {
        if (instance == null) {
            instance = new CustomerIMHelper();
        }
        return instance;
    }

    /**
     * 初始化操作
     * @param context
     * application context
     */
    public void init(Context context){

        //初始化配置客服Options
        ChatClient.Options options = new ChatClient.Options();
        //设置手动关联appkey 在客服系统app渠道下创建手动关联
        options.setAppkey("1107180814253417#myeasuichatdemo");
        //设置租户ID 在客服管理员模式下 设置 企业信息中可以找到
        options.setTenantId("8019");
        //坐席输入状态  显示访客排队人数 消息预知功能
        options.showAgentInputState().showVisitorWaitCount().showMessagePredict();
        //开启客服debug模式
        options.setConsoleLog(true);

        // 判断初始化成功后会返回 true   初始化成功之后在去进行其它操作
        if (ChatClient.getInstance().init(context,options)){
            uiProvider = UIProvider.getInstance();
            //初始化EaseUI
            uiProvider.init(context);
            //因为EaseUI中注释了初始化设置EMOptions的方法 这里不需要new 直接获取EMOptions对象
            EMOptions EMOptions = EMClient.getInstance().getOptions();
            // TODO: 2019/8/8  获取到EMOptions后进行相关配置
            //注释掉init中初始化环信SDK的方法
            EaseUI.getInstance().init(context,EMOptions);

            ConnectListener();
            MessageListener();

        }

    }

    /**
     *  连接监听
     */
    private void ConnectListener(){

        EMConnectionListener  connectionListener = new EMConnectionListener() {

            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int ErrorCode) {
                EMLog.e("ConnectListener", "onDisconnect" + ErrorCode);
                if (ErrorCode == EMError.USER_REMOVED){
                    //账号被移除

                }else if (ErrorCode == EMError.USER_LOGIN_ANOTHER_DEVICE){
                    //账号在其他设备登录
                    ChatClient.getInstance().logout(false, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(int code, String error) {

                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                }
            }
        };
        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    /**
     *  消息监听(IM的消息监听也是可以收到客服消息的 这里就直接用IM的消息监听)
     */
    private void MessageListener(){

        EMMessageListener messageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //正常普通消息回调
                for (EMMessage message : list) {
                    EMLog.d(TAG, "onMessage : " + message.toString());
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                //透传消息回调
                for (EMMessage message : list) {
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    EMLog.d(TAG, "onCMDMessage action: " + cmdMsgBody.action() +" onMessage" + message.toString());
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                //收到已读回执

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                //收到已送达回执

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                //消息状态变动
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);

    }

}
