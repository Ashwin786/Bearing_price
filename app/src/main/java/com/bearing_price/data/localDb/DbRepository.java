package com.bearing_price.data.localDb;

import com.bearing_price.ApplicationContext;
import com.bearing_price.data.Database;
import com.bearing_price.data.model.Price_dto;

import java.util.List;

public class DbRepository {
    private static final DbRepository ourInstance = new DbRepository();
    private final Database db;

    public static DbRepository getInstance() {
        return ourInstance;
    }

    private DbRepository() {
        db = new Database(ApplicationContext.getContext());
    }

    public List<Price_dto> get_recent_search_products() {
        return db.get_recent_search_products();
    }

    public void update_RecentProduct(int id) {
        db.update_recent(id);
    }

    public List<Price_dto> get_search_products(String text) {
        return db.get_search_products(text);
    }
}
