package com.nepo.nepotransfered.net;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.nepo.nepotransfered.app.Global;
import com.nepo.nepotransfered.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncImageLoader {
	private ThreadPoolExecutor mPoolExecutor;
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private Handler mMainThreadHandler;

	/**
	 * 创建一个异步图片加载器，默认最大5个工作线程，最大等待队列20
	 */
	public AsyncImageLoader() {
		this(5, 20);
	}

	/**
	 * 创建一个异步图片加载器，当等待下载的图片超过设置的最大等待数量之后，会从等待队列中放弃一个最早加入队列的任务
	 * 
	 * @param maxPoolSize
	 *            最大工作线程数
	 * @param queueSize
	 *            最大等待数
	 */
	public AsyncImageLoader(int maxPoolSize, int queueSize) {
		this(2, maxPoolSize, 3, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(queueSize),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	/**
	 * 自定义线程池的加载器,请参考:{@link ThreadPoolExecutor}
	 * 
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param handler
	 */
	public AsyncImageLoader(int corePoolSize, int maximumPoolSize,
							long keepAliveTime, TimeUnit unit,
							BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		mPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, unit, workQueue, handler);
		mMainThreadHandler = new Handler(Looper.getMainLooper());
	}

	/**
	 * 异步加载一张图片
	 * 
	 * @param imageUrl
	 * @param imageCallback
	 */
	public void loadDrawable(final int viewId, final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				imageCallback.onLoaded(viewId, drawable);
				return;
			}
		}
//		LoadImageTask task = new LoadImageTask(viewId, imageUrl, this,
//				mMainThreadHandler, imageCallback);
//		mPoolExecutor.execute(task);

		try {
			boolean isTask = true;
			// 读取本地
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String fileDir = Environment.getExternalStorageDirectory()
						+ "/" + Global.PHONE_TEMP_DIR;
				File tmpF = new File(fileDir);
				if (!tmpF.exists()) {
					tmpF.mkdirs();
				}
				File f = new File(fileDir + Utils.md5(imageUrl));
				if (f.exists()) {
					FileInputStream fis = new FileInputStream(f);
					Drawable d = Drawable.createFromStream(fis, "src");
					imageCallback.onLoaded(viewId, d);
					isTask=false;
				}
			}
			if (isTask) {
				LoadImageTask task = new LoadImageTask(viewId, imageUrl, this,
						mMainThreadHandler, imageCallback);
				mPoolExecutor.execute(task);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 停止线程池运行，停止之后，将不能在继续调用 {@link #(String, ImageCallback)}
	 */
	public void shutdown() {
		mPoolExecutor.shutdown();
		imageCache.clear();
	}

	private void cache(String url, Drawable drawable) {
		imageCache.put(url, new SoftReference<Drawable>(drawable));
	}

	/**
	 * 下载任务
	 * 
	 */
	private static final class LoadImageTask implements Runnable {

		private Handler mHandler;
		private ImageCallback mCallback;
		private AsyncImageLoader mLoader;
		private String mPath;
		private int viewId;

		/**
		 * @param imgPath
		 *            要下载的图片地址
		 * @param loader
		 *            图片加载器
		 * @param handler
		 *            主线程Handler
		 * @param imageCallback
		 *            图片加载回调
		 */
		public LoadImageTask(int viewId, String imgPath,
				AsyncImageLoader loader, Handler handler,
				ImageCallback imageCallback) {
			this.mHandler = handler;
			this.mPath = imgPath;
			this.mLoader = loader;
			this.mCallback = imageCallback;
			this.viewId = viewId;
		}

		@Override
		public void run() {
			try {
				// 读取本地
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					String fileDir = Environment.getExternalStorageDirectory()
							+ "/" + Global.PHONE_TEMP_DIR;
					File tmpF = new File(fileDir);
					if (!tmpF.exists()) {
						tmpF.mkdirs();
					}
					File f = new File(fileDir + Utils.md5(mPath));
					if (f.exists()) {
						FileInputStream fis = new FileInputStream(f);
						Drawable d = Drawable.createFromStream(fis, "src");
						mLoader.cache(mPath, d );
						mCallback.onLoaded(viewId, d);
						return;
					}
					//读取网络
					URL url;
					InputStream is = null;
					url = new URL(mPath);
					URLConnection conn = url.openConnection();
					conn.connect();
					is = conn.getInputStream();
					FileOutputStream out = new FileOutputStream(f);
					byte[] buffer = new byte[1024];
					int byteread = 0;
					while ((byteread = is.read(buffer)) != -1) {
						out.write(buffer, 0, byteread);
					}
					is.close();
					out.flush();
					out.close();
					final Drawable drawable = Drawable.createFromStream(new FileInputStream(f), "src");
					mLoader.cache(mPath, drawable);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mCallback.onLoaded(viewId, drawable);
						}
					});
					is.close();
					
				}
				// 读取网络
				else {
					URL url;
					InputStream is = null;
					url = new URL(mPath);
					URLConnection conn = url.openConnection();
					conn.connect();
					is = conn.getInputStream();
					final Drawable drawable = Drawable.createFromStream(is,
							"src");
					mLoader.cache(mPath, drawable);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mCallback.onLoaded(viewId, drawable);
						}
					});
					is.close();

				}


			} catch (final Exception e) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						mCallback.onError(viewId, e);
					}
				});
			}
		}
	}

	/**
	 * 回调接口,在主线程中运行
	 * 
	 */
	public static interface ImageCallback {
		/**
		 * 加载成功
		 * 
		 *            下载下来的图片
		 */
		public void onLoaded(int viewId, Drawable drawable);

		/**
		 * 加载失败
		 * 
		 * @param e
		 *            异常
		 */
		public void onError(int viewId, Exception e);
	}
}
