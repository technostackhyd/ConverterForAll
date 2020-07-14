package in.technostack.projects.converterforall.ViewAdapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import in.technostack.projects.converterforall.R;

public class ListViewAdapter extends ArrayAdapter<String> {
    Activity context;
    String names[];
    String values[];
    public ListViewAdapter(Activity context,String[] names,String[] values){
            super(context, R.layout.list_item,names);
            this.context=context;
            this.names=names;
            this.values=values;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row==null){
            LayoutInflater inflater = (context).getLayoutInflater();
            row = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.Name =  row.findViewById(R.id.name);
            holder.Value =  row.findViewById(R.id.value);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        holder.Name.setText(names[position]);
        holder.Value.setText(values[position]);
        return row;
    }

    static class ViewHolder {
        TextView Name;
        TextView Value;
    }
}
