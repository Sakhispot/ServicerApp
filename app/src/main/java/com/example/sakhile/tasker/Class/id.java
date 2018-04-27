package com.example.sakhile.tasker.Class;

import com.google.gson.annotations.SerializedName;

public class id {
    @SerializedName("$oid")
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
