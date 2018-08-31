package com.bearing_price;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 18/5/17.
 */
public class Price_adapter extends ArrayAdapter<Price_dto> {
    private final Context context;
    protected final List<Price_dto> price_list, orginal_list;
    private final int resource;
    private LayoutInflater mLayoutInflater;
    private Price_dto dto;


    public Price_adapter(Context context, int resource, List<Price_dto> price_list) {
        super(context, resource, price_list);
        this.resource = resource;
        this.price_list = price_list;
        orginal_list = new ArrayList<>(price_list);
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return price_list.size();
    }

    @Override
    public Price_dto getItem(int i) {
        return price_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertview == null) {
            holder = new ViewHolder();
            convertview = mLayoutInflater.inflate(resource, viewGroup, false);
            holder.product = (TextView) convertview.findViewById(R.id.product);
            holder.price = (TextView) convertview.findViewById(R.id.price);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        dto = getItem(i);
        holder.product.setText(dto.getProduct());
        holder.price.setText("₹ "+dto.getPrice());
        return convertview;
    }

    private class ViewHolder {
        TextView product, price;
    }

    public void filter(String text) {
        // TODO Auto-generated method stub
        price_list.clear();
        if (text.length() == 0) {
            price_list.addAll(orginal_list);
        } else {
            for (Price_dto dto : orginal_list) {
                if (dto.getProduct().startsWith(text))
                    price_list.add(0,dto);
                else if (dto.getProduct().contains(text)) {
                    price_list.add(dto);
                }
            }
        }

        notifyDataSetChanged();
    }
}
