package shikii.Data.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import shikii.Data.Common.DataTable;
import shikii.Data.Common.DbCommand;
import shikii.Data.UnitDB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SqliteDB extends UnitDB
{
    SQLiteDatabase mdb ;
    boolean InternalConnect(String DBFilePath)
    {

        SqliteDB db = new SqliteDB() ;


        if(  DBFilePath.indexOf(".")==-1)
        {
            File file = new File(DBFilePath);
            if(!file.isDirectory())
            {
                file.mkdirs() ;
            }
            DBFilePath =  DBFilePath+"/shikii.db" ;
        }
        conn = new SQLiteConnection() ;
        conn.ConnectionString = DBFilePath ;
        boolean isOpen =  conn.Open() ;
        if(isOpen)
        {
            ThisDbCommand = new SQLiteCommand();
            ThisDbCommand.Connection = conn;
            this.cmd = ThisDbCommand ;
            return  true ;
        }
        else
        {
            ErrorReport = "未能连接到SQLite数据库" ;

            return false;
        }
    }
    public boolean Connect(String DBFilePath)
    {
        this.bConnected = InternalConnect(DBFilePath) ;

        if( this.bConnected)
        {
            if(!this.TableExists(TABLENAME))
            {
                this.CreateKeyValueTable(TABLENAME);
            }

            return true ;
        }
        else
            return false ;
    }
    //insert into table values(id=1,name='sf',bin=?)    ?表示二进制点位符
    public void NewRecord (String strTableName,String strValue,Object...binArgs)
   {

       try {
           cmd.CommandText = String.format("insert into %s values(%s)", strTableName, strValue);
           if (!bRemoteMode) {
               cmd.ExecuteNonQuery(binArgs);
               this.Status = true;

           } else {
               //  CheckRemote(cmd.CommandText, DBOperator.OPERATOR_INSERT);

           }
       }
       catch (Exception e)
       {
           HandleError(e,cmd);
       }
   }




}
