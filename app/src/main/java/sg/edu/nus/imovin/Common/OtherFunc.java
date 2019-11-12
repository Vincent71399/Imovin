package sg.edu.nus.imovin.Common;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import sg.edu.nus.imovin.Database.DBFunctions;
import sg.edu.nus.imovin.Database.DataBaseHelper;
import sg.edu.nus.imovin.System.ImovinApplication;

public class OtherFunc {
    public static DBFunctions GetDBFunction(){
        SQLiteDatabase db = GetDatabase();
        if(db != null) {
            return new DBFunctions(db);
        }else{
            Log.d("TestDBFunction", "Return Null");
            return null;
        }
    }

    public static SQLiteDatabase GetDatabase(){
        SQLiteDatabase database = ImovinApplication.getInstance().getDatabase();
        if (database == null || !database.isOpen()) {
            if(ImovinApplication.getInstance().getUserInfoResponse() != null) {
                DataBaseHelper dbHelper = new DataBaseHelper(ImovinApplication.getInstance(), ImovinApplication.getInstance().getUserInfoResponse().get_id());
                database = dbHelper.getWritableDatabase();
                ImovinApplication.getInstance().setDatabase(database);
            }
        }
        return database;
    }
}
