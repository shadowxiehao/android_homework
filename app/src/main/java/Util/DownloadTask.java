package Util;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import Interface.DownloadListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    //AsyncTask 的三个泛型 第一个泛型为 doinbackground方法参数类型
    //第二个泛型为 onProgressUpdate的方法参数
    //第三泛型为 doinbackground返回值类型,doinbackground返回值传入onPostExecute方法参数

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;

    private DownloadListener listener;
    private String fieName;
    private String downloadUrl;

    private boolean isPaused = false;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;

        long downloadLength = 0;
        //下载网址
        downloadUrl = params[0];
        fieName = downloadUrl.substring(downloadUrl.lastIndexOf("/")) ;
        //相对路径
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File(dir + fieName);
        Log.d("file",file.toString());

        //如果文件存在,那么获取已经下载的文件的长度
        if (file.exists()) {
            downloadLength = file.length();
        }
        //获取文件总长度
        long contentLength = getContentLength(downloadUrl);
        Log.d("URL",downloadUrl);
        Log.d("name",fieName);
        Log.d("dir",dir);
        Log.d("contentLength",contentLength+"");
        //如果没有获取到网络上文件长度则失败
        if (contentLength == 0){
            return TYPE_FAILED;
        }
        //如果获取到的网络文件长度和本地相同则是已经下载成功
        else if (contentLength == downloadLength) {
            return TYPE_SUCCESS;
        }

        //开始下载

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //向服务器添加请求头可以跳过已经下载的地方进行下载
                .addHeader("RANGE","bytes=" +  downloadLength + "-")
                .url(downloadUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null){
                is = response.body().byteStream();
                //随机读写 rw 读写 如果文件不存在自动创建
                saveFile = new RandomAccessFile(file,"rw");
                //跳过已经下载的长度
                saveFile.seek(downloadLength);
                //循环字节数组
                byte [] bytes = new byte[1024];
                int length = 0;
                int total = 0;
                while ((length = is.read(bytes)) != -1){
                    //正在读取流时暂停
                    if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        saveFile.write(bytes,0,length);
                        total += length;
                        //计算已经下载的百分比
                        int progress = (int) ((total + downloadLength)*100/contentLength);
                        //传递下载进度
                        publishProgress(progress);
                    }
                }
                is.close();
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果网络突然中断则会从上一个判断中出来
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        listener.onProgress(progress);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPause();
                break;
        }
    }


    //访问网络获取文件大小
    public long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //给外部调用控制下载暂停
    public void pauseDoenload(){
        isPaused = true;
    }

}

