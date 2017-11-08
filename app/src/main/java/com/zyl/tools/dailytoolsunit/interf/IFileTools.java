package com.zyl.tools.dailytoolsunit.interf;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IFileTools {

    /**
     * 获取盘符
     *
     * @return
     */
    HashSet<String> getExternalMounts();

    String formatSambaFileName(String name);

    String getMimeType(File file) throws MalformedURLException;

    String getMimeType(Context context, Uri uri);

    String fileExt(String url);

    /**
     * 打开指定文件
     *
     * @param context
     * @param file
     */
    void openFile(Context context, File file);

    /**
     * 删除文件
     *
     * @param file
     */
    void deleteFile(File file);

    boolean checkSdcardStatus();

    String getPath(Context context, Uri uri);

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    String getMD5OfFile(File file);

    /**
     * output text to file (call this function in a new thread)
     *
     * @param content
     * @param file
     * @throws IOException
     */
    void outTextToFile(String content, File file) throws IOException;

    /**
     * download file (call this function in a new thread)
     *
     * @param downloadUrl
     * @param destFile
     * @throws Exception
     */
    void downloadFile(String downloadUrl, String destFile) throws IOException;
}
