/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.client.capturephoto.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.apache.http.util.EntityUtils;

import com.client.capturephoto.adapter.ImageInfo;

import android.os.AsyncTask;
import android.util.Log;

public class FileOperationHelper
{
    private static final String LOG_TAG = "FileOperation";
    private ArrayList<ImageInfo> mCurFileNameList = new ArrayList<ImageInfo>();
    
    private IOperationProgressListener mOperationListener;

    public interface IOperationProgressListener {
        void onFinish();
    }

    public FileOperationHelper( IOperationProgressListener l) {
        mOperationListener = l;
    }
    
    public FileOperationHelper() {
    }
    
    public static boolean deleteFiles(ArrayList<ImageInfo> files)
    {
        for (ImageInfo f : files)
        {
            deleteFile(f);
        }
        return true;
    }
    
    public static boolean deleteFile(ImageInfo f)
    {
        if (f == null)
        {
            Log.e(LOG_TAG, "DeleteFile: null parameter");
            return false;
        }
        
        File file = new File(f.fullName);
//        boolean directory = file.isDirectory();
//        if (directory)
//        {
            // do nothing
            //            for (File child : file.listFiles(mFilter)) {
            //                if (Util.isNormalFile(child.getAbsolutePath())) {
            //                    DeleteFile(Util.GetImageInfo(child, mFilter, true));
            //                }
            //            }
//        }
        boolean result = file.delete();
        if (f.imageCode != null)
        {
            file = new File(f.imageCode);
            file.delete();
        }
        Log.v(LOG_TAG, "DeleteFile >>> " + f.fullName + " >>> " + result);
        return result;
    }
    
    /**
     * 复制一个新的列表
     * @param files
     * @return
     */
    public static ArrayList<ImageInfo> copyFileList(ArrayList<ImageInfo> files)
    {
        ArrayList<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
        for (ImageInfo f : files)
        {
            imageInfos.add(f);
        }
        return imageInfos;
    }
}
