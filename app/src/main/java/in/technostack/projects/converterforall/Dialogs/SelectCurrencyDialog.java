package in.technostack.projects.converterforall.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import in.technostack.projects.converterforall.Database.CurrencyRates;
import in.technostack.projects.converterforall.Database.CurrencyReaderHelper;
import in.technostack.projects.converterforall.R;
import in.technostack.projects.converterforall.Values;
import in.technostack.projects.converterforall.ViewAdapters.SelectedArrayAdapter;

public class SelectCurrencyDialog extends DialogFragment {
    View view;
    ListView selectCurrList;
    SelectedArrayAdapter adapter;
    CurrencyReaderHelper mDbHelper;
    public String Title;
    public int NUMBER;
    ArrayList<Integer> currOther=new ArrayList<>();
    private static final String SQL_CREATE_ENTRIES_2=
            "CREATE TABLE IF NOT EXISTS " + CurrencyRates.UserCurrPrefrences.TABLE_NAME+" ("+
                    CurrencyRates.UserCurrPrefrences._ID + " INTEGER PRIMARY KEY," +
                    CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY + " TEXT)";
    public SelectCurrencyDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyCustomTheme);
        mDbHelper=new CurrencyReaderHelper(getActivity().getApplicationContext());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.select_currency_list,null);
        SearchView search=view.findViewById(R.id.selectCurrSearch);
        TextView selectTitle=view.findViewById(R.id.selectTitle);
        selectTitle.setText(Title);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter!=null){
                    adapter.getFilter().filter(s);
                    return true;
                }
                return false;
            }
        });
        selectCurrList=view.findViewById(R.id.selectCurrListview);
        Values obj=new Values();
        ArrayList<CurrencyModel> curPrefList= obj.getSelectedCurrency();
        setSelectionPref();
        for(int i=0; i<currOther.size(); i++){
            curPrefList.get(currOther.get(i)).isSelected = true;
        }
        adapter=new SelectedArrayAdapter(getActivity(),R.layout.currency_select_list_item, curPrefList,NUMBER);
        selectCurrList.setAdapter(adapter);
        selectCurrList.setClickable(true);
        setCancelable(false);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<CurrencyModel> list= adapter.getList();
                int currIndex=0;
                ArrayList currIndexArray=new ArrayList();
                    for (int index = 0; index < list.size(); index++) {
                        if (list.get(index).isSelected()) {
                            currIndexArray.add(index);
                        }
                    }
                    if (currIndexArray.size()>=1) {
                        boolean b = insert(currIndex, currIndexArray);
                        if (b)
                            sendResult(2018, "success");
                        else
                            sendResult(2018,"fail");
                    }
                    else {
                        sendResult(2018,"fail");
                    }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    sendResult(2018,"cancel");
            }
        });
        builder.setView(view);
        return builder.create();
    }
    private void sendResult(int REQUEST_CODE,String RESULT) {
        Intent intent = new Intent();
        intent.putExtra("Result",RESULT);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

    private boolean delete(String TableName){
        SQLiteDatabase db=null;
        try{
                db = mDbHelper.getWritableDatabase();
                db.execSQL("delete from "+ TableName);
                return true;
        }
        catch (Exception e)
        {
            db.execSQL(SQL_CREATE_ENTRIES_2);
            return true;
        }

    }

    private boolean insert(int curr,ArrayList currArray){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
                boolean b = delete(CurrencyRates.UserCurrPrefrences.TABLE_NAME);
                if (b) {
                    db.beginTransaction();
                    for (int i = 0; i < currArray.size(); i++) {
                        ContentValues values = new ContentValues();
                        values.put(CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY, currArray.get(i).toString());
                        long id=db.insert(CurrencyRates.UserCurrPrefrences.TABLE_NAME, null, values);
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    return true;
                }
        }
        catch (Exception e){

        }
        return false;
    }

    private void setSelectionPref(){
        Cursor cursor1 = getPref(CurrencyRates.UserCurrPrefrences.TABLE_NAME,CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY);
        if (cursor1!=null && cursor1.moveToFirst()){
            try {
                String currValue1;
                currOther.clear();
                do {
                    currValue1 = cursor1.getString(
                            cursor1.getColumnIndexOrThrow(CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY));
                    currOther.add(Integer.parseInt(currValue1));
                }while (cursor1.moveToNext());
                cursor1.close();
            }
            catch (Exception e){
            }
        }
    }
    private Cursor getPref(String TableName, String ColumnName){
        try{
            if (Count(TableName)>0) {
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select " + ColumnName + " FROM " + TableName, null);
                return cursor;
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    private long Count(String TableName) {
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            long cnt = DatabaseUtils.queryNumEntries(db, TableName);
            db.close();
            return cnt;
        }
        catch (Exception e){
            return 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}
