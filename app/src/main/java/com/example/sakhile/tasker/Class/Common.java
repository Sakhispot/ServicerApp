package com.example.sakhile.tasker.Class;

public class Common {
    private static String DB_NAME= "services";
    private static String COLLECTION_NAME= "users";
    public static String API_KEY="zPDv_7eq8LOXDfbnVAvY0EpXV8Pk6RuA";

    public  static String getAddressSingle(User user){
        String baseUlr= String.format("https://api.mlab.com/api/1/databases/%s/collections/%s", DB_NAME, COLLECTION_NAME);
        StringBuilder stringBuilder= new StringBuilder(baseUlr);
        stringBuilder.append("/"+user.getId().getOid()+"?apiKey="+API_KEY);

        return stringBuilder.toString();
    }

    public static String getAddressAPI(){
        String baseUlr= String.format("https://api.mlab.com/api/1/databases/%s/collections/%s", DB_NAME, COLLECTION_NAME);
        StringBuilder stringBuilder= new StringBuilder(baseUlr);
        stringBuilder.append("?apiKey="+API_KEY);

        return stringBuilder.toString();
    }
}
