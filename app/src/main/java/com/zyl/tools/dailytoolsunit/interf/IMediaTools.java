package com.zyl.tools.dailytoolsunit.interf;

import android.content.Context;
import android.database.Cursor;

import com.zyl.tools.dailytoolsunit.model.MediaModel;

import java.util.List;

/**
 * Created by zhaoyongliang on 2017/6/16.
 */

public interface IMediaTools {

    String[] getImageResourceCursorColumns();

    String[] getThumbColumns();

    String[] getVideoColumns();

    String[] getAudioColumns();

    List<MediaModel> fetchExternalImageResource(Context context);

    List<MediaModel> fetchInternalImageResource(Context context);

    MediaModel getImageResourceItem(Cursor cursor);

    List<MediaModel> fetchExternalVideoResource(Context context);

    MediaModel getVideoResourceItem(Cursor cursor, Context context);

    List<MediaModel> fetchInternalVideoResource(Context context);

    List<MediaModel> fetchExternalAudioResource(Context context);

    MediaModel getAudioResourceItem(Cursor cursor);

    List<MediaModel> fetchInternalAudioResource(Context context);

    /**
     * 关闭数据库游标
     *
     * @param cursor
     */
    void closeCursor(Cursor cursor);


}
