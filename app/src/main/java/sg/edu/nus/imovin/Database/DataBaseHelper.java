package sg.edu.nus.imovin.Database;

import android.content.Context;

import java.util.Locale;

import sg.edu.nus.imovin.GreenDAO.DaoMaster;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_CREATE_COMMENT;

public class DataBaseHelper extends DaoMaster.OpenHelper{
    private static final String DATABASE_NAME = "ImovinDatabase_%s.db";

    public DataBaseHelper(Context context, String uid) {
        super(context, String.format(
                Locale.ENGLISH, DATABASE_NAME, uid));
    }
}
