package com.bearing_price.view.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bearing_price.R;
import com.bearing_price.data.model.Price_dto;

import java.util.List;


public class Product_adapter extends ArrayAdapter<Price_dto> {
    private final Context context;
    protected List<Price_dto> price_list;
    private final int resource;
    private LayoutInflater mLayoutInflater;
    private Price_dto dto;


    public Product_adapter(Context context, int resource, List<Price_dto> price_list) {
        super(context, resource, price_list);
        this.resource = resource;
        this.price_list = price_list;
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
            holder.brand = (TextView) convertview.findViewById(R.id.brand);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        dto = getItem(i);
        holder.product.setText(dto.getProduct());
        holder.price.setText("₹ " + dto.getPrice());
        holder.brand.setText(dto.getBrand());
        return convertview;
    }

    public void setData(List<Price_dto> list) {
        this.price_list = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView product, price, brand;
    }

}

