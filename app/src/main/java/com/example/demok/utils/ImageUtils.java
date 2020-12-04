package com.example.demok.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;

import com.example.baselib.utils.LogUtils;
import com.example.demok.base.MyApplication;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/1/23.
 */

public class ImageUtils {

    //加载本地图片
    public static void loadDrawable(SimpleDraweeView draweeView, int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);

       }

    public static void loadUri(SimpleDraweeView draweeView, Uri uri) {
//        draweeView.setImageURI(uri);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    public static void loadUri(SimpleDraweeView draweeView, String uri) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
//        draweeView.setImageURI(uri);
    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载图片成bitmap。
     *
     * @param imageUrl 图片地址。
     */
    public static void loadToBitmap(String imageUrl, BaseBitmapDataSubscriber mDataSubscriber, Context context) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUrl))
                .setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage
                (imageRequest, context);
        dataSource.subscribe(mDataSubscriber, CallerThreadExecutor.getInstance());
    }


    /**
     * 转换为bitmap
     *
     * @param activity
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getUriforBitmap(Context context, Bitmap bitmap) {
        try {
            // 读取uri所在的图片
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
            return uri;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 拷贝文件，将uri拷贝到 App专属目录下
     * @param uri 要拷贝文件的Uri
     * @param saveName 保存到专属目录下的文件名
     * @return 拷贝后新的文件
     * https://blog.csdn.net/y331271939/article/details/100899980
     */
    public static File getFile(Uri uri, String saveName) {
        File rootFile = MyApplication.mContext.getExternalFilesDir(null);
        File file = new File(rootFile, saveName);

        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = MyApplication.mContext.getContentResolver().openInputStream(uri);
            while (true) {
                int numRead = inputStream.read(buffer);
                if (numRead == -1) {
                    break;
                }
                fileOutputStream.write(buffer, 0, numRead);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            file = null;
            e.printStackTrace();
        }

        return file;
    }


    /**
     * 获取视频的第一帧
     *
     * @param path
     * @return
     */
    public static Bitmap getVideoThumb(String path) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(path);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取视频缩略图
     * https://blog.csdn.net/le920309/article/details/103134203/
     * @param uri
     * @param path 视频路径
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static Bitmap getVideoThumbnail(Context context, Uri uri, String path, int width, int height) throws IOException {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if (Build.VERSION.SDK_INT >= 29) {
            bitmap = context.getContentResolver().loadThumbnail(uri, new Size(width, height), null);
        }
        return bitmap;
    }

    /**
     * 获取视频的旋转角度
     *
     * @param path
     * @return true 竖屏
     */
    public static boolean getVideoWidthOrHeight(String path) {
//        float videoWidth = 0;
//        float videoHeight = 0;
        boolean isShu = false;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(path);
//            videoWidth = Float.parseFloat(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//            videoHeight = Float.parseFloat(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
//            LogUtils.i("视频旋转角度" + rotation);
            if (rotation.equals("90") || rotation.equals("270")) { //竖屏
                isShu = true;
            } else {
                isShu = false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return isShu;
    }

    /**
     * 获取网络视频第一帧
     *
     * @param videoUrl
     * @return
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 从网络下载图片
     * 1、根据提供的图片URL，获取图片数据流
     * 2、将得到的数据流写入指定路径的本地文件
     *
     * @param url            URL
     * @param loadFileResult LoadFileResult
     */
//    public static void downloadImage(Activity activity, String url, final IDownloadResult loadFileResult) {
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//
//        Uri uri = Uri.parse(url);
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
//        ImageRequest imageRequest = builder.build();
//
//        // 获取未解码的图片数据
//        DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, activity);
//        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
//            @Override
//            public void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
//                if (!dataSource.isFinished() || loadFileResult == null) {
//                    return;
//                }
//
//                CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
//                if (imageReference != null) {
//                    final CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
//                    try {
//                        PooledByteBuffer pooledByteBuffer = closeableReference.get();
//                        InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);
//                        String photoPath = loadFileResult.getFilePath();
//                        byte[] data = StreamTool.read(inputStream);
//                        StreamTool.write(photoPath, data);
//                        activity.runOnUiThread(() -> {
//                            loadFileResult.onResult(photoPath);
//                        });
//                    } catch (IOException e) {
//                        activity.runOnUiThread(() -> {
//                            loadFileResult.onResult(null);
//                        });
//                        e.printStackTrace();
//                    } finally {
//                        imageReference.close();
//                        closeableReference.close();
//                    }
//                }
//            }
//
//            @Override
//            public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
//                int progress = (int) (dataSource.getProgress() * 100);
//                if (loadFileResult != null) {
//                    loadFileResult.onProgress(progress);
//                }
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//                if (loadFileResult != null) {
//                    loadFileResult.onResult(null);
//                }
//
//                Throwable throwable = dataSource.getFailureCause();
//                if (throwable != null) {
//                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
//                }
//            }
//        }, Executors.newSingleThreadExecutor());
//    }
//
//    public static List<ImageModel> queryImagesFromExternal(ContextWrapper contextWrapper) {
//        List<ImageModel> imageModelList = new ArrayList<>();
//        Cursor cursor = contextWrapper.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
//        if (cursor != null) {
//           Gson gson = new Gson();
//            while (cursor.moveToNext()) {
//                try {
//                    JSONObject json = new JSONObject();
//                    String[] columnNames = cursor.getColumnNames();
//                    for (String columnName : columnNames) {
//                        String s = cursor.getString(cursor.getColumnIndex(columnName));
//                        json.put(columnName, s);
//                    }
//                    ImageModel imageModel = gson.fromJson(json.toString(), ImageModel.class);
//                    imageModelList.add(imageModel);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            cursor.close();
//        }
//        return imageModelList;
//    }

}
