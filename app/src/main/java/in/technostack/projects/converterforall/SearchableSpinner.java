package in.technostack.projects.converterforall;

import android.widget.Spinner;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import java.util.ArrayList;
import java.util.List;


public class SearchableSpinner extends Spinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem {

    String selectedItem;
    int selectedItemPosition;
    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private List _items;
    private ArrayList<CurrencyModel> _currItems;
    private SearchableListDialog _searchableListDialog;
    private boolean isCurr=false;
    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;

    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }

    private void init() {
        _items = new ArrayList<>();
        _searchableListDialog = SearchableListDialog.newInstance
                (_items);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);
        _arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                    .simple_list_item_1, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
                if (null != _arrayAdapter) {
                    _items.clear();
                    for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                        _items.add(_arrayAdapter.getItem(i));
                    }
                    _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
                }

        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        try {
            if (!_isFromInit) {
                    _arrayAdapter = (ArrayAdapter) adapter;
                if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                            .simple_list_item_1, new String[]{_strHintText});
                    super.setAdapter(arrayAdapter);
                } else {
                    super.setAdapter(adapter);
                }

            } else {
                _isFromInit = false;
                super.setAdapter(adapter);
            }
        }
        catch (Exception e) {
        }
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {

        setSelection(_items.indexOf(item));
        if (!_isDirty) {
            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(_items.indexOf(item));
        }
        if (!isCurr) {
            selectedItemPosition = _items.indexOf(item);
            selectedItem = item.toString();
        }
        else {
            CurrencyModel obj=(CurrencyModel) item;
            selectedItemPosition = _items.indexOf(item);
            selectedItem = obj.getCurrName();
        }
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {

            return super.getSelectedItem();
        }
    }

    public void setTitle(String title){
        _searchableListDialog.setTitle(title);
    }

    public void setCurr(boolean b){
        isCurr=b;
        _searchableListDialog.setCurr(b);
    }
}
