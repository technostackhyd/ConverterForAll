package in.technostack.projects.converterforall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import in.technostack.projects.converterforall.DataModels.ImageItem;
import in.technostack.projects.converterforall.Database.CurrencyRates;
import in.technostack.projects.converterforall.Database.CurrencyReaderHelper;
import in.technostack.projects.converterforall.ViewAdapters.GridViewAdapter;
import in.technostack.projects.converterforall.ViewAdapters.RecyclerAdapter;

public class ReArrangeHome extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<ImageItem> list;
    int HOME_TYPE = 0;
    SQLiteDatabase db;
    CurrencyReaderHelper mDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_arrange_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Arrange Items");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        HOME_TYPE = intent.getIntExtra("type", 0);
        list = (ArrayList<ImageItem>) intent.getSerializableExtra("list");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(list);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            float f = 5;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.spinner_center_color));
                viewHolder.itemView.setElevation(f);
            }
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(list, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            UpdateHomeDb();
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    private void UpdateHomeDb(){
        mDbHelper = new CurrencyReaderHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        String TABLE_NAME = "";
        String COLUMN_UPDATE ="";
        String COLUMN_WHERE ="";
        //1 - Calculator, 2- Converter, 3- Favorites
        if(HOME_TYPE == 1 || HOME_TYPE == 2){
            TABLE_NAME = CurrencyRates.HomeElements.TABLE_NAME;
            COLUMN_UPDATE = CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION;
            COLUMN_WHERE = CurrencyRates.HomeElements.COLUMN_ELEMENT_NAME;
        }
        else {
            TABLE_NAME = CurrencyRates.Favorites.TABLE_NAME;
            COLUMN_UPDATE = CurrencyRates.Favorites.COLUMN_NAME_POSITION;
            COLUMN_WHERE = CurrencyRates.Favorites.COLUMN_NAME_TITLE;
        }
        db.beginTransaction();
        for (int i=0; i<list.size(); i++){
            ContentValues newValues = new ContentValues();
            newValues.put(COLUMN_UPDATE, i+1);
            String[] args = new String[]{list.get(i).getTitle()};
            db.update(TABLE_NAME, newValues, COLUMN_WHERE +"=?", args);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
