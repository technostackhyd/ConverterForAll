package in.technostack.projects.converterforall.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CurrencyReaderHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.CurrencyEntry.TABLE_NAME + " (" +
                    CurrencyRates.CurrencyEntry._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.CurrencyEntry.COLUMN_NAME_TITLE + " TEXT," +
                    CurrencyRates.CurrencyEntry.COLUMN_NAME_VALUE + " TEXT," +
                    CurrencyRates.CurrencyEntry.COLUMN_NAME_TIME + " TEXT)";
    private static final String SQL_CREATE_ENTRIES_2=
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.UserCurrPrefrences.TABLE_NAME+" ("+
                    CurrencyRates.UserCurrPrefrences._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY + " TEXT)";
    private static final String SQL_CREATE_ENTRIES_3=
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.UserPreferences.TABLE_NAME + " (" +
                    CurrencyRates.UserPreferences._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.UserPreferences.COLUMN_NAME_CONVERT + " TEXT," +
                    CurrencyRates.UserPreferences.COLUMN_NAME_VALUE1 + " INTEGER," +
                    CurrencyRates.UserPreferences.COLUMN_NAME_VALUE2 + " INTEGER)";

    private static final String SQL_CREATE_ENTRIES_4=
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.Favorites.TABLE_NAME + " (" +
                    CurrencyRates.Favorites._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.Favorites.COLUMN_NAME_IMAGE + " INTEGER," +
                    CurrencyRates.Favorites.COLUMN_NAME_TITLE + " TEXT," +
                    CurrencyRates.Favorites.COLUMN_NAME_POSITION + " INTEGER," +
                    CurrencyRates.Favorites.COLUMN_NAME_LINK + " INTEGER)";
    private static final String SQL_CREATE_ENTRIES_5=
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.HomeElements.TABLE_NAME + " (" +
                    CurrencyRates.HomeElements._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.HomeElements.COLUMN_ELEMENT_IMG_ID + " INTEGER," +
                    CurrencyRates.HomeElements.COLUMN_ELEMENT_NAME + " TEXT," +
                    CurrencyRates.HomeElements.COLUMN_ELEMENT_LINK + " INTEGER," +
                    CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION + " INTEGER," +
                    CurrencyRates.HomeElements.COLUMN_ELEMENT_TYPE + " INTEGER)";
    private static final String SQL_UPDATE_ENTRIES_FAVORITES =
            "ALTER TABLE " + CurrencyRates.Favorites.TABLE_NAME + " ADD COLUMN " + CurrencyRates.Favorites.COLUMN_NAME_POSITION + " INTEGER DEFAULT 0";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CurrencyRates.CurrencyEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_2 =
            "DROP TABLE IF EXISTS " + CurrencyRates.UserCurrPrefrences.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_3 =
            "DROP TABLE IF EXISTS " + CurrencyRates.UserPreferences.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_4 =
            "DROP TABLE IF EXISTS " + CurrencyRates.Favorites.TABLE_NAME;
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "CurrencyRecords.db";
    public CurrencyReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_CREATE_ENTRIES_2);
            db.execSQL(SQL_CREATE_ENTRIES_3);
            db.execSQL(SQL_CREATE_ENTRIES_4);
            db.execSQL(SQL_CREATE_ENTRIES_5);
        }
        catch (Exception e){

        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion> oldVersion){
            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_CREATE_ENTRIES_2);
            db.execSQL(SQL_CREATE_ENTRIES_3);
            db.execSQL(SQL_CREATE_ENTRIES_4);
            db.execSQL(SQL_CREATE_ENTRIES_5);
        }
        db.execSQL(SQL_UPDATE_ENTRIES_FAVORITES);
    }
}
