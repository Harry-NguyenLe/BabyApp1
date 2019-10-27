package controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import model.Child;
import model.Route;

public class DBHelper {
    String DATABASE_NAME = "Baby.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase db = null;

    Context context;

    public DBHelper(Context context) {
        this.context = context;

        processSQLite();
    }

    private void processSQLite() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDatabaseFromAsset();
                Toast.makeText(context, "Copy successful !!!", Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void CopyDatabaseFromAsset() {
        try {
            AssetManager assetManager = context.getAssets();// Access to asset folder
            InputStream databaseInputStream = assetManager.open(DATABASE_NAME); //open file database using input stream


            String outputStream = getPathDatabaseSystem();

            File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!file.exists()) {
                file.mkdir();
            }

            //Truyền vào outputStream một đường dẫn đến database để tiến hành đọc file
            OutputStream databaseOutputStream = new FileOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = databaseInputStream.read(buffer)) > 0) {
                databaseOutputStream.write(buffer, 0, length);
            }


            databaseOutputStream.flush();
            databaseOutputStream.close();
            databaseInputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getPathDatabaseSystem() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }


    public ArrayList<Child> getChild() {
        db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("select * from Children", null);

        ArrayList<Child> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            int isChecked = cursor.getInt(3);

            arrayList.add(new Child(id, name, image, isChecked));
        }
        db.close();
        return arrayList;
    }

    public long insertChild(Child child) {
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"INSERT INTO Student(name,address,gender) VALUES(?,?,?)"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", child.getChildName());
        contentValues.put("Image", child.getChildImage());
        contentValues.put("IsChecked", child.getChecked());

        long result = db.insert("Children",
                null,
                contentValues);
        db.close();
        return result;
    }


    public boolean updateChild(Child child) {
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"UPDATE Student SET name = ? and image=? Where id = ?"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", child.getChildName());
        contentValues.put("Image", child.getChildImage());

        db.update("Children", contentValues, "ID=" + child.getId(), null);
        db.close();
        return true;

    }

    public boolean updateChildChecked(Child child) {
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"UPDATE Student SET name = ? and image=? Where id = ?"

        ContentValues contentValues = new ContentValues();
        contentValues.put("IsChecked", child.getChecked());

        db.update("Children", contentValues, "ID=" + child.getId(), null);
        db.close();
        return true;

    }


    public boolean deleteChild(int id) {

        //"Delete From Student where id = ?"
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        db.delete("Children", "ID = " + id, null);
        db.close();
        return true;

    }

    public ArrayList<Route> getRoute() {
        db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("select * from Route", null);

        ArrayList<Route> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String childName = cursor.getString(2);
            String time = cursor.getString(3);
            byte[] icon = cursor.getBlob(5);
            int timeInInteger = cursor.getInt(6);

            arrayList.add(new Route(id, name, childName, time, icon, timeInInteger));
        }
        db.close();
        return arrayList;
    }

    public boolean updateRoute(Route route) {

        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"UPDATE Student SET name = ? and image=? Where id = ?"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", route.getRouteName());
        contentValues.put("ChildName", route.getRouteChildName());
        contentValues.put("Time", route.getRouteTime());
        contentValues.put("IsChecked", route.getCheck());
        contentValues.put("Icon", route.getIcon());
        contentValues.put("TimeLong", route.getTimeInInteger());

        db.update("Route", contentValues, "ID=" + route.getRouteID(), null);
        db.close();
        return true;
    }

    public boolean updateRouteChildName(Route route) {

        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"UPDATE Student SET name = ? and image=? Where id = ?"

        ContentValues contentValues = new ContentValues();
        contentValues.put("ChildName", route.getRouteChildName());
        contentValues.put("Time", route.getRouteTime());

        db.update("Route", contentValues, "ID=" + route.getRouteID(), null);
        db.close();
        return true;
    }

    public long insertRoute(Route route) {
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"INSERT INTO Student(name,address,gender) VALUES(?,?,?)"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", route.getRouteName());
        contentValues.put("ChildName", route.getRouteChildName());
        contentValues.put("Time", route.getRouteTime());
        contentValues.put("IsChecked", route.getCheck());
        contentValues.put("Icon", route.getIcon());

        long result = db.insert("Route",
                null,
                contentValues);
        db.close();
        Log.d("route", contentValues + "");
        return result;
    }

}
