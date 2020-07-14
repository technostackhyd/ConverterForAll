package in.technostack.projects.converterforall.Database;

import android.provider.BaseColumns;

public class CurrencyRates {
    private CurrencyRates(){

    }
    public static class CurrencyEntry implements BaseColumns {
        public static final String TABLE_NAME = "rates";
        public static final String COLUMN_NAME_TITLE = "currency";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_TIME = "time";
    }
    public static class UserCurrPrefrences implements BaseColumns{
        public static final String TABLE_NAME="otherCurr";
        public static final String COLUMN_OTHER_CURRENCY="other";
    }
    public static class UserPreferences implements BaseColumns{
        public static final String TABLE_NAME="converterValues";
        public static final String COLUMN_NAME_CONVERT="convert";
        public static final String COLUMN_NAME_VALUE1="value1";
        public static final String COLUMN_NAME_VALUE2="value2";
    }

    public static class Favorites implements BaseColumns{
        public static final String TABLE_NAME="favorites";
        public static final String COLUMN_NAME_IMAGE="image";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_LINK="link";
        public static final String COLUMN_NAME_POSITION="position";
    }
    public static class HomeElements implements BaseColumns {
        public static final String TABLE_NAME = "home";
        public static final String COLUMN_ELEMENT_IMG_ID = "image";
        public static final String COLUMN_ELEMENT_NAME = "name";
        public static final String COLUMN_ELEMENT_LINK = "link";
        //1-Calculator, 2-Converter
        public static final String COLUMN_ELEMENT_TYPE = "type";
        public static final String COLUMN_ELEMENT_POSITION = "position";
    }
}
