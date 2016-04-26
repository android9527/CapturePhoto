
package com.client.capturephoto.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 计算公式 pixels = dips * (density / 160)
 * 
 * @version 1.0.1 2010-12-11
 * @author
 */
public class DensityUtil {

    // 当前屏幕的densityDpi
    private float dmDensityDpi = 0.0f;
    private float scale = 0.0f;
    
    private int width, height;

    /**
     * 根据构造函数获得当前手机的屏幕系数
     */
    public DensityUtil(Context context) {
        // 获取当前屏幕
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        // 设置DensityDpi
        setDmDensityDpi(dm.densityDpi);
        // 密度因子
        scale = dm.densityDpi / 160;
        height = dm.heightPixels;
        width = dm.widthPixels;
    }
    
    public int getScreenWidth(){
        return width;
    }
    
    public int getScreenHeight(){
        return height;
    }

    /**
     * 当前屏幕的density因子
     * 
     * @param DmDensity
     * @retrun DmDensity Getter
     */
    public float getDmDensityDpi() {
        return dmDensityDpi;
    }

    /**
     * 当前屏幕的density因子
     * 
     * @param DmDensity
     * @retrun DmDensity Setter
     */
    public void setDmDensityDpi(float dmDensityDpi) {
        this.dmDensityDpi = dmDensityDpi;
    }

    /**
     * 密度转换像素
     */
    public int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 像素转换密度
     */
    public int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public String toString() {
        return " dmDensityDpi:" + dmDensityDpi;
    }
    
    
    /**
     * 获取图片的宽
     * @param number 每行显示数量
     * @param horSpacing 水平间隔
     * @param gridSpacing gridview间隔
     * @return
     * 
     *   | |imageview | |          | |          | |
     *   | |          | |          | |          | |
     *   | |          | |          | |          | |
     *   | |          | |          | |          | |
     *   | |          | |          | |          | |
     */
    public int getImageViewWidth(int number, int horSpacing, int gridSpacing)
    {
        int imageWidth = (width - 2 * horSpacing - (number - 1) * gridSpacing)
                / number;
        return imageWidth;
    }
    public int getImageViewHeight(int imageWidth)
    {
        return imageWidth * 4 / 3;
    }
}
