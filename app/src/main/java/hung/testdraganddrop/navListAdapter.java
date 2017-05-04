package hung.testdraganddrop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by everything on 3/30/2016.
 */
public class navListAdapter extends ArrayAdapter<nav_item> {
    Context context;
    int reslayout;
    List<nav_item> listNavItems;

    public navListAdapter(Context context, int reslayout, List<nav_item> listNavItems) {
        super(context,reslayout, listNavItems);
        this.context = context;
        this.reslayout = reslayout;
        this.listNavItems = listNavItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= View.inflate(context,reslayout,null);
        TextView tvtitle=(TextView)v.findViewById(R.id.title_new);
        ImageView navionView=(ImageView)v.findViewById(R.id.nav_icon);
        nav_item navList=listNavItems.get(position);
        tvtitle.setText(navList.getTitle());
        navionView.setImageResource(navList.getResIcon());
        return v;
    }
}
