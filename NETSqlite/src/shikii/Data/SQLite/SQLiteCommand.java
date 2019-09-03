package shikii.Data.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import shikii.Data.Common.DataTable;
import shikii.Data.Common.DbCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shikii on 2018/4/22.
 */
public class SQLiteCommand extends DbCommand {
     SQLiteDatabase mdb ;
    @Override
    public void ExecuteNonQuery() {
        CheckConnection() ;
        mdb.execSQL(CommandText);
    }
    @Override
    public void ExecuteNonQuery(Object ... BinArgs) {
        CheckConnection() ;
        mdb.execSQL(CommandText,BinArgs);
    }
    void CheckConnection()
    {
        if(Connection != null)
            if(mdb == null)
                mdb = (SQLiteDatabase) Connection.DBObject ;
    }
    Cursor RawQuery(String sql)  {
        CheckConnection() ;
        Cursor obj;
        try {
            obj = mdb.rawQuery(sql, null);
            return  obj ;
        }
        catch (Exception e)
        {
           // Console.WriteLine(e.toString());
            return null ;
        }

    }
    @Override
    public DataTable ExecuteReader() {

        SQLiteDataTable dt = new SQLiteDataTable(this.RawQuery(CommandText));
        return dt;
    }

    @Override
    public Object ExecuteScalar() {
       DataTable dt =  ExecuteReader() ;
        if(dt.Rows>0)
            return dt.IndexOf(0,0) ;
        else
        return null;
    }




}
