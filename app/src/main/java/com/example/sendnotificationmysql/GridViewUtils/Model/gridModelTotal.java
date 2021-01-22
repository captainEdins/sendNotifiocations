package com.example.sendnotificationmysql.GridViewUtils.Model;

public class gridModelTotal {

    String category, total, id;

    public gridModelTotal(String category, String total, String id) {
        this.category = category;
        this.total = total;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
