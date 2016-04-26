/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.client.capturephoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity extends Activity
{
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //		getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_clear_memory_cache:
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().stop();
                return true;
            case R.id.item_clear_disc_cache:
                ImageLoader.getInstance().clearDiscCache();
                return true;
            default:
                return false;
        }
    }
    
    public static class Extra
    {
        public static final String IMAGES = "com.client.capturephoto.IMAGES";
        
        public static final String IMAGE_POSITION = "com.client.capturephoto.IMAGE_POSITION";
    }
    
    protected void showToast(int resID)
    {
        Toast.makeText(this, resID, Toast.LENGTH_LONG).show();
    }
    
    protected void showToastInUI(final int resID)
    {
        runOnUiThread(new Runnable()
        {
            
            @Override
            public void run()
            {
                showToast(resID);
            }
        });
    }
    
    protected void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    
    protected void showToastInUI(final String msg)
    {
        runOnUiThread(new Runnable()
        {
            
            @Override
            public void run()
            {
                showToast(msg);
            }
        });
    }
    
    private ProgressDialog progressDialog;
    
    protected void showProgress(String msg)
    {
        progressDialog = new ProgressDialog(this);
        // dialog.setIcon(R.drawable.icon);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    
    protected void dismissProgress()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    
}
