package sg.edu.nus.imovin.Database;

import android.content.Context;

import sg.edu.nus.imovin.GreenDAO.DaoMaster;

public class DataBaseHelper extends DaoMaster.OpenHelper{
    private static final String DATABASE_NAME = "ImovinDatabase.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME);
    }
}
