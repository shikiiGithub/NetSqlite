package shikii.Data;

import shikii.Data.Common.DataTable;
import shikii.Data.Common.DbCommand;
import shikii.Data.Common.DbConnection;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shikii on 2018/4/22.
 */

public abstract class DBRoot
{

    public DbCommand ThisDbCommand ;
    protected boolean bRemoteMode = false;
    public String ErrorReport;

    protected DbConnection conn;
    protected DbCommand cmd;

    protected String DBName;
    protected String strPassword;
    protected String UserName;

    public List<String> lst_TableNames;
     /*protected DbParameter DbPar;
    public string BinaryParam = "@data";*/
    public final String CREATEVIEW = "CREATE VIEW %s AS SELECT %s FROM  %s;";
    public boolean  Status = false ;
    public DBRoot()
    {
        this.lst_TableNames = new ArrayList<>();
    }
    public shikii.Data.Common.DataTable ProvideTable(String sql)
    {
        DataTable dt = null;
        try
        {

            if(dt!= null)
            {
                dt.Dispose();

            }
            dt = new DataTable();
            cmd.CommandText = sql;
            if (!bRemoteMode)
            {
                return  cmd.ExecuteReader();
            }
            else
            {
              //  dt  = RemoteHandle(sql, byt_Operator) as DataTable;
                if (dt != null)
                    Status = true;
                else
                {
                    Status = false;
                }
            }

            this.Status = true;
        }
        catch (Exception e)
        {
           // HandleError(e, cmd, DBOperator.OPERATOR_QUERY_TABLE);
            return null;
        }

        return dt;
    }
    public String UniqueResult(String sql)
    {
        cmd.CommandText = sql;
        String strResult = null;
        try
        {
            if (!bRemoteMode)
            {
                strResult = cmd.ExecuteScalar().toString();
                this.Status = true;

            }
            else
            {
              //  strResult = RemoteHandle(sql, DBOperator.OPERATOR_QUERY_UNIQUE) as String;
                if (strResult != null)
                    this.Status = true;
                else
                    Status = false;
            }
        }
        catch (Exception e)
        {
           // HandleError(e, cmd, DBOperator.OPERATOR_QUERY_UNIQUE);
            return null;
        }
        return strResult;

    }
    public void RemoveRecord(String TableName, String strRequirement)
    {
        try
        {
            cmd.CommandText = String.format("Delete from  %s where %s ;", TableName, strRequirement);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
               // CheckRemote(cmd.CommandText, DBOperator.OPERATOR_DELETE);

            }
        }
        catch (Exception e)
        {
            HandleError(e, cmd);
        }
    }
    public void Update(String TableName, String strColumnAssignAndRequirment)
    {
        try
        {
            cmd.CommandText =
                    String.format
                            ("update  %s set %s",
                                    TableName, strColumnAssignAndRequirment);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
               // CheckRemote(cmd.CommandText, DBOperator.OPERATOR_UPDATE);

            }
        }
        catch (Exception e)
        {
            HandleError(e, cmd);

        }
    }
    public  void NewDB(String DBName)
    {

    try
    {
        cmd.CommandText = String.format("create database %s ;", DBName);
        if (!bRemoteMode)
        {
            cmd.ExecuteNonQuery();
            this.Status = true;

        }
        else
        {
           // CheckRemote(cmd.CommandText, DBOperator.OPERATOR_NEW_DB);

        }
    }
    catch (Exception e)
    {
        HandleError(e, cmd);
    }

}
    public void RemoveDB(String strDBName)
    {
        try
        {
            cmd.CommandText = String.format("DROP database %s ;", strDBName);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
                //CheckRemote(cmd.CommandText, DBOperator.OPERATOR_DROP_DB);

            }
        }
        catch (Exception e)
        {
            HandleError(e, cmd );
        }
    }
    public void NewTable(String tablename,String tableDef)
    {
        try
        {
            cmd.CommandText = String.format("create table %s(%s) ;",
                    tablename,tableDef);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
                //CheckRemote(cmd.CommandText, DBOperator.OPERATOR_NEW_TABLE);

            }
        }
        catch (Exception e)
        {

            HandleError(e, cmd );
        }

    }
    public void NewRecord(String strTableName,String strValue)
    {
        try
        {
            cmd.CommandText = String.format("insert into %s values(%s)", strTableName, strValue);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
              //  CheckRemote(cmd.CommandText, DBOperator.OPERATOR_INSERT);

            }
        }
        catch (Exception e)
        {
            HandleError(e, cmd );
        }

    }
    // strColumnNames such as " age,sex,... "
    public void NewView(String ViewName,  String strTableName,String strColumnNames)
    {
        try
        {
            this.cmd.CommandText =
                   String.format(CREATEVIEW, strColumnNames, strTableName);
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
               // CheckRemote(cmd.CommandText, DBOperator.OPERATOR_NEW_VIEW);

            }
        }
        catch (Exception ex)
        {

            HandleError(ex, cmd);
        }

    }
    public void NewKeyValueView(String ViewName,String strFromTableName,
                               String  strColumnName_Name,String strColumnName_Val)
    {
       String str = String.format("%s as Name,%s as Val",
                strColumnName_Name, strColumnName_Val
        );
        NewView(ViewName, strFromTableName, str);
    }
    public void ExecuteNonQuery(String sql )
    {
        try
        {
            cmd.CommandText = sql;
            if (!bRemoteMode)
            {
                cmd.ExecuteNonQuery();
                this.Status = true;

            }
            else
            {
                //CheckRemote(cmd.CommandText, byt_Operator);

            }
        }
        catch (Exception e)
        {
            HandleError(e, cmd );
        }

    }
    protected void HandleError(Exception e, DbCommand cmm_)
    {


    }




}
