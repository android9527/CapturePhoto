package com.client.capturephoto.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.client.capturephoto.R;
import com.client.capturephoto.R.drawable;
import com.client.capturephoto.R.id;
import com.client.capturephoto.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    
    private List<ImageInfo> list;
    
    private DisplayImageOptions options;
    
    // 用来控制CheckBox的选中状况
    private ArrayList<ImageInfo> isSelected;
    
    private LayoutParams params = null;
    
    public ImageAdapter(Context c, List<ImageInfo> list)
    {
        mInflater = LayoutInflater.from(c);
        this.list = list;
        options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .build();
        isSelected = new ArrayList<ImageInfo>();
        params = new LayoutParams(0, 0);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
    }
    
    public void setPictureLayoutParams(int pictureWidth, int pictureHeight)
    {
        params.width = pictureWidth;
        params.height = pictureHeight;
    }
    
    public int getCount()
    {
        return list.size();
    }
    
    public Object getItem(int position)
    {
        return list.get(position);
    }
    
    public long getItemId(int position)
    {
        
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.imageview_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(Uri.fromFile(new File(
                list.get(position).fullName)).toString(),
                holder.imageView,
                options);
        
        holder.imageView.setLayoutParams(params);
        //        ImageLoader.getInstance().displayImage(Uri.fromFile(new File(
        //                list.get(position).fullName)).toString(),
        //                holder.imageView,
        //                options,
        //                new SimpleImageLoadingListener()
        //                {
        //                    @Override
        //                    public void onLoadingStarted(String imageUri, View view)
        //                    {
        //                    }
        //                    
        //                    @Override
        //                    public void onLoadingFailed(String imageUri, View view,
        //                            FailReason failReason)
        //                    {
        //                        String message = null;
        //                        switch (failReason.getType())
        //                        {
        //                            case IO_ERROR:
        //                                message = "Input/Output error";
        //                                break;
        //                            case DECODING_ERROR:
        //                                message = "Image can't be decoded";
        //                                break;
        //                            case NETWORK_DENIED:
        //                                message = "Downloads are denied";
        //                                break;
        //                            case OUT_OF_MEMORY:
        //                                message = "Out Of Memory error";
        //                                break;
        //                            case UNKNOWN:
        //                                message = "Unknown error";
        //                                break;
        //                        }
        //                        
        //                    }
        //                    
        //                    @Override
        //                    public void onLoadingComplete(String imageUri, View view,
        //                            Bitmap loadedImage)
        //                    {
        //                        
        //                        System.err.println("onLoadingComplete"
        //                                + loadedImage.getWidth() + "  "
        //                                + loadedImage.getHeight());
        //                        
        //                    }
        //                });
        if (mCurrentMode == Mode.Pick)
        {
            holder.checkBox.setChecked(isSelected.contains(list.get(position)));
            holder.checkBox.setClickable(false);
            holder.checkBox.setVisibility(View.VISIBLE);
            convertView.setBackgroundResource(isSelected.contains(list.get(position)) ? R.drawable.grid_view_item_press_shape
                    : R.drawable.grid_view_item_selector_xml);
            //            convertView.setBackgroundResource(isSelected.contains(list.get(position)) ? R.drawable.login_button_pressed
            //                    : R.drawable.login_btn_selector);
        }
        else
        {
            convertView.setBackgroundResource(R.drawable.grid_view_item_selector_xml);
            //            convertView.setBackgroundResource(R.drawable.login_btn_selector);
            holder.checkBox.setVisibility(View.GONE);
        }
        // holder.checkBox.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // // setSelected(position);
        // if (getSelected(position)) {
        // // holder.imageView.setBackgroundResource(R.drawable.four_bg);
        // isSelected.remove((Integer)position);
        // } else {
        // // holder.imageView.setBackgroundColor(Color.TRANSPARENT);
        // isSelected.add(position);
        // }
        // }
        // });
        return convertView;
    }
    
    public class ViewHolder
    {
        ImageView imageView;
        
        CheckBox checkBox;
    }
    
    public enum Mode
    {
        View, Pick
    };
    
    private Mode mCurrentMode;
    
    public void setMode(Mode m)
    {
        mCurrentMode = m;
        isSelected.clear();
        notifyDataSetChanged();
    }
    
    public Mode getMode()
    {
        return mCurrentMode;
    }
    
    public ArrayList<ImageInfo> getIsSelected()
    {
        return isSelected;
    }
    
    public void setIsSelected(ArrayList<ImageInfo> isSelected)
    {
        this.isSelected = isSelected;
    }
    
    public void setSelected(int position, View view)
    {
        if (getSelected(position))
        {
            isSelected.remove(list.get(position));
        }
        else
        {
            isSelected.add(list.get(position));
        }
        notifyDataSetChanged();
        if (selectedChangeListener != null)
            selectedChangeListener.onSelectedChange(isSelected.size());
    }
    
    public boolean getSelected(int position)
    {
        return isSelected.contains(list.get(position));
    }
    
    public interface ISelectedChangeListener
    {
        void onSelectedChange(int count);
    }
    
    private ISelectedChangeListener selectedChangeListener;
    
    public void setOnSelectedChangeListener(ISelectedChangeListener l)
    {
        selectedChangeListener = l;
    }
    
}
