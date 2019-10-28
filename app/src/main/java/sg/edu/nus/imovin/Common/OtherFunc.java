package sg.edu.nus.imovin.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import sg.edu.nus.imovin.Database.DBFunctions;
import sg.edu.nus.imovin.Database.DataBaseHelper;
import sg.edu.nus.imovin.System.ImovinApplication;

public class OtherFunc {
    public static DBFunctions GetDBFunction(){
        SQLiteDatabase db = GetDatabase(ImovinApplication.getInstance());
        if(db != null) {
            return new DBFunctions(db);
        }else{
            Log.d("TestDBFunction", "Return Null");
            return null;
        }
    }

    public static SQLiteDatabase GetDatabase(Context context){
        SQLiteDatabase database = ((ImovinApplication) context.getApplicationContext()).getDatabase();
        if (database == null || !database.isOpen()) {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            database = dbHelper.getWritableDatabase();
            ((ImovinApplication) context.getApplicationContext()).setDatabase(database);
        }
        return database;
    }
}
