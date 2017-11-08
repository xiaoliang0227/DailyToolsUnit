package com.zyl.tools.dailytoolsunit.tool;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.zyl.tools.dailytoolsunit.interf.IFileTools;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitCommonConsts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public class FileTools implements IFileTools {

    private static FileTools instance;

    public static FileTools getInstance() {
        if (null == instance) {
            instance = new FileTools();
        }
        return instance;
    }

    /**
     * 获取盘符
     *
     * @return
     */
    @Override
    public HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
    }

    @Override
    public String formatSambaFileName(String name) {
        String tmp = name;
        if (tmp.indexOf("/") >= 0) {
            tmp = name.substring(0, tmp.length() - 1);
        }
        return tmp;
    }

    @Override
    public String getMimeType(File file) throws MalformedURLException {
        return fileExt(file.getPath());
    }

    @Override
    public String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return "unknwon";
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    /**
     * 打开指定文件
     *
     * @param context
     * @param file
     */
    @Override
    public void openFile(Context context, File file) {
        MimeTypeMap myMimeType = MimeTypeMap.getSingleton();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMimeType.getExtensionFromMimeType(fileExt(file.getPath())).substring(1);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "当前格式不支持浏览", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    @Override
    public void deleteFile(File file) {
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File item : files) {
                        deleteFile(item);
                    }
                } else {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkSdcardStatus() {
        boolean flag = true;
        // check whether the sdcard is mounted
        flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        // check available size
        StatFs status = new StatFs(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        long availableBlocks = 0L;
        long blockSize = 0L;
        if (Build.VERSION.SDK_INT < 18) {
            availableBlocks = status.getAvailableBlocks();
            blockSize = status.getBlockSize();
        } else {
            availableBlocks = status.getAvailableBlocksLong();
            blockSize = status.getBlockSizeLong();
        }
        boolean tmp = ((availableBlocks * blockSize) / 1024 > 10);
        flag &= tmp;
        return flag;
    }

    @Override
    public String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else
            return uri.getPath();
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    @Override
    public String getMD5OfFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase(Locale.ENGLISH);//转为大写
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return TextUtils.isEmpty(value) ? "" : value;
    }

    /**
     * output text to file (call this function in a new thread)
     *
     * @param content
     * @param file
     * @throws IOException
     */
    @Override
    public void outTextToFile(String content, File file) throws IOException {
        if (null == content || null == file) {
            return;
        }
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
        bw.write(content);
        bw.newLine();
        bw.close();
    }

    /**
     * download file (call this function in a new thread)
     *
     * @param downloadUrl
     * @param destFile
     * @throws Exception
     */
    @Override
    public void downloadFile(String downloadUrl, String destFile) throws IOException {
        InputStream input;
        OutputStream output;
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(ToolsUnitCommonConsts.DEFAULT_TIMEOUT_CONNECTION);
        connection.setReadTimeout(ToolsUnitCommonConsts.DEFAULT_TIMEOUT_SOCKET);
        input = connection.getInputStream();
        File folder = new File(destFile.substring(0, destFile.lastIndexOf("/")));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        output = new FileOutputStream(destFile, false);
        byte buffer[] = new byte[1024 * 5];
        int readsize = 0;
        while ((readsize = input.read(buffer)) != -1) {
            output.write(buffer, 0, readsize);
        }
        if (connection != null) {
            connection.disconnect();
        }
        input.close();
        output.flush();
        output.close();
    }
}
