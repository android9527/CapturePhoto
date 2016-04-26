package com.client.capturephoto;

import java.util.ArrayList;

import com.client.capturephoto.adapter.ImageInfo;
import com.client.capturephoto.db.MySQLiteOpenHelper;
import com.client.capturephoto.util.ProgressAsyncTask;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UnUploadActivity extends BaseActivity
{
    
    private ListView mListView;
    
    ArrayList<ImageInfo> imageInfos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.un_upload_picture);
        mListView = (ListView) findViewById(R.id.un_upload_list);
        initProgressTask();
    }
    
    private void initProgressTask()
    {
        ProgressAsyncTask progressAsyncTask = new ProgressAsyncTask(
                UnUploadActivity.this)
        {
            
            @Override
            protected void onPostExecute(Object obj)
            {
                super.onPostExecute(obj);
                
                if (imageInfos != null && imageInfos.size() > 0)
                {
                    MyAdapter adapter = new MyAdapter(UnUploadActivity.this,
                            imageInfos);
                    mListView.setAdapter(adapter);
                }
            }
            
            @Override
            protected Object doInBackground(Object... params)
            {
                
                MySQLiteOpenHelper helper = new MySQLiteOpenHelper(
                        UnUploadActivity.this);
                imageInfos = MySQLiteOpenHelper.queryAll(helper);
                
                return super.doInBackground(params);
            }
        };
        progressAsyncTask.execute();
    }
    
    public class MyAdapter extends BaseAdapter
    {
        public static final String ITEM_NAME = "item_name";
        
        /**
         * ²Ëµ¥ÁÐ±í
         */
        private ArrayList<ImageInfo> list;
        
        private Context context;
        
        public MyAdapter(Context context, ArrayList<ImageInfo> list)
        {
            this.context = context;
            this.list = list;
        }
        
        @Override
        public int getCount()
        {
            return list.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if (convertView == null)
            {
                
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.un_upload_picture_item, null);
                holder = new ViewHolder();
                holder.fullNameText = (TextView) convertView.findViewById(R.id.picture_fullname);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fullNameText.setText(list.get(position).fullName);
            return convertView;
        }
        
        class ViewHolder
        {
            TextView fullNameText;
        }
        
    }
}
