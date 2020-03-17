package com.bearing_price.view.product;

import com.bearing_price.data.model.Price_dto;

import java.util.List;

public interface ProductNavigator {
    void setAdapter(List<Price_dto> list);

    void setRecentAdapter(List<Price_dto> list);

    void emptyListError();
}
