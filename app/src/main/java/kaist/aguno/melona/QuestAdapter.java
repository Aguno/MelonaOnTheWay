package kaist.aguno.melona;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class QuestAdapter  extends BaseAdapter{
    private Context context;
    private ArrayList<QuestItem> QuestItemList = new ArrayList<QuestItem>();
    private ViewHolder viewHolder;

    class ViewHolder {
        TextView titleTextView;
        TextView detailTextView;
        TextView rewardTextView;
    }

    public QuestAdapter(){
    }

    public QuestAdapter(ArrayList<HashMap<String, String>> list, String[] keys){
/*
        Iterator<HashMap<String, String>> iter = list.iterator();
        while (iter.hasNext()){
            HashMap<String, String> elem = iter.next();
            addItem(elem.get(keys[0]), elem.get(keys[1]), elem.get(keys[2]));
        }
        */
        addItem(keys[0], keys[1], keys[2]);
    }

    @Override
    public int getCount(){ return QuestItemList.size();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.quest_item, parent, false);
            //convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.detailTextView = (TextView) convertView.findViewById(R.id.where);
            viewHolder.rewardTextView = (TextView) convertView.findViewById(R.id.reward);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        QuestItem QuestItem = QuestItemList.get(position);
        viewHolder.titleTextView.setText(QuestItem.getTitle());
        viewHolder.detailTextView.setText(QuestItem.getDetail());
        viewHolder.rewardTextView.setText(QuestItem.getReward());



        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public QuestItem getItem(int position) {
        return QuestItemList.get(position);
    }

    public void addItem( String title, String detail, String reward) {
        QuestItem item = new QuestItem();

        item.setTitle(title);
        item.setDetail(detail);
        item.setReward(reward);

        QuestItemList.add(item);
    }



}