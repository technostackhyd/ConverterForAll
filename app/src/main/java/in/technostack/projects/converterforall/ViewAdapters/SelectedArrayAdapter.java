package in.technostack.projects.converterforall.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import in.technostack.projects.converterforall.R;

public class SelectedArrayAdapter extends ArrayAdapter implements Filterable {

    private Context context;
    private int layoutResourceId;
    private int NUMBER;
    private ArrayList<CurrencyModel> list = new ArrayList();
    private ArrayList<CurrencyModel> filterList=new ArrayList<>();
    public SelectedArrayAdapter(Context context, int layoutResourceId, ArrayList<CurrencyModel> data,int num) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = data;
        this.filterList=data;
        this.NUMBER=num;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        final CurrencyModel item = filterList.get(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.currName =  row.findViewById(R.id.currSelectName);
            holder.currFlag =  row.findViewById(R.id.currSelectFlag);
            holder.currCheck = row.findViewById(R.id.currCheck);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.currFlag.setImageResource(item.getCurrFlag());
        holder.currName.setText(item.getCurrName());
        holder.currCheck.setChecked(item.isSelected());
        holder.currCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb=(CheckBox) view;
                int count=0;
                if (cb.isChecked()) {
                    for (CurrencyModel obj: list) {
                        if (obj.isSelected())
                            count++;
                    }
                    if (count>=NUMBER) {
                        Toast.makeText(getContext(), "You can select only " + NUMBER, Toast.LENGTH_LONG).show();
                        cb.setChecked(false);
                    }
                    else
                        list.get(list.indexOf(item)).setSelected(cb.isChecked());
                }
                else {
                    list.get(list.indexOf(item)).setSelected(cb.isChecked());
                }
            }
        });
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
        CheckBox currCheck;
    }

    public ArrayList<CurrencyModel> getList(){
        return list;
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



