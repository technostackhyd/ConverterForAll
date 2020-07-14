package in.technostack.projects.converterforall.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import in.technostack.projects.converterforall.R;

public class CurrArrayAdapter extends ArrayAdapter implements Filterable {
    private Context context;
    private int layoutResourceId;
    private ArrayList<CurrencyModel> list = new ArrayList();
    private ArrayList<CurrencyModel> filterList=new ArrayList<>();
    public CurrArrayAdapter(Context context, int layoutResourceId, ArrayList<CurrencyModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = data;
        this.filterList=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.currName =  row.findViewById(R.id.currName);
            holder.currFlag =  row.findViewById(R.id.currFlag);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        CurrencyModel item = filterList.get(position);
        holder.currFlag.setImageResource(item.getCurrFlag());
        holder.currName.setText(item.getCurrName());
        return row;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    static class ViewHolder {
        TextView currName;
        ImageView currFlag;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        final ArrayList<CurrencyModel> l=new ArrayList<>();
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = list;
                    results.count = list.size();
                }
                else {
                    for (CurrencyModel data: list){
                        String name=data.getCurrName();
                        String charSeq=charSequence.toString();
                        if (name.toLowerCase().contains(charSeq.toLowerCase())){
                            l.add(data);
                        }
                    }
                    results.values=l;
                    results.count=l.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults!=null) {
                    filterList=null;
                    filterList = (ArrayList<CurrencyModel>) filterResults.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
