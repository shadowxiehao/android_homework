package Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.myapplication.Work7Activity;

public class ClockService extends Service {
    public static final String CLOCK_SERVICE_ACTION="clock_service_actoin";
    private boolean controllOpt=false;//控制计时
    public ClockService() { }
    @Override
    public void onCreate(){
        Log.d("广播服务","启动oncreate注册");
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(CLOCK_SERVICE_ACTION);
        //在service中注册广播（serviceController）,接受来自Work7Activity中
        //的广播信息，实现对计时服务的控制（暂停、继续）
        super.registerReceiver(serviceController,intentFilter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        countTime();//执行计时功能
        return Service.START_STICKY;
    }
    //实现计时功能，每隔一秒减少总时间并Work7Activity发送广播
    private void countTime() {
        if (!controllOpt) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("时钟服务", "创建了新进程");
                    Intent intent = new Intent(Work7Activity.CLOCK_ACTION);
                    while (controllOpt) {
                        try {
                            Thread.sleep(1000);
                            //二次检测,防止睡的时候按暂停导致了多计时1秒
                            if(controllOpt) {
                                if (Work7Activity.TIME <= 0) {
                                    sendBroadcast(intent);
                                    stopSelf();
                                    break;
                                }
                                Work7Activity.TIME -= 1000;
                                sendBroadcast(intent);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    //广播接受者，接受来自Work6Activity的广播以便暂停、继续、停止广播
    private BroadcastReceiver serviceController=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String method=intent.getStringExtra("method");
            Log.d("收到广播",method);
            switch (method){
                case "pause":
                    controllOpt=false;
                    break;
                case "continue":
                    countTime();
                    controllOpt=true;
                    break;
                case "stop":
                    //销毁这个服务
                    stopSelf();
                    controllOpt=false;
                    break;
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy(){
        super.unregisterReceiver(serviceController);
    }
}