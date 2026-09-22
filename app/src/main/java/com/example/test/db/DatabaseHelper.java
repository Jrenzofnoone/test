package com.example.test.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.test.Order;
import com.example.test.Product;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Auth.db";
    private static final int DATABASE_VERSION = 4;
    //User
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME ="username";
    public static final String COLUMN_PASSWORD = "password";

    /// product
    public static final String TABLE_PRODUCT = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_DESC = "description";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_USERID = "user_id";

    //orders
    public static final String TABLE_ORDERS = "orders";
    public static final String CLM_ORDERS_ID = "id";
    public static final String CLM_ORDERS_BUYER_ID = "buyer_id";
    public static final String CLM_ORDERS_SELLER_ID =  "seller_id";
    public static final String CLM_ORDERS_PRODUCT_ID = "product_id";
    public static final String CLM_ORDERS_QUANTITY = "quantity";
    public static final String CLM_ORDERS_PRICE = "total_price";
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL)";
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCT + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
                COLUMN_PRODUCT_DESC + " TEXT, " +
                COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
                COLUMN_PRODUCT_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_USERID + " INTEGER NOT NULL)";
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " (" +
                CLM_ORDERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLM_ORDERS_BUYER_ID + " INTEGER NOT NULL, " +
                CLM_ORDERS_SELLER_ID + " INTEGER NOT NULL, " +
                CLM_ORDERS_PRODUCT_ID + " INTEGER NOT NULL, " +
                CLM_ORDERS_QUANTITY +" INTEGER NOT NULL, " +
                CLM_ORDERS_PRICE + " REAL NOT NULL)";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public boolean checkUserCredentials(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkUsernameExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean registerUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public int getUserId(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        int userid = -1;
        Cursor cursor = db.rawQuery("SELECT "+COLUMN_ID+ " FROM "+
                 TABLE_USERS+ " WHERE " + COLUMN_USERNAME +" = ?", new String[]{username});
        if (cursor.moveToFirst()){
            userid = cursor.getInt(0);
        }
        cursor.close();
        return userid;
    }
    public String getUsername(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_ID + " =?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()){
            username = cursor.getString(0);
        }
        cursor.close();
        return username;
    }
    public boolean insertProduct(String name, double price, String desc,int quantity, String category, int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_DESC, desc);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_CATEGORY, category);
        values.put(COLUMN_PRODUCT_USERID, userId);
        long result = db.insert(TABLE_PRODUCT, null, values);
        return result != -1;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT, new String[]{});
        while(cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME);
            int priceIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE);
            int descIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC);
            int quanIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY);
            int cateIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY);
            int userIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_USERID);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            double price = cursor.getDouble(priceIndex);
            String desc = cursor.getString(descIndex);
            int quantity = cursor.getInt(quanIndex);
            String category = cursor.getString(cateIndex);
            int user = cursor.getInt(userIndex);
            productList.add(new Product(id, name, price, desc, quantity, category, user));
        }
        cursor.close();
        return productList;
    }
    public List<Product> getUserProducts(int userId) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE " +
                COLUMN_PRODUCT_USERID + " = ? ", new String[]{String.valueOf(userId)});
        while(cursor.moveToNext()){
            int idIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME);
            int priceIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE);
            int descIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC);
            int quanIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY);
            int cateIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY);
            int userIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_USERID);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            double price = cursor.getDouble(priceIndex);
            String desc = cursor.getString(descIndex);
            int quantity = cursor.getInt(quanIndex);
            String category = cursor.getString(cateIndex);
            int user = cursor.getInt(userIndex);

            productList.add(new Product(id, name, price, desc,quantity, category, user));
        }
        cursor.close();
        return productList;
    }

    public boolean placeOrder(int buyerId, int productId, int quantityToBuy) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT " +  COLUMN_PRODUCT_QUANTITY + ", " + COLUMN_PRODUCT_PRICE + ", " + COLUMN_PRODUCT_USERID + " FROM " +
                TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + " =?", new String[]{String.valueOf(productId)});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int currentQuantity = cursor.getInt(0);
        double price = cursor.getDouble(1);
        int sellerId = cursor.getInt(2);
        cursor.close();

        if (currentQuantity < quantityToBuy) {
            return false;
        }

        ContentValues productValues = new ContentValues();
        productValues.put(COLUMN_PRODUCT_QUANTITY, currentQuantity - quantityToBuy);
        db.update(TABLE_PRODUCT, productValues, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

        ContentValues orderValues = new ContentValues();
        orderValues.put(CLM_ORDERS_PRICE, price * quantityToBuy);
        orderValues.put(CLM_ORDERS_QUANTITY, quantityToBuy);
        orderValues.put(CLM_ORDERS_PRODUCT_ID, productId);
        orderValues.put(CLM_ORDERS_SELLER_ID, sellerId);
        orderValues.put(CLM_ORDERS_BUYER_ID, buyerId);

        long result = db.insert(TABLE_ORDERS, null, orderValues);

        return  result!= -1;
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "o." + CLM_ORDERS_ID + ", " +
                "o." + CLM_ORDERS_BUYER_ID + ", " +
                "o." + CLM_ORDERS_SELLER_ID + ", " +
                "o." + CLM_ORDERS_PRODUCT_ID + ", " +
                "o." + CLM_ORDERS_QUANTITY + ", " +
                "o." + CLM_ORDERS_PRICE + ", " +
                "p." + COLUMN_PRODUCT_NAME  + ", " +
                "p." + COLUMN_PRODUCT_DESC  +
                " FROM " + TABLE_ORDERS + " o " +
                " INNER JOIN " + TABLE_PRODUCT  + " p " +
                "ON o." + CLM_ORDERS_PRODUCT_ID + " =p." + COLUMN_PRODUCT_ID +
                " WHERE o." + CLM_ORDERS_BUYER_ID + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(buyerId)});
        while(cursor.moveToNext()){
            int idIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_ID);
            int buyerIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_BUYER_ID);
            int sellerIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_SELLER_ID);
            int productIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_PRODUCT_ID);
            int quantityIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_QUANTITY);
            int priceIndex = cursor.getColumnIndexOrThrow(CLM_ORDERS_PRICE);
            int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME);
            int descIndex = cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC);


            int id = cursor.getInt(idIndex);
            int buyerIdAgain = cursor.getInt(buyerIndex);
            int sellerId = cursor.getInt(sellerIndex);
            int productId = cursor.getInt(productIndex);
            int quantity = cursor.getInt(quantityIndex);
            double price = cursor.getDouble(priceIndex);
            String productName = cursor.getString(nameIndex);
            String productDesc = cursor.getString(descIndex);
            orderList.add(new Order(id,buyerIdAgain,sellerId,productId, quantity, price, productName, productDesc));
        }
        cursor.close();
        return orderList;
    }
}
