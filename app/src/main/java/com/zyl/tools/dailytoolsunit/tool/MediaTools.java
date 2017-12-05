package com.zyl.tools.dailytoolsunit.tool;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import com.zyl.tools.dailytoolsunit.enumeration.MediaType;
import com.zyl.tools.dailytoolsunit.interf.IMediaTools;
import com.zyl.tools.dailytoolsunit.model.MediaModel;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyongliang on 2017/6/16.
 */

public class MediaTools implements IMediaTools {

    private final static String PICTURES_DEFAULT_SORT = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

    private final static String VIDEOES_DEFAULT_SORT = MediaStore.Video.Media.DATE_MODIFIED + " DESC";

    private final static String AUDIOES_DEFAULT_SORT = MediaStore.Audio.Media.DATE_MODIFIED + " DESC";

    private static final String TAG = "MediaTools";

    private MediaTools() {

    }

    public static MediaTools getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final MediaTools instance = new MediaTools();
    }


    @Override
    public String[] getImageResourceCursorColumns() {
        String[] columns = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            columns = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.WIDTH
            };
        } else {
            columns = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.TITLE,
            };
        }
        return columns;
    }

    @Override
    public String[] getThumbColumns() {
        return new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };
    }

    @Override
    public String[] getVideoColumns() {
        String[] columns = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            columns = new String[]{
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_MODIFIED,
                    MediaStore.Video.Media.HEIGHT,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.TITLE,
                    MediaStore.Video.Media.WIDTH
            };
        } else {
            columns = new String[]{
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_MODIFIED,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.TITLE,
            };
        }
        return columns;
    }

    @Override
    public String[] getAudioColumns() {
        String[] columns = new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.TITLE,
        };
        return columns;
    }

    @Override
    public List<MediaModel> fetchExternalImageResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                getImageResourceCursorColumns(), null, null, PICTURES_DEFAULT_SORT);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getImageResourceItem(cursor));
            }
        }
        closeCursor(cursor);
        return data;
    }

    @Override
    public List<MediaModel> fetchInternalImageResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                getImageResourceCursorColumns(), null, null, PICTURES_DEFAULT_SORT);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getImageResourceItem(cursor));
            }
        }
        closeCursor(cursor);
        return data;
    }

    @Override
    public MediaModel getImageResourceItem(Cursor cursor) {
        MediaModel item = new MediaModel();
        item.setType(MediaType.IMAGE);
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
        item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        item.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)));
        }
        item.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)));
        item.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
        item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)));
        }
        item.setLast_modified(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)) * 1000);
        ToolsUnitLogUtil.debug(TAG, "image resource item:" + item.toString());
        return item;
    }

    @Override
    public List<MediaModel> fetchExternalVideoResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                getVideoColumns(), null, null, null);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getVideoResourceItem(cursor, context));
            }
        }
        closeCursor(cursor);
        return data;
    }

    @Override
    public MediaModel getVideoResourceItem(Cursor cursor, Context context) {
        MediaModel item = new MediaModel();
        item.setType(MediaType.VIDEO);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
        Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                getThumbColumns(), String.format("%s = %d", MediaStore.Video.Thumbnails.VIDEO_ID, id), null, null);
        if (thumbCursor.moveToFirst()) {
            item.setThumbPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
        }
        item.setId(id);
        item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
        item.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)));
        }
        item.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
        item.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
        item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)));
        }
        item.setLast_modified(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)) * 1000);
        ToolsUnitLogUtil.debug(TAG, "video resource item:" + item.toString());
        return item;
    }

    @Override
    public List<MediaModel> fetchInternalVideoResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                getVideoColumns(), null, null, VIDEOES_DEFAULT_SORT);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getVideoResourceItem(cursor, context));
            }
        }
        closeCursor(cursor);
        return data;
    }

    @Override
    public List<MediaModel> fetchExternalAudioResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                getAudioColumns(), null, null, null);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getAudioResourceItem(cursor));
            }
        }
        closeCursor(cursor);
        return data;
    }

    @Override
    public MediaModel getAudioResourceItem(Cursor cursor) {
        MediaModel item = new MediaModel();
        item.setType(MediaType.AUDIO);
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
        item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
        item.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
        item.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)));
        item.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
        item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
        item.setLast_modified(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)) * 1000);
        ToolsUnitLogUtil.debug(TAG, "audio resource item:" + item.toString());
        return item;
    }

    @Override
    public List<MediaModel> fetchInternalAudioResource(Context context) {
        List<MediaModel> data = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                getAudioColumns(), null, null, AUDIOES_DEFAULT_SORT);
        if (null != cursor) {
            data = new ArrayList<>();
            while (cursor.moveToNext()) {
                data.add(getAudioResourceItem(cursor));
            }
        }
        closeCursor(cursor);
        return data;
    }

    /**
     * 关闭数据库游标
     *
     * @param cursor
     */
    @Override
    public void closeCursor(Cursor cursor) {
        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
