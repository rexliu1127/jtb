package io.grx.auth.entity.requestModel;

public class RequestContactNameListModel {

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str.replace("[","{").replace("]","}");
    }

    @Override
    public String toString() {
        return "RequestContactNameListModel{" +
                "str='" + str + '\'' +
                '}';
    }
}
