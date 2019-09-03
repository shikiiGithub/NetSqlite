package shikii.Data.SQLite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import shikii.Data.Common.DataTable;
import shikii.Data.Common.DbCommand;
import shikii.Data.Common.DbConnection;

/**
 * Created by shikii on 2018/4/22.
 */
public class SQLiteConnection extends DbConnection {
    SQLiteDatabase mdb ;
    //ConnectionString = 路径
    @Override
    public boolean Open() {
        try {

            mdb = SQLiteDatabase.openOrCreateDatabase(ConnectionString, null);
        }
        catch (Exception e)
        {
            Log.e("数据库sqlite错误:", "是否存储空间不足或者未为模拟器增加外置存储空间？");
        }
        if(mdb==null)
        return false;
        else {
            this.DBObject  = mdb ;
            return true;
        }
    }

    @Override
    public boolean Close() {
        try {
            mdb.close();
            return true;
        } catch (Exception e) {
           return false ;
        }
    }

    @Override
    public boolean Dispose() {
        return true ;
    }
}
