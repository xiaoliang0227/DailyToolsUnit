package com.zyl.tools.dailytoolsunit.interf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IImageTools {

    /**
     * 制作圆角图片
     *
     * @param bitmap
     * @param roundPixels
     * @return
     */
    Bitmap getRoundCornorImage(Bitmap bitmap, int roundPixels);

    /**
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     *
     * @param path the path of video file
     * @param kind     could be MINI_KIND or MICRO_KIND
     */
    Bitmap createVideoThumbnail(String path, int kind);

    /**
     * Creates a centered bitmap of the desired size.
     *
     * @param source original bitmap source
     * @param width  targeted width
     * @param height targeted height
     */
    Bitmap extractThumbnail(Bitmap source, int width, int height);

    /**
     * Creates a centered bitmap of the desired size.
     *
     * @param source  original bitmap source
     * @param width   targeted width
     * @param height  targeted height
     * @param options options used during thumbnail extraction
     */
    Bitmap extractThumbnail(Bitmap source, int width, int height, int options);

    /**
     * 怀旧效果
     *
     * @param bmp
     * @return
     */
    Bitmap olderBitmap(Bitmap bmp);

    /**
     * 模糊效果
     *
     * @param bmp
     * @return
     */
    Bitmap blurImage(Bitmap bmp);

    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     *
     * @param bmp
     * @return
     */
    Bitmap blurImageAmeliorate(Bitmap bmp);

    /**
     * 图片锐化（拉普拉斯变换）
     *
     * @param bmp
     * @return
     */
    Bitmap sharpenImageAmeliorate(Bitmap bmp);

    /**
     * 图片效果叠加
     *
     * @param bmp 限制了尺寸大小的Bitmap
     * @return
     */
    Bitmap overlay(Bitmap bmp, Bitmap overlay);

    /**
     * 光晕效果
     *
     * @param bmp
     * @param x   光晕中心点在bmp中的x坐标
     * @param y   光晕中心点在bmp中的y坐标
     * @param r   光晕的半径
     * @return
     */
    Bitmap halo(Bitmap bmp, int x, int y, float r);

    /**
     * 放大缩小图片
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    Bitmap zoomBitmap(Bitmap bitmap, int w, int h);

    /**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    Bitmap drawableToBitmap(Drawable drawable);

    /**
     * 获得圆角图片的方法
     * @param bitmap
     * @param roundPx
     * @return
     */
    Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx);

    /**
     * 获得带倒影的图片方法
     * @param bitmap
     * @return
     */
    Bitmap createReflectionImageWithOrigin(Bitmap bitmap);

    /**
     * 灰阶处理
     * @param bmpOriginal
     * @return
     */
    Bitmap toGrayscale(Bitmap bmpOriginal);

    /**
     * 黑白处理
     * @param mBitmap
     * @return
     */
    Bitmap toHeibai(Bitmap mBitmap);

    /**
     * 浮雕效果
     * @param mBitmap
     * @return
     */
    Bitmap toFuDiao(Bitmap mBitmap);

    /**
     * 油画处理
     * @param bmpSource
     * @return
     */
    Bitmap toYouHua(Bitmap bmpSource);

    /**
     * 模糊处理
     * @param bmpSource
     * @param Blur
     * @return
     */
    Bitmap toMohu(Bitmap bmpSource, int Blur);

    /**
     * 做旧处理
     * @param bitmap
     * @return
     */
    Bitmap toOld(Bitmap bitmap);

    /**
     * 从path中获取图片信息
     * @param path
     * @param width
     * @param heigth
     * @return
     */
    Bitmap decodeBitmap(String path, int width, int heigth);

    /**
     * 从path中获取图片信息
     * @param path
     * @return
     */
    BitmapFactory.Options decodeBitmap(String path);

    /**
     * @Description 获取专辑封面
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     */
    Bitmap createAlbumArt(final String filePath);

    int calculateInSampleSize(BitmapFactory.Options op, int reqWidth, int reqheight);
}
