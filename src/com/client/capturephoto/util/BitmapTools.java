package com.client.capturephoto.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class BitmapTools {

	private static final String TAG = "BitmapTools";

	public Context context;

	public BitmapTools(Context context) {
		this.context = context;
	}
    
    public static Bitmap getimage(String srcPath, int width, int height)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空  
        
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > width)
        {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / width);
        }
        else if (w < h && h > height)
        {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        return BitmapFactory.decodeFile(srcPath, newOpts);
        //压缩好比例大小后再进行质量压缩  
    } 
	
	private Bitmap compressImage(Bitmap image) {  
	    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	/**
	 * 设置图片缩放
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getBitmapByOptions(Context context, String imagePath) {
		Bitmap bitmap = null;
		try {
			int maxH = 400;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			bitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回bm为空
			// 计算缩放比
			int be = (int) (options.outHeight / (float) maxH);
			int ys = options.outHeight % maxH;// 求余数
			float fe = ys / (float) maxH;
			if (fe >= 0.5)
				be = be + 1;
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;

			// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
			options.inJustDecodeBounds = false;

			bitmap = BitmapFactory.decodeFile(imagePath, options);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 另一种动态计算缩放比得到Bitmap
	 * 
	 * @param context
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getBitmapBySampleSize(Context context, String imagePath, int maxNumOfPixels) {

		if (imagePath.equals("")) {
			return null;
		}
		Bitmap bitmap = null;

		// 解码bitmap,处理OutOfMemoryError
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		
//		opts.inSampleSize = computeSampleSize(opts, -1, 1920 * 2560 / 4);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(imagePath, opts); // 解码bitmap
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}

		return bitmap;

	}

	/**
	 * 根据Uri获取Bitmap 如果图片过大或者 解码多张图片会造成OutOfMemoryError
	 * 
	 * @param data
	 * @return
	 */
	public Bitmap getBitmapByUri(Context context, Intent data) {
		// 直接用Uri解码图片会造成OutOfMemoryError
		Bitmap bitmap = null;
		Uri uri = data.getData();
		ContentResolver cr = context.getContentResolver();

		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();

		// 根据Uri获取文件名
		String imgName = cursor.getString(cursor
				.getColumnIndex("_display_name")); // 图片文件名

		// 图片文件
		if (imgName != null
				&& (imgName.endsWith(".jpg") || imgName.endsWith(".jpeg")
						|| imgName.endsWith(".png") || imgName.endsWith(".gif"))) {

			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				cr.openInputStream(uri).close();
			} catch (Exception e) {
			    e.printStackTrace();
			} catch (OutOfMemoryError err) {
				err.printStackTrace();
			}
		} else {
		}
		return bitmap;
	}

	/**
	 * 根据Uri获得图片路径
	 */
	public String getImagePath(Context context, Intent data) {
		Uri selectedImage = data.getData();
		String path = "";
		Cursor cursor = null;
		try {
			if (selectedImage.toString().startsWith("content://")) {
				String imagePath[] = { MediaStore.Images.Media.DATA };
				cursor = context.getContentResolver().query(selectedImage,
						imagePath, null, null, null);
				cursor.moveToFirst();
				path = cursor.getString(cursor.getColumnIndex(imagePath[0]));
			} else if (selectedImage.toString().startsWith("file:///")) {
				path = selectedImage.toString().substring(
						"file:///".length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;

	}

	/**
	 * 检查存储卡是否挂载
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * 图片资源缩放到合适大小
     */
    public static Bitmap getResizedBitmap(String path, int newWidth, int newHeight ) {

        
        Bitmap bm = BitmapFactory.decodeFile(path);
        // 获得当前图片的宽度和高度
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 计算缩放率,新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        // 得到缩放后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, true);
        bm.recycle();
        bm = null;
        return resizedBitmap;
    }

	/**
	 * 图片资源缩放到合适大小
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		// 获得当前图片的宽度和高度
		int width = bm.getWidth();
		int height = bm.getHeight();

		// 计算缩放率,新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 得到缩放后的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, true);
		bm.recycle();
		return resizedBitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {

			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 显示大图对话框
	 * 
	 * @param res
	 */
//	public static void showImg(Bitmap res, Activity a) {
//
//		LayoutInflater inflater = LayoutInflater.from(a);
//		final View view = inflater.inflate(R.layout.big_image_dialog, null);
//
//		ImageView closeBtn = (ImageView) view.findViewById(R.id.close_btn);
//		ImageView ll_viewArea = (ImageView) view.findViewById(R.id.ll_viewArea);
//
//		ll_viewArea.setImageBitmap(res);
//		AlertDialog.Builder b = new AlertDialog.Builder(a);
//		b.setView(view);
//		final AlertDialog dialog = b.create();
//		dialog.show();
//
//		closeBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				dialog.dismiss();
//			}
//		});
//	}

	/**
	 * 回收bitmap
	 * 
	 * @param bitmap
	 */
	public static void destoryBitmap(Bitmap bitmap) {

		if (null != bitmap && !bitmap.isRecycled()) {
			bitmap.recycle();
			System.err.println("destory bitmap success!");
			bitmap = null;
		}
//		System.gc();

	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
//	private static String ALBUM_PATH = Environment
//			.getExternalStorageDirectory() + BaseString.ROOT_FOLDER;
//
//	public static void saveFile(Bitmap bm, String fileName) throws Exception {
//
//		/**
//		 * 创建目录，多级目录要逐一创建
//		 */
//		File dirFile = new File(ALBUM_PATH + "cache/images/");
//		if (!dirFile.exists()) {
//			dirFile.mkdirs();
//		}
//		File myCaptureFile = new File(ALBUM_PATH + "cache/images/" + fileName);
//
//		BufferedOutputStream bos = new BufferedOutputStream(
//				new FileOutputStream(myCaptureFile));
//		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//		bos.flush();
//		bos.close();
//	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
//	public static Bitmap getCacheBitmap(String fileName) {
//
//		File imageFile = new File(ALBUM_PATH + "cache/images/", fileName);
//
//		Bitmap bitmap = null;
//
//		if (imageFile.exists()) {
//			try {
//				bitmap = BitmapFactory.decodeStream(new FileInputStream(
//						imageFile));
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		return bitmap;
//
//	}

	/**
	 * 保存图片 将bitmap保存至本地文件夹
	 * 
	 * @param bitName
	 * @param mBitmap
	 */
	// Bitmap类有一compress成员，可以把bitmap保存到一个stream中。
	// 例如：
	public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
//		String dirPath = "/sdcard/Note/";
//		File f = new File(dirPath);
//		if (!f.exists())
//			f.mkdirs();

		// f.createNewFile();

//		File photo = new File(dirPath + bitName + ".png");
	    
		BufferedOutputStream fOut = null;
		try {
			fOut = new BufferedOutputStream(new FileOutputStream(new File(bitName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try
        {
            if (fOut != null)
                fOut.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            if (fOut != null)
                fOut.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getImagePathByUri(Context context, Uri uri) {
		String path = "";
		Cursor cursor = null;
		try {
			if (uri.toString().startsWith("content://")) {
				String imagePath[] = { MediaStore.Images.Media.DATA };
				cursor = context.getContentResolver().query(uri, imagePath,
						null, null, null);
				cursor.moveToFirst();
				path = cursor.getString(cursor.getColumnIndex(imagePath[0]));
			} else if (uri.toString().startsWith("file:///")) {
				path = uri.toString().substring("file:///".length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * 调用系统图片裁剪工具
	 */
	public static void cropImageUri(Activity a, Uri uri, /*
														 * int aspectX, int
														 * aspectY, int outputX,
														 * int outputY,
														 */int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", aspectX); // 宽度比例
		// intent.putExtra("aspectY", aspectY); // 高度比例
		// intent.putExtra("outputX", outputX);
		// intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		a.startActivityForResult(intent, requestCode);
	}

	public static void chooseBigImage(Activity a, /*
												 * int aspectX, int aspectY,int
												 * outputX, int outputY,
												 */Uri imageUri, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", aspectX);
		// intent.putExtra("aspectY", aspectY);
		// intent.putExtra("outputX", outputX); //输出宽度
		// intent.putExtra("outputY", outputY); //输出高度
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection
		a.startActivityForResult(intent, requestCode);
	}
}
