package edu.csulb.android.notetakingapplication;

import android.provider.BaseColumns;

/**
 * Created by Gaurav on 26-02-2017.
 */

public class TableData {
    public TableData(){}

    public static abstract class TableInfo implements BaseColumns{
        public static final String PHOTO_ID ="_id";
        public static final String PHOTO_CAPTION ="photo_caption";
        public static final String PHOTO_PATH ="photo_path";
        public static final String PHOTO_NUMBER = "photo_number";
        public static final String DATABASE_NAME="note_database";
        public static final String TABLE_NAME="photo_info";

    }
}
