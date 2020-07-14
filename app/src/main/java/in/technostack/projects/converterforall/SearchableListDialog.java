package in.technostack.projects.converterforall;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import in.technostack.projects.converterforall.ViewAdapters.CurrArrayAdapter;

public class SearchableListDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";

    private ArrayAdapter listAdapter;

    private CurrArrayAdapter currListAdapter;

    private ListView _listViewItems;

    private SearchableItem _searchableItem;

    private OnSearchTextChanged _onSearchTextChanged;

    private SearchView _searchView;

    private String _strTitle;

    private String _strPositiveButtonText;

    private boolean isCurr=false;

    private DialogInterface.OnClickListener _onClickListener;

    public SearchableListDialog() {

    }

    public static SearchableListDialog newInstance(List items) {
        SearchableListDialog multiSelectExpandableFragment = new
                SearchableListDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);

        multiSelectExpandableFragment.setArguments(args);
        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        if (null != savedInstanceState) {
            _searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        alertDialog.setView(rootView);

        String strPositiveButton = _strPositiveButtonText == null ? "CLOSE" : _strPositiveButtonText;
        alertDialog.setPositiveButton(strPositiveButton, _onClickListener);

        String strTitle = _strTitle == null ? "Select Item" : _strTitle;
        //alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("item", _searchableItem);
    }

    public void setTitle(String strTitle) {
        _strTitle = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _strPositiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _strPositiveButtonText = strPositiveButtonText;
        _onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this._searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this._onSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        _searchView = (SearchView) rootView.findViewById(R.id.search);
        _searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        _searchView.setIconifiedByDefault(false);
        _searchView.setOnQueryTextListener(this);
        _searchView.setOnCloseListener(this);
        _searchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);

        _listViewItems = (ListView) rootView.findViewById(R.id.listItems);

        if (!isCurr) {
            List items = (List) getArguments().getSerializable(ITEMS);
            listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);        //attach the adapter to the list
            _listViewItems.setAdapter(listAdapter);
        }
        else {
            float scale = getResources().getDisplayMetrics().density;
            ColorDrawable transparent = new ColorDrawable(getResources().getColor(R.color.transparent));
            _listViewItems.setDivider(transparent);
            _listViewItems.setDividerHeight((int) (20*scale+0.5f));
            ArrayList<CurrencyModel> currItems = (ArrayList) getArguments().getSerializable(ITEMS);
            currListAdapter = new CurrArrayAdapter(getActivity(), R.layout.currency_list_item, currItems);        //attach the adapter to the list
            _listViewItems.setAdapter(currListAdapter);
        }

        _listViewItems.setTextFilterEnabled(true);
        _listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isCurr)
                    _searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
                else
                    _searchableItem.onSearchableItemClicked(currListAdapter.getItem(position),position);

                getDialog().dismiss();
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        _searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        listAdapter.filterData(s);
        if (TextUtils.isEmpty(s)) {
//                _listViewItems.clearTextFilter();
            if (!isCurr)
                ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(null);
            else
                ((CurrArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(null);
        } else {
            if (!isCurr)
                ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(s);
            else {
                ((CurrArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(s);
            }
        }
        if (null != _onSearchTextChanged) {
            _onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }

    public void setCurr(boolean b){
        isCurr=b;
    }

    @Override
    public void onPause() {

        dismiss();
        super.onPause();
    }
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
