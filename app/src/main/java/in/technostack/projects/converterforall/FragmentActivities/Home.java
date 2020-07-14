package in.technostack.projects.converterforall.FragmentActivities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import in.technostack.projects.converterforall.DataModels.ImageItem;
import in.technostack.projects.converterforall.Database.CurrencyRates;
import in.technostack.projects.converterforall.Database.CurrencyReaderHelper;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.ReArrangeHome;
import in.technostack.projects.converterforall.ViewAdapters.GridViewAdapter;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;
public class Home extends Fragment {
    View view;
    GridView calView, conView, favView;
    ImageView favReorder, calReorder, conReorder;
    TextView favDetails;
    Fragment fr;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;
    private GridViewAdapter calGridAdapter,conGridAdapter,favGridAdapter;
    String getUrl ="http://currencies.technostack.in/getOffer.php?name=converter";
    ConnectivityManager conMgr;
    LinearLayout favLayout;
    SQLiteDatabase db;
    CurrencyReaderHelper mDbHelper;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean favBool;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.home,container,false);
        bundle=new Bundle();
        fragmentManager=getFragmentManager();
        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mDbHelper=new CurrencyReaderHelper(getActivity().getApplicationContext());
        if(Count(CurrencyRates.HomeElements.TABLE_NAME)<=0){
            AddHomeData();
        }
        calView=view.findViewById(R.id.gridView1);
        conView=view.findViewById(R.id.gridView2);
        favView=view.findViewById(R.id.gridView3);
        favReorder = view.findViewById(R.id.favReorder);
        calReorder = view.findViewById(R.id.calReorder);
        conReorder = view.findViewById(R.id.conReorder);
        favLayout=view.findViewById(R.id.favLayout);
        favDetails = view.findViewById(R.id.favDetails);
        calGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getHomeData(1));
        calView.setAdapter(calGridAdapter);
        conGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getHomeData(2));
        conView.setAdapter(conGridAdapter);
        favGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getFavData());
        favView.setAdapter(favGridAdapter);
        preferences = getActivity().getSharedPreferences("reviewAppHome", Context.MODE_PRIVATE);
        editor = preferences.edit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        calView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                int ids=item.getLink();
                if (ids!=-1)
                setMenu(ids);
            }});
        conView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                int ids=item.getLink();
                if (ids!=-1)
                setMenu(ids);
            }});
        favView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                String title=item.getTitle();
                int ids=getScreenId(title);
                if (ids!=-1)
                    setMenu(ids);
            }});
        favReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReArrangeHome.class);
                //3 - Favorites
                intent.putExtra("type", 3);
                intent.putExtra("list", favGridAdapter.getList());
                startActivityForResult(intent, 3);
            }
        });
        calReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReArrangeHome.class);
                //1- Calculator
                intent.putExtra("type", 1);
                intent.putExtra("list", calGridAdapter.getList());
                startActivityForResult(intent,1);
            }
        });
        conReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReArrangeHome.class);
                //2- Converter
                intent.putExtra("type", 2);
                intent.putExtra("list", conGridAdapter.getList());
                startActivityForResult(intent,2);
            }
        });
        ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Home));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (i==KeyEvent.KEYCODE_BACK){
                        getActivity().finish();
                        return true;
                    }
                return false;
            }
        });
        setHasOptionsMenu(true);
        registerForContextMenu(calView);
        registerForContextMenu(conView);
        registerForContextMenu(favView);
        String locale;
        if (!checkOffer())
        {
            locale=getUserCountry(getActivity());
            if (locale==null)
                locale = getResources().getConfiguration().locale.getCountry();
            getUrl =
                    getUrl + "&country=" + locale;
            if (checkConnection()) {
                setOffer(getUrl);
            }
        }
        ((MainActivity) getActivity()).setScreenName(getResources().getString(R.string.Home));
        return view;
    }

    private long Count(String TABLE_NAME){
        db = mDbHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return count;
    }
    private void AddHomeData(){
        db=mDbHelper.getWritableDatabase();
        ArrayList<ImageItem> data = getData();
        ArrayList<ImageItem> data1 = getCalData();
        db.beginTransaction();
        try {
            for (int i =0; i<data1.size(); i++){
                ImageItem item = data1.get(i);
                ContentValues values = new ContentValues();
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_NAME, item.getTitle());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_LINK, item.getLink());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_IMG_ID, item.getImage());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION, i+1);
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_TYPE, 1);
                db.insert(CurrencyRates.HomeElements.TABLE_NAME, null, values);
            }
            for (int i =0; i<data.size(); i++){
                ImageItem item = data.get(i);
                ContentValues values = new ContentValues();
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_NAME, item.getTitle());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_LINK, item.getLink());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_IMG_ID, item.getImage());
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION, i+1);
                values.put(CurrencyRates.HomeElements.COLUMN_ELEMENT_TYPE, 2);
                db.insert(CurrencyRates.HomeElements.TABLE_NAME, null, values);
            }
        }
        catch (Exception e){
            db.endTransaction();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    private ArrayList<ImageItem> getHomeData(int TYPE){
        ArrayList<ImageItem> data = new ArrayList<ImageItem>();
        db = mDbHelper.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM " + CurrencyRates.HomeElements.TABLE_NAME + " Where " + CurrencyRates.HomeElements.COLUMN_ELEMENT_TYPE +" = " + TYPE + " ORDER BY " + CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION + " ASC", null);
            while (cursor.moveToNext()){
                int image=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.HomeElements.COLUMN_ELEMENT_IMG_ID));
                String title=cursor.getString(cursor.getColumnIndexOrThrow(CurrencyRates.HomeElements.COLUMN_ELEMENT_NAME));
                int link=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.HomeElements.COLUMN_ELEMENT_LINK));
                int position=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.HomeElements.COLUMN_ELEMENT_POSITION));
                ImageItem item=new ImageItem(image,title,link, position);
                data.add(item);
            }
            Log.d("LOLOL", data.size()+"");
            cursor.close();
        }
        catch (Exception e){

        }
        return data;
    }
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        ImageItem i1=new ImageItem(R.drawable.currency,getResources().getString(R.string.Currency),R.id.curr, 1);
        ImageItem i2=new ImageItem(R.drawable.volume,getResources().getString(R.string.Volume),R.id.vol,2);
        ImageItem i3=new ImageItem(R.drawable.length,getResources().getString(R.string.Length),R.id.len,3);
        ImageItem i4=new ImageItem(R.drawable.weight,getResources().getString(R.string.Weight),R.id.wt,4);
        ImageItem i5=new ImageItem(R.drawable.temp,getResources().getString(R.string.Temperature),R.id.temp,5);
        ImageItem i6=new ImageItem(R.drawable.area,getResources().getString(R.string.Area),R.id.area,6);
        ImageItem i7=new ImageItem(R.drawable.power,getResources().getString(R.string.Power),R.id.power,7);
        ImageItem i8=new ImageItem(R.drawable.speed,getResources().getString(R.string.Speed),R.id.speed,8);
        ImageItem i9=new ImageItem(R.drawable.time,getResources().getString(R.string.Time),R.id.time,9);
        ImageItem i10=new ImageItem(R.drawable.data,getResources().getString(R.string.Data),R.id.data,10);
        ImageItem i11=new ImageItem(R.drawable.energy,getResources().getString(R.string.Energy),R.id.energy,11);
        ImageItem i12=new ImageItem(R.drawable.angle,getResources().getString(R.string.Angle),R.id.angle,12);
        ImageItem i13=new ImageItem(R.drawable.pressure,getResources().getString(R.string.Pressure),R.id.pressure,13);
        ImageItem i14=new ImageItem(R.drawable.ic_frequency,getResources().getString(R.string.Frequency),R.id.frequency,14);
        ImageItem i15=new ImageItem(R.drawable.transfer,getResources().getString(R.string.DataRate),R.id.datarate,15);
        ImageItem i16=new ImageItem(R.drawable.density,getResources().getString(R.string.densityTitle),R.id.density,16);
        imageItems.add(i1);
        imageItems.add(i2);
        imageItems.add(i3);
        imageItems.add(i4);
        imageItems.add(i5);
        imageItems.add(i6);
        imageItems.add(i7);
        imageItems.add(i8);
        imageItems.add(i9);
        imageItems.add(i10);
        imageItems.add(i11);
        imageItems.add(i12);
        imageItems.add(i13);
        imageItems.add(i14);
        imageItems.add(i15);
        imageItems.add(i16);
        return imageItems;
    }
    private ArrayList<ImageItem> getCalData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        ImageItem i1=new ImageItem(R.drawable.calculator, getResources().getString(R.string.calci),R.id.calculator,1);
        ImageItem i2=new ImageItem(R.drawable.date,getResources().getString(R.string.Age),R.id.cdate,2);
        ImageItem i3=new ImageItem(R.drawable.percentage,getResources().getString(R.string.Percentage),R.id.percent,3);
        ImageItem i4=new ImageItem(R.drawable.bmi,getResources().getString(R.string.bmi),R.id.bmi,4);
        final SharedPreferences preferencesOffer = getActivity().getSharedPreferences("offer", Context.MODE_PRIVATE);
        String res=preferencesOffer.getString("percentage","0");
        String active=preferencesOffer.getString("active","0");
        ImageItem i5;
        if(!res.equals("0") && active.equals("1"))
            i5=new ImageItem(R.drawable.star_50,getResources().getString(R.string.gopro).substring(3)+"-"+res+getResources().getString(R.string.off),R.id.pro,5);
        else
            i5=new ImageItem(R.drawable.star_50,getResources().getString(R.string.gopro),R.id.pro,5);
        imageItems.add(i1);
        imageItems.add(i2);
        imageItems.add(i3);
        imageItems.add(i4);
        imageItems.add(i5);
        return imageItems;
    }
    private ArrayList<ImageItem> getFavData() {
        db = mDbHelper.getReadableDatabase();
        ArrayList<ImageItem> favorites=new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + CurrencyRates.Favorites.TABLE_NAME + " ORDER BY " + CurrencyRates.Favorites.COLUMN_NAME_POSITION + " ASC", null);
            while (cursor.moveToNext()){
                int image=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.Favorites.COLUMN_NAME_IMAGE));
                String title=cursor.getString(cursor.getColumnIndexOrThrow(CurrencyRates.Favorites.COLUMN_NAME_TITLE));
                int link=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.Favorites.COLUMN_NAME_LINK));
                int position=cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyRates.Favorites.COLUMN_NAME_POSITION));
                ImageItem item=new ImageItem(image,title,link, position);
                favorites.add(item);
            }
            cursor.close();
        }
        catch (Exception e){

        }
        return favorites;
    }
    private void setMenu(int id){
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,android.R.animator.fade_in,android.R.animator.fade_out);

        switch (id){
            case R.id.home:
                fr= new Home();
                fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.cdate:
                fr= new dateCalculate();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.percent:
                fr= new PercentageCalculate();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.pro:
                showDialog(getResources().getString(R.string.getProTitle),getResources().getString(R.string.pro),"user");
                break;
            case R.id.calculator:
                fr=new Calculator();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.bmi:
                fr=new BmiCalculate();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            default:
                fr= new ConvertValues();
                bundle.putInt("case",id);
                fr.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.search_layout, menu);
            MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint("Search");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<ImageItem> imageItemscal=new ArrayList<>(getCalData());
                    ArrayList<ImageItem> filteredCal=new ArrayList<>();
                    ArrayList<ImageItem> imageItemscon=new ArrayList<>(getData());
                    ArrayList<ImageItem> filteredCon=new ArrayList<>();
                    ArrayList<ImageItem> imageItemsfav=new ArrayList<>(getFavData());
                    ArrayList<ImageItem> filteredFav=new ArrayList<>();
                    if (!newText.isEmpty()){
                        for (int i=0;i<imageItemscal.size();i++){
                            if (imageItemscal.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
                                filteredCal.add(imageItemscal.get(i));
                            }
                        }
                        for (int i=0;i<imageItemscon.size();i++){
                            if (imageItemscon.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
                                filteredCon.add(imageItemscon.get(i));
                            }
                        }
                        for (int i=0;i<imageItemsfav.size();i++){
                            if (imageItemsfav.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
                                filteredFav.add(imageItemsfav.get(i));
                            }
                        }

                        if (filteredCal.size()>=1){
                            calGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredCal);
                            calView.setAdapter(calGridAdapter);
                        }
                        else {
                            ImageItem im=new ImageItem(R.drawable.ic_sentiment_dissatisfied_yellow_24dp,getResources().getString(R.string.noresult),-1, 0);
                            filteredCal.add(im);
                            calGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredCal);
                            calView.setAdapter(calGridAdapter);

                        }
                        if (filteredCon.size()>=1){
                            conGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredCon);
                            conView.setAdapter(conGridAdapter);
                        }
                        else {
                            ImageItem im=new ImageItem(R.drawable.ic_sentiment_dissatisfied_yellow_24dp,getResources().getString(R.string.noresult),-1, 0);
                            filteredCon.add(im);
                            conGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredCon);
                            conView.setAdapter(conGridAdapter);
                        }
                        if (filteredFav.size()>=1){
                            favGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredFav);
                            favView.setAdapter(favGridAdapter);
                        }
                        else {
                            ImageItem im=new ImageItem(R.drawable.ic_sentiment_dissatisfied_yellow_24dp,getResources().getString(R.string.noresult),-1, 0);
                            filteredFav.add(im);
                            favGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, filteredFav);
                            favView.setAdapter(favGridAdapter);
                        }
                    }
                    else {
                        calGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getCalData());
                        calView.setAdapter(calGridAdapter);
                        conGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getData());
                        conView.setAdapter(conGridAdapter);
                        favGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item_layout, getFavData());
                        favView.setAdapter(favGridAdapter);
                    }
                    return true;
                }
            });
        }
        catch (Exception ex) {
        }
    }

    public void showDialog(String title,String message,String WHO) {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.WHO=WHO;
        try {
            dd.show(fm,"");
        }
        catch (Exception e){}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int g;
        if (v==calView)
            g=0;
        else if (v==conView)
            g=1;
        else
            g=2;
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
        int i=info.position;
        if (g==0 || g==1){
            menu.add(0,v.getId(),0,"Pin Shortcut to Home Screen");
            menu.add(g,v.getId(),i,"Add to Favorites");
        }
        else {
            menu.add(g,v.getId(),i,"Remove From Favorites");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().toString().contains("Pin Shortcut"))
            showDialog(getResources().getString(R.string.getProTitle),getResources().getString(R.string.pro),"user");
        else if (item.getTitle().toString().contains("Favorites")){
            boolean result;
            if (item.getGroupId()==0){
                result=addFavorites(getHomeData(1).get(item.getOrder()));
                if (result) {
                    favGridAdapter.add(getHomeData(1).get(item.getOrder()));
                    favGridAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), getResources().getString(R.string.favAdded), Toast.LENGTH_SHORT).show();
                }
                if(favGridAdapter.getCount()>0){
                    favDetails.setVisibility(View.GONE);
                }
            }
            else if (item.getGroupId()==1){
                result=addFavorites(getHomeData(2).get(item.getOrder()));
                if (result){
                    favGridAdapter.add(getHomeData(2).get(item.getOrder()));
                    favGridAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),getResources().getString(R.string.favAdded),Toast.LENGTH_SHORT).show();
                }
                if(favGridAdapter.getCount()>0){
                    favDetails.setVisibility(View.GONE);
                }
            }
            else {
                String[] val={String.valueOf(getFavData().get(item.getOrder()).getLink())};
                result=removeFavorites(CurrencyRates.Favorites.TABLE_NAME,CurrencyRates.Favorites.COLUMN_NAME_LINK,val);
                if (result){
                    favGridAdapter.remove(favGridAdapter.getItem(item.getOrder()));
                    favGridAdapter.notifyDataSetChanged();
                }
                if(favGridAdapter.getCount() == 0){
                        favDetails.setVisibility(View.VISIBLE);
                }
            }



        }
        return super.onContextItemSelected(item);
    }

    public boolean checkOffer(){
        final SharedPreferences preferencesOffer = getActivity().getSharedPreferences("offer", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferencesOffer.edit();
        if (preferencesOffer.getString("active","0").equals("1")) {
            String end = preferencesOffer.getString("end", "0");
            if (!end.equals("0")) {
                Double elapsedDays = dateDifference(end);
                if (elapsedDays < 0) {
                    editor.putString("active","0");
                    editor.commit();
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        else
        {
            return false;
        }

    }

    private void setOffer(String url){
        final SharedPreferences preferencesOffer = getActivity().getSharedPreferences("offer", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferencesOffer.edit();
        final String TAG_OFFERS = "offers";
        final String TAG_SUCCESS = "success";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int res = response.getInt(TAG_SUCCESS);
                            if (res == 1) {
                                JSONArray rates = response.getJSONArray(TAG_OFFERS);
                                JSONObject obj = rates.getJSONObject(0);
                                String percentage = obj.getString("percentage");
                                String start = obj.getString("start");
                                String end = obj.getString("end");
                                String active = obj.getString("active");
                                int position = 0;
                                try {
                                    for (int i=0;i<calGridAdapter.getCount();i++){
                                        ImageItem item=(ImageItem) calGridAdapter.getItem(i);
                                        position = item.getPosition();
                                        if (item.getLink()==R.id.pro){
                                            calGridAdapter.remove(calGridAdapter.getItem(i));
                                            break;
                                        }
                                    }
                                    calGridAdapter.add(new ImageItem(R.drawable.star_50,getResources().getString(R.string.gopro).substring(3)+"-"+percentage+getResources().getString(R.string.off),R.id.pro, position));
                                    calGridAdapter.notifyDataSetChanged();
                                }
                                catch (Exception e)
                                {

                                }
                                editor.putString("percentage", percentage);
                                editor.putString("start", start);
                                editor.putString("end", end);
                                editor.putString("active",active);
                                editor.commit();
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {

            }
        });
        queue.add(stringRequest);

    }

    private Double dateDifference(String end){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date today=calendar.getTime();
        String todayDate=dateFormat.format(today);
        Date todayD=null;
        Date endDate=null;
        try{
            endDate=dateFormat.parse(end);
            todayD=dateFormat.parse(todayDate);
        }
        catch (Exception e){

        }
        long different = endDate.getTime() - todayD.getTime();
        Double secondsInMilli = 1000.0;
        Double minutesInMilli = secondsInMilli * 60;
        Double hoursInMilli = minutesInMilli * 60;
        Double daysInMilli = hoursInMilli * 24;
        Double elapsedDays = different / daysInMilli;
        return elapsedDays;
    }

    //Connection
    private boolean checkConnection(){
        try {
            conMgr = (ConnectivityManager) getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);
            return conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected();
        }
        catch (Exception e){

        }
        return false;
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null) {
                if(simCountry.length() == 2){
                    // SIM country code is available
                    return simCountry.toLowerCase(Locale.US);
                }
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null) {
                    if(networkCountry.length() == 2){
                        // network country code is available
                        return networkCountry.toLowerCase(Locale.US);
                    }
                }
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }

    private boolean addFavorites(ImageItem imageItem){
        db=mDbHelper.getWritableDatabase();
        String[] val={String.valueOf(imageItem.getTitle())};
        long cnt=checkCount(CurrencyRates.Favorites.TABLE_NAME,CurrencyRates.Favorites.COLUMN_NAME_TITLE,val);
        if (cnt==0){
            try {
                long count = Count(CurrencyRates.Favorites.TABLE_NAME);
                ContentValues values=new ContentValues();
                values.put(CurrencyRates.Favorites.COLUMN_NAME_IMAGE,imageItem.getImage());
                values.put(CurrencyRates.Favorites.COLUMN_NAME_TITLE,imageItem.getTitle());
                values.put(CurrencyRates.Favorites.COLUMN_NAME_LINK,imageItem.getLink());
                values.put(CurrencyRates.Favorites.COLUMN_NAME_POSITION,count++);
                db.insert(CurrencyRates.Favorites.TABLE_NAME, null, values);
                return true;
            }
            catch (Exception e){
                return false;
            }
        }
        else {
            Toast.makeText(getActivity(),getResources().getString(R.string.alreadyInFav),Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private long checkCount(String TABLE, String COLUMN, String[] VALUE){
        db=mDbHelper.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE,COLUMN+" =? ",VALUE);
        return cnt;
    }

    private boolean removeFavorites(String TABLE, String COLUMN, String[] VALUE){
        db=mDbHelper.getWritableDatabase();
        int i=-1;
        try {
            i=db.delete(TABLE,COLUMN+"=?",VALUE);
            if (i!=-1)
                return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }

    private void showFav(boolean val){
        if (val)
            favLayout.setVisibility(View.VISIBLE);
        editor.putBoolean("showFav",val);
        editor.commit();
    }
    private void hideFav(boolean val){
        favBool=val;
        if (!val)
            favLayout.setVisibility(View.GONE);
        editor.putBoolean("showFav",val);
        editor.commit();
    }

    public int getScreenId(String screenName){
        int i=0;
        if (getString(R.string.calci).equalsIgnoreCase(screenName)){
            i=R.id.calculator;
        }
        else if (getString(R.string.Age).equalsIgnoreCase(screenName)){
            i=R.id.cdate;
        }
        else if (getString(R.string.Percentage).equalsIgnoreCase(screenName)){
            i=R.id.percent;
        }
        else if (getString(R.string.bmi).equalsIgnoreCase(screenName)){
            i=R.id.bmi;
        }
        else if (getString(R.string.Currency).equalsIgnoreCase(screenName)){
            i=R.id.curr;
        }
        else if (getString(R.string.Volume).equalsIgnoreCase(screenName)){
            i=R.id.vol;
        }
        else if (getString(R.string.Length).equalsIgnoreCase(screenName)){
            i=R.id.len;
        }
        else if (getString(R.string.Weight).equalsIgnoreCase(screenName)){
            i=R.id.wt;
        }
        else if (getString(R.string.Temperature).equalsIgnoreCase(screenName)){
            i=R.id.temp;
        }
        else if (getString(R.string.Area).equalsIgnoreCase(screenName)){
            i=R.id.area;
        }
        else if (getString(R.string.Power).equalsIgnoreCase(screenName)){
            i=R.id.power;
        }
        else if (getString(R.string.Speed).equalsIgnoreCase(screenName)){
            i=R.id.speed;
        }
        else if (getString(R.string.Time).equalsIgnoreCase(screenName)){
            i=R.id.time;
        }
        else if (getString(R.string.Data).equalsIgnoreCase(screenName)){
            i=R.id.data;
        }
        else if (getString(R.string.Energy).equalsIgnoreCase(screenName)){
            i=R.id.energy;
        }
        else if (getString(R.string.Angle).equalsIgnoreCase(screenName)){
            i=R.id.angle;
        }
        else if (getString(R.string.Pressure).equalsIgnoreCase(screenName)){
            i=R.id.pressure;
        }
        else if (getString(R.string.Frequency).equalsIgnoreCase(screenName)){
            i=R.id.frequency;
        }
        else if (getString(R.string.DataRate).equalsIgnoreCase(screenName)){
            i=R.id.datarate;
        }
        else if (getString(R.string.densityTitle).equalsIgnoreCase(screenName)){
            i=R.id.density;
        }
        return i;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LOLOL", "Back - " + requestCode);
        switch (requestCode){
            case 1:
                calGridAdapter.setList(getHomeData(requestCode));
                calGridAdapter.notifyDataSetChanged();
                break;
            case 2:
                conGridAdapter.setList(getHomeData(requestCode));
                conGridAdapter.notifyDataSetChanged();
                break;
            case 3:
                favGridAdapter.setList(getFavData());
                favGridAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
    private void FavSection(){
        favBool = sharedPreferences.getBoolean("favSwitch", true);
        if(favBool){
            favLayout.setVisibility(View.VISIBLE);
            if(favGridAdapter.getCount() == 0){
                favDetails.setVisibility(View.VISIBLE);
            }
        }
        else{
            favLayout.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        FavSection();
    }
}
