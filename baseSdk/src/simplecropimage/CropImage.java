package simplecropimage;

import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Handler;
import android.util.Log;


/**
 * 裁剪处理
 *
 */
public class CropImage
{
	public boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    public boolean mSaving; // Whether the "save" button is already clicked.
    public HighlightView mCrop;
    
	private Context mContext;
	private Handler mHandler = new Handler();
	private CropImageView mImageView;
	private Bitmap mBitmap;
	
	public CropImage(Context context, CropImageView imageView)
	{
		mContext = context;
		mImageView = imageView;
		mImageView.setCropImage(this);
	}
	
	/**
	 * 图片裁剪
	 */
	public void crop(Bitmap bm)
	{
		mBitmap = bm;
		startFaceDetection();
	}
	
	private void startFaceDetection() {
        if (((Activity)mContext).isFinishing()) {
            return;
        }

        showProgressDialog("请稍等……", new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        try {
							if (b != mBitmap && b != null) {
							    mImageView.setImageBitmapResetBase(b, true);
							    mBitmap.recycle();
							    mBitmap = b;
							}
							if (mImageView.getScale() == 1.0f) {
							    mImageView.center(true, true);
							}
							latch.countDown();
						} catch (Exception e) {
//							errorHandler.sendEmptyMessageDelayed(0, 1000);
		        			Log.e("ObtainPicturesCall", e.getMessage(), e);
						}
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
//					errorHandler.sendEmptyMessageDelayed(0, 1000);
        			Log.e("ObtainPicturesCall", e.getMessage(), e);
                }
                mRunFaceDetection.run();
            }
        }, mHandler);
    }

	/**
	 * 裁剪并保存
	 * @return
	 */
	public Bitmap cropAndSave(Bitmap bm)
	{
		final Bitmap bmp = onSaveClicked(bm);
		mImageView.mHighlightViews.clear();
		return bmp;
	}
	
	/**
	 * 取消裁剪
	 */
	public void cropCancel()
	{
		mImageView.mHighlightViews.clear();
		mImageView.invalidate();
	}
	
    private Bitmap onSaveClicked(Bitmap bm) {
        // CR: TODO!
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)  
            return bm;

        if (mCrop == null) {
            return bm;
        }

        mSaving = true;

        Rect r = mCrop.getCropRect();

        int width = r.width(); // CR: final == happy panda!
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(bm, r, dstRect, null);
        }
        return croppedImage;
    }

    private void showProgressDialog(String msg, Runnable job, Handler handler)
    {
    	final ProgressDialog progress = ProgressDialog.show(mContext, null, msg);
    	new Thread(new BackgroundJob(progress, job, handler)).start();
    }
    
	public void clear() {
		if (mImageView != null)
			mImageView.clear();
		mContext = null;
	}
	
    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, false, false);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() throws Exception{
        	if(mBitmap == null)
        			throw new Exception();
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // CR: sentences!
            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, false, false);
            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth(); // CR: F => f (or change
                                                      // all f to F).
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    try {
						mWaitingToPick = mNumFaces > 1;
						if (mNumFaces > 0) {
						    for (int i = 0; i < mNumFaces; i++) {
						        handleFace(mFaces[i]);
						    }
						} else {
						    makeDefault();
						}
						mImageView.invalidate();
						if (mImageView.mHighlightViews.size() == 1) {
						    mCrop = mImageView.mHighlightViews.get(0);
						    mCrop.setFocus(true);
						}
					} catch (Exception e) {
//						errorHandler.sendEmptyMessageDelayed(0, 1000);
	        			Log.e("ObtainPicturesCall", e.getMessage(), e);
					}
                }
            });
        }
    };
	
	class BackgroundJob implements Runnable
    {
    	private ProgressDialog mProgress;
    	private Runnable mJob;
    	private Handler mHandler;
    	public BackgroundJob(ProgressDialog progress, Runnable job, Handler handler)
    	{
    		mProgress = progress;
    		mJob = job;
    		mHandler = handler;
    	}
    	
    	public void run()
    	{
    		try 
    		{
    			mJob.run();
    		}
    		finally
    		{
    			mHandler.post(new Runnable()
    			{
    				public void run()
    				{
    					if (mProgress != null && mProgress.isShowing())
    					{
    						mProgress.dismiss();
    						mProgress = null;
    					}
    				}
    			});
    		}
    	}
    }
	
//	private Handler errorHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			Activity activity = ((Activity)mContext);
//			YmToastUtils.showToast(mContext,"图片格式错误，请重新操作！");
//			activity.setResult(ObtainPicturesCall.DO_AGAIN);
//			activity.finish();
//		}
//	};
}