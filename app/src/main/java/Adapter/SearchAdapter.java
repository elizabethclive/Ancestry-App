package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.familymap.EventActivity;
import com.example.familymap.PersonActivity;
import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import Model.Model;
import Model.Event;
import Model.Person;
import Util.ListItem;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private ArrayList<ListItem> matches;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView searchLine1, searchLine2;
        private ImageView searchItemImage;
        private RelativeLayout searchListItem;

        public MyViewHolder(View view) {
            super(view);
            searchLine1 = (TextView) view.findViewById(R.id.lblListItem);
            searchLine2 = (TextView) view.findViewById(R.id.lblListItem2);
            searchItemImage = (ImageView) view.findViewById(R.id.icon);
            searchListItem = view.findViewById(R.id.banner);
            searchListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ListItem item = matches.get(getAdapterPosition());
            Drawable mapIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.male_icon).sizeDp(40);
            if (item.getSecondLine().length() > 0) {
                Event event = Model.getInstance().getEventFromId(item.getId());
                Model.getInstance().setSelectedEvent(event);
                Model.getInstance().setSelectedPerson(Model.getInstance().getPersonFromId(event.getPersonID()));
                Intent intent = new Intent(context, EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Person person  = Model.getInstance().getPersonFromId(item.getId());
                Model.getInstance().setSelectedPerson(person);
                Intent intent = new Intent(context, PersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            this.getAdapterPosition();
        }

        public void bindData(final ListItem listItem) {
            searchLine1.setText(listItem.getFirstLine());
            searchLine2.setText(listItem.getSecondLine());
            searchItemImage.setImageDrawable(listItem.getIcon());
        }
    }


    public SearchAdapter(ArrayList<ListItem> listItems, Context context) {
        this.matches = listItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        // changed R.id.list_item to viewType
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem listItem = matches.get(position);
//        holder.
        ((MyViewHolder) holder).bindData(matches.get(position));
        // do stuff
//        holder.searchListItem.s

    }


    @Override
    public int getItemCount() {
        return matches.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.list_item;
    }
}