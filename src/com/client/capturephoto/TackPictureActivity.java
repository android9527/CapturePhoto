package com.client.capturephoto;

import static com.client.capturephoto.MainActivity.imageInfos;
import static com.client.capturephoto.util.Constant.ARRAY_LIMIT;
import static com.client.capturephoto.util.Constant.DEBUG;
import static com.client.capturephoto.util.Constant.DEFAULT_LIMIT_POSITION;
import static com.client.capturephoto.util.Constant.LIMIT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.client.capturephoto.adapter.ImageInfo;
import com.client.capturephoto.camera.CameraDisabledException;
import com.client.capturephoto.camera.CameraHardwareException;
import com.client.capturephoto.camera.CameraHolder;
import com.client.capturephoto.camera.CameraSettings;
import com.client.capturephoto.camera.CameraUtil;
import com.client.capturephoto.util.Constant;
import com.client.capturephoto.util.SPUtil;
import com.client.capturephoto.util.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.CameraProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

public class TackPictureActivity extends BaseActivity implements
        SurfaceHolder.Callback, View.OnTouchListener
{
    
    private int mState = STATE_IDLE;
    private static final int STATE_IDLE = 0; // Focus is not active.
    private static final int STATE_FOCUSING = 1; // Focus is in progress.
    // Focus is in progress and the camera should take a picture after focus finishes.
    private static final int STATE_SUCCESS = 3; // Focus finishes and succeeds.
    private static final int STATE_FAIL = 4; // Focus finishes and fails.
    
    private String TAG = "TackPictureActivity";// ??
    
    private Button btnCapture, buttonBack;
    
    private SurfaceHolder mSurfaceHolder;
    
    private SurfaceView mSurfaceView;
    
    private SPUtil spUtil;
    
    private String currentCode;
    
    private static final String END = ".jpg";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
        initView();
        
//        DratwCaptureRect mDraw = new DrawCaptureRect(this, 190,10,100,100,Color.GREEN);
//      //��һ��activity������Ӷ����content
//        addConentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        spUtil = SPUtil.getInstance(TackPictureActivity.this);
        currentCode = getIntent().getStringExtra(Constant.TEXT_CODE);
    }
    
    @Override
    protected void onPause()
    {
        resetScreenOn();
        super.onPause();
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    @Override
    protected void onResume()
    {
        keepScreenOnAwhile();
        super.onResume();
    }
    
    private void stopPreview()
    {
        if (mCameraDevice != null)
        {
            Log.v(TAG, "stopPreview");
            mCameraDevice.cancelAutoFocus(); // Reset the focus.
            mCameraDevice.stopPreview();
        }
    }
    
    private void takeAPicture()
    {
        if (mCameraDevice != null)
        {
            /* ����takePicture()�������� */
            mCameraDevice.takePicture(null, null, jpegCallback);// ����PictureCallback
        }
    }
    
    private final PictureCallback jpegCallback = new PictureCallback()
    {
        public void onPictureTaken(byte[] _data, Camera _camera)
        {
            
            // ���SDCard�Ƿ����
            if (!Util.isHasSdcard())
            {
                btnCapture.setEnabled(true);
                showToast(R.string.toast_nosdcard);
                return;
            }
            FileOutputStream fileOutputStream = null;
            try
            {
                File myCaptureFile = new File(Util.SDCARD_ROOT_PATH
                        + File.separator + getString(R.string.app_name)
                        + File.separator + currentCode + File.separator);
                if (!myCaptureFile.exists())
                {
                    myCaptureFile.mkdirs();
                }
                
                myCaptureFile = new File(myCaptureFile.getAbsolutePath()
                        + File.separator + currentCode + "-"
                        + Constant.simpleDateFormat.format(System.currentTimeMillis())
                        + END);
                fileOutputStream = new FileOutputStream(myCaptureFile);
                fileOutputStream.write(_data);
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.fullName = myCaptureFile.getAbsolutePath();
                imageInfo.imageCode = myCaptureFile.getParent();
                imageInfos.add(imageInfo);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                showToast(R.string.message_save_error);
            }
            finally
            {
                try
                {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            btnCapture.setEnabled(true);
            startPreview();
            //                BufferedOutputStream bos = null;
            //                 try
            //                 {
            //                 bos = new BufferedOutputStream(new FileOutputStream(
            //                 myCaptureFile));
            //                 bitmap.compress(CompressFormat.JPEG, 80, bos);
            //                
            //                 Matrix matrix = new Matrix();
            //                 matrix.setRotate(mDisplayOrientation);
            //                 bitmap = Bitmap
            //                 .createBitmap(bitmap, 0, 0, bitmap.getWidth(),
            //                 bitmap.getHeight(), matrix, true);
            //                 bos.close();
            //                 bitmap.recycle();
            //                 }
            //                 catch (FileNotFoundException e)
            //                 {
            //                 e.printStackTrace();
            //                 }
            //                 catch (IOException e)
            //                 {
            //                 e.printStackTrace();
            //                 }
            //            }
        }
    };
    
    @SuppressWarnings("deprecation")
	private void initView()
    {
        buttonBack = (Button) findViewById(R.id.button_back);
        btnCapture = (Button) findViewById(R.id.takepicture);
        /* ��SurfaceView��Ϊ���Preview֮�� */
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.setOnTouchListener(this);
        btnCapture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (checkToTakePicture())
//                {
                    btnCapture.setEnabled(false);
                    takeAPicture();
//                }
            }
        });
        
        buttonBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(RESULT_OK);
                TackPictureActivity.this.finish();
            }
        });
    }
    
    private boolean checkToTakePicture()
    {
        int limitPosition = spUtil.getInt(LIMIT, DEFAULT_LIMIT_POSITION);
        if (imageInfos.size() >= Integer.parseInt(ARRAY_LIMIT[limitPosition]))
          {
              showToast(R.string.message_limit);
              return false;
          }
          return true;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        openCamera();
        setParameters(holder);
        startPreview();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height)
    {
        // Make sure we have a surface in the holder before proceeding.
        if (holder.getSurface() == null)
        {
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }
        
        Log.v(TAG, "surfaceChanged. w=" + width + ". h=" + height);
        
        // We need to save the holder for later use, even when the mCameraDevice
        // is null. This could happen if onResume() is invoked after this
        // function.
        mSurfaceHolder = holder;
        
        // The mCameraDevice will be null if it fails to connect to the camera
        // hardware. In this case we will show a dialog and then finish the
        // activity, so it's OK to ignore it.
        if (mCameraDevice == null)
            return;
        
        // Set preview display if the surface is being created. Preview was
        // already started. Also restart the preview if display rotation has
        // changed. Sometimes this happens when the device is held in portrait
        // and camera app is opened. Rotation animation takes some time and
        // display rotation in onCreate may not be what we want.
        if (CameraUtil.getDisplayRotation(this) != mDisplayRotation)
        {
            setDisplayOrientation();
        }
        startPreview();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        stopPreview();
        closeCamera();
        mSurfaceHolder = null;
    }
    
    private Camera mCameraDevice;
    
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();
    
    private class CameraErrorCallback implements
            android.hardware.Camera.ErrorCallback
    {
        private static final String TAG = "CameraErrorCallback";
        
        public void onError(int error, android.hardware.Camera camera)
        {
            Log.e(TAG, "Got camera error callback. error=" + error);
            if (error == android.hardware.Camera.CAMERA_ERROR_SERVER_DIED)
            {
                // We are not sure about the current state of the app (in preview or
                // snapshot or recording). Closing the app is better than creating a
                // new Camera object.
                //                        throw new RuntimeException("Media server died.");
                showToast(R.string.cannot_connect_camera);
            }
        }
    }
    
    private final AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();
    
    private Camera.Parameters mParameters;
    
    
    /**
     * ����Camera ����
     * @param mSurfaceHolder
     */
    private void setParameters(SurfaceHolder mSurfaceHolder)
    {
        mCameraDevice.setErrorCallback(mErrorCallback);
        
        // If we're previewing already, stop the preview first (this will blank
        // the screen).
        setPreviewDisplay(mSurfaceHolder);
        setDisplayOrientation();
        
        setCameraParameters();
        // If the focus mode is continuous autofocus, call cancelAutoFocus to
        // resume it because it may have been paused by autoFocus call.
        //        if (Parameters.FOCUS_MODE_CONTINUOUS_PICTURE.equals(mParameters.getFocusMode()))
        //        {
        //            mCameraDevice.cancelAutoFocus();
        //        }
        
    }
    
    
    /**
     * ��ʼԤ��
     */
    private void startPreview()
    {
        try
        {
            Log.v(TAG, "startPreview");
            mCameraDevice.startPreview();
            if (isSupportAutoFocus)
            {
                mCameraDevice.autoFocus(mAutoFocusCallback);
            }
        }
        catch (Throwable ex)
        {
            closeCamera();
            throw new RuntimeException("startPreview failed", ex);
        }
    }
    
    /**
     * �Զ��۽�
     * @author Administrator
     *
     */
    private final class AutoFocusCallback implements
            android.hardware.Camera.AutoFocusCallback
    {
        public void onAutoFocus(boolean focused, android.hardware.Camera camera)
        {
            if (focused){
                mState = STATE_SUCCESS;
            }
            else{
                mState = STATE_FAIL;
                mCameraDevice.autoFocus(mAutoFocusCallback);
            }
        }
    }
    
    private void setCameraParameters()
    {
        mParameters = mCameraDevice.getParameters();
        updateCameraParametersPreference();
        
        mCameraDevice.setParameters(mParameters);
    }

    private boolean isSupportAutoFocus;
    private void updateCameraParametersPreference()
    {
        mParameters.setPictureFormat(ImageFormat.JPEG);
        mParameters.setRotation(mDisplayOrientation);
        
        List<String> focusModes = mParameters.getSupportedFocusModes();
        /**
         * �Ƿ�֧���Զ��۽�
         */
        if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            isSupportAutoFocus = true;
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        //                mParameters.setPictureSize(3264,2448);
        // Set picture size.
        //                String pictureSize = mPreferences.getString(
        //                        CameraSettings.KEY_PICTURE_SIZE, null);
        //                if (pictureSize == null) {
        
        int width = spUtil.getInt(Constant.PICTURE_WIDTH, 0);
        int height = spUtil.getInt(Constant.PICTURE_HEIGHT, 0);
        if (width == 0 || height == 0)
        {
            CameraSettings.initialCameraPictureSize(this, mParameters);
            Size size = mParameters.getPictureSize();
            spUtil.save(Constant.PICTURE_WIDTH,
                    Math.min(size.width, size.height));
            spUtil.save(Constant.PICTURE_HEIGHT,
                    Math.max(size.width, size.height));
        }
        else
        {
            mParameters.setPictureSize(Math.max(width, height),
                    Math.min(width, height));
        }
        //                } else {
        //                    List<Size> supported = mParameters.getSupportedPictureSizes();
        //                    setCameraPictureSize(
        //                            pictureSize, supported, mParameters);
        //                }
        
        //        // Set the preview frame aspect ratio according to the picture size.
        Size size = mParameters.getPictureSize();
        
        Log.e(TAG, "size.width = " + size.width + "  size.height = "
                + size.height);
//        //        // Set a preview size that is closest to the viewfinder height and has
//        //        // the right aspect ratio.
//                List<Size> sizes = mParameters.getSupportedPreviewSizes();
//                Size optimalSize = CameraUtil.getOptimalPreviewSize(this,
//                        sizes, (double) size.width / size.height);
//                Size original = mParameters.getPreviewSize();
//                if (!original.equals(optimalSize)) {
//                    mParameters.setPreviewSize(optimalSize.width, optimalSize.height);
//        
//                    // Zoom related settings will be changed for different preview
//                    // sizes, so set and read the parameters to get lastest values
//                    mCameraDevice.setParameters(mParameters);
//                    mParameters = mCameraDevice.getParameters();
//                }
//                Log.v(TAG, "Preview size is " + optimalSize.width + "x" + optimalSize.height);
        //
        //
        //        // Set JPEG quality.
        int jpegQuality = CameraProfile.getJpegEncodingQualityParameter(CameraProfile.QUALITY_HIGH);
        mParameters.setJpegQuality(jpegQuality);
    }
    
    // The display rotation in degrees. This is only valid when mCameraState is
    // not PREVIEW_STOPPED.
    private int mDisplayRotation;
    
    // The value for android.hardware.Camera.setDisplayOrientation.
    private int mDisplayOrientation = 90;
    
    private void setDisplayOrientation()
    {
        mDisplayRotation = CameraUtil.getDisplayRotation(this);
//        mDisplayOrientation = CameraUtil.getDisplayOrientation(mDisplayRotation);
        mCameraDevice.setDisplayOrientation(mDisplayOrientation);
    }
    
    private void setPreviewDisplay(SurfaceHolder holder)
    {
        try
        {
            mCameraDevice.setPreviewDisplay(holder);
        }
        catch (Throwable ex)
        {
            closeCamera();
            throw new RuntimeException("setPreviewDisplay failed", ex);
        }
    }

    /**
     * ��Ļ����
     */
    private void keepScreenOnAwhile()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    
    private void resetScreenOn()
    {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    /**
     * �ͷ����
     * Close the camera now because other activities may need to use it.
     */
    private void closeCamera()
    {
        if (mCameraDevice != null)
        {
            CameraHolder.instance().release();
            //            mCameraDevice.setZoomChangeListener(null);
            //            mCameraDevice.setFaceDetectionListener(null);
            //            mCameraDevice.setErrorCallback(null);
            mCameraDevice = null;
        }
    }
    
    /**
     * ������ͷ
     */
    private void openCamera()
    {
        // Start the preview if it is not started.
        try
        {
            mCameraDevice = CameraUtil.openCamera(this);
        }
        catch (CameraHardwareException e)
        {
            CameraUtil.showErrorAndFinish(this, R.string.cannot_connect_camera);
            return;
        }
        catch (CameraDisabledException e)
        {
            CameraUtil.showErrorAndFinish(this, R.string.camera_disabled);
            return;
        }
        catch (Exception e)
        {
            CameraUtil.showErrorAndFinish(this, R.string.cannot_connect_camera);
            return;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                setResult(RESULT_OK);
                TackPictureActivity.this.finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    class DrawCaptureRect extends View
    {
        private int mcolorfill;
        
        private int mleft, mtop, mwidth, mheight;
        
        public DrawCaptureRect(Context context, int left, int top, int width,
                int height, int colorfill)
        {
            super(context);
            // TODO Auto-generated constructor stub
            this.mcolorfill = colorfill;
            this.mleft = left;
            this.mtop = top;
            this.mwidth = width;
            this.mheight = height;
        }
        
        @Override
        protected void onDraw(Canvas canvas)
        {
            // TODO Auto-generated method stub
            Paint mpaint = new Paint();
            mpaint.setColor(mcolorfill);
            mpaint.setStyle(Paint.Style.FILL);
            mpaint.setStrokeWidth(1.0f);
            canvas.drawLine(mleft, mtop, mleft + mwidth, mtop, mpaint);
            canvas.drawLine(mleft + mwidth, mtop, mleft + mwidth, mtop
                    + mheight, mpaint);
            canvas.drawLine(mleft, mtop, mleft, mtop + mheight, mpaint);
            canvas.drawLine(mleft, mtop + mheight, mleft + mwidth, mtop
                    + mheight, mpaint);
            super.onDraw(canvas);
        }
        
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(!isSupportAutoFocus){
            return false;
        }
        
        if(mState == STATE_FOCUSING ||
                mState == STATE_SUCCESS || mState == STATE_FAIL)
            mCameraDevice.cancelAutoFocus();
        mState = STATE_FOCUSING;
        mCameraDevice.autoFocus(mAutoFocusCallback);
        return false;
    }
}
