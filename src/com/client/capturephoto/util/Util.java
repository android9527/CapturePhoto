package com.client.capturephoto.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.client.capturephoto.R;
import com.client.capturephoto.R.string;
import com.client.capturephoto.exception.UploadErrorException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Util
{
    
    public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath();// ·��
    
    /**
     * ���洢���Ƿ����
     * 
     * @return
     */
    public static boolean isHasSdcard()
    {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }
    
    /**
     * ��������Ƿ�����
     * 
     * @return
     */
    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // ��ȡϵͳ����
        if (manager != null)
        {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    { // �ж������Ƿ�������
                        return true; // ����������
                    }
                }
            }
            return false;
        }
        return false;
    }
    
    
    public static void uploadFile(Context context, String url, String filePath, HashMap<String, String> parameters) throws Exception
    {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams()
                .setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                        HttpVersion.HTTP_1_1);
        // ����ʱ
        httpclient.getParams()
                .setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
        // ��ȡ��ʱ
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 90000);
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        //�ڱ��У����ӵ�һ��input type='file'Ԫ�أ�name=��uploadedfile��
        File file = new File(filePath);
        FileBody fileBody = new FileBody(file);
        entity.addPart(context.getString(R.string.request_file_name), fileBody);
        
        if (parameters != null && parameters.size() > 0)
        {
            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext())
            {
                Entry entry = (Entry) it.next();
                // entry.getKey() ����������Ӧ�ļ�
                // entry.getValue() ����������Ӧ��ֵ
                StringBody stringBody = new StringBody(
                        (String) entry.getValue(),
                        ContentType.create("text/plain", Consts.ASCII));
                entity.addPart((String) entry.getKey(), stringBody);
                if (Constant.DEBUG)
                {
//                    Log.e(LOG_TAG, "=====HashMap key = " + entry.getKey()
//                            + " value = " + entry.getValue());
                }
            }
        }
        
        //�ڱ��У����ӵ�һ��input type='file'Ԫ�أ�name=��anotherfile��
        //      File file2 = new File(srcPath2);
        //      FileBody fileBody2 = new FileBody(file2);
        //      entity.addPart("anotherfile", fileBody2);
        
        httppost.setEntity(entity.build());
        HttpResponse response = httpclient.execute(httppost);
        
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null)
            {
                String result = EntityUtils.toString(resEntity, "GBK");
                Log.v("msg", result);
                if(result.contains("ok")){
                }else{
                    throw new UploadErrorException(result);
                }
            }
        }
        httpclient.getConnectionManager().shutdown();
    }
}
