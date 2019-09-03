package shikii.Data;

import shikii.Data.Common.DataTable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shikii on 2018/4/22.
 */
public class UnitDB extends  DBRoot {


    protected  final String LOCALDBNAME = "shikii";
    protected  final String TABLENAME = "App_Extension_Data_Table";
    public final String EMBEDDEDTABLEDEF = "Name nvarchar(128) primary key,Val nvarchar(512) not null";

    public final String DefaultTable = TABLENAME ;


    protected boolean bConnected = false;

    public  final String SPLITMARK = "^" ;

    public final String FIELD_VALUE = "Val";
    public final String FIELD_NAME = "Name";
    protected final String BADCONNECTION = "未能连接本地数据库";
    int nError = -99999;

    public String[] GetNameColumnValues(String strTableName)
    {
        try
        {
          DataTable   dtx = this.ProvideTable(String.format("select Name from %s; ", strTableName) );
            String[] ColumnsValue = new String[dtx.Rows ];
            for (int i = 0; i < ColumnsValue.length; i++)
            {
                ColumnsValue[i] = dtx.IndexOf(i,0) ;
            }

            return ColumnsValue;
        }
        catch (Exception e)
        {

            return null;
        }




    }

    public void CreateKeyValueTable(String strTableName)
    {

            NewKeyValueTable(strTableName);


    }
    public   void CopyKeyValueTable(String newTableName, String oldTableName)
    {
        CreateKeyValueTable(newTableName);
        this.ExecuteNonQuery(
                String.format("insert into %s SELECT * FROM %s"
                        , newTableName, oldTableName)
                 );
    }
    void NewKeyValueTable(String strTableName)
    {
        this.NewTable(strTableName, EMBEDDEDTABLEDEF);
    }
    void NewSpecialKeyValueTable(String strTableName)
    {
        this.NewTable(strTableName, "Name varchar(128) primary key, val varchar(512) not null");
    }

    public void GetAllTableNames()
    {
     SQLiteGetAllTableNames();
    }
    void SQLiteGetAllTableNames()
    {

        try
        {
            DataTable  dt = this.ProvideTable("select name from sqlite_master where type='table' order by name;" );

            for (int i = 0; i < dt.Rows; i++)
            {
                String strTableName = dt.IndexOf(i,0)  ;
                //if (!strTableName.Contains("pg_") && !strTableName.Contains("sql_"))
                this.lst_TableNames.add(strTableName);
            }
        }
        catch(Exception e)
        {
            HandleError(e, null   );
        }



    }

    public void Write(String strTableName, String strName, String strValue)
    {
        if (!bConnected)
        {
            HandleError(null, null   );
            return;
        }
        if (IsExistRecord(strName, strTableName))
            this.Update(strTableName, String.format("Val='%s' Where Name='%s' ", strValue, strName));
        else
            this.NewRecord(strTableName, String.format("'%s','%s'", strName, strValue));

    }
    public String FetchValue(String strTableName ,String strLabelName)
    {
        if (!bConnected)
        {
            HandleError(null, null );
            return null;
        }

       /* if (Global_DBDataNotify != null)
        {

            Global_DBDataNotify(null, NotifyMessages.NoControlRead, strTableName, strLabelName);
        }*/
        return this.UniqueResult(
                String.format("select Val from %s where Name='%s'; "
                        , strTableName, strLabelName));

    }
    public void Write(String strName, String strValue)
    {
        Write(this.DefaultTable, strName, strValue);
    }
    public String FetchValue(String strLabelName)
    {
        if (!bConnected)
        {
            HandleError(null, null );
            return null;
        }

        return this.UniqueResult(
                String.format("select Val from %s where Name='%s'; "
                        , DefaultTable, strLabelName));
    }






    public <T> void WriteArray(String strTableName ,String strName, T[] tArr)
    {

        for (int i = 0; i < tArr.length; i++)
        {
            this.AppendItem( strTableName, strName, tArr[i].toString());
        }

    }
    public <T> void WriteArray(String strName, T[] tArr)
    {
        for (int i = 0; i < tArr.length; i++)
        {
            this.AppendItem( DefaultTable, strName, tArr[i].toString());
        }


    }
    public void AppendItem(String strLabelName, String obj)
    {
        AppendItem(DefaultTable, strLabelName, obj);
    }
    public void AppendItem(String strTableName, String strLabelName, String obj)
    {

        StringBuilder sb = new StringBuilder();
        String[] strArr_Write = null ;
        String[] strArr =   FetchArray(strTableName,strLabelName) ;
        if(obj==null)
            return  ;
        if(strArr==null)
        {
            if(obj==null)
                return ;
            String str = FetchValue(strTableName,strLabelName) ;
            if(str==null||str.equals(""))
            {
                Write(strTableName,strLabelName,obj.toString());
                return ;
            }
            else {
                strArr_Write = new String[]{str, obj};
            }

        }
        else {
            strArr_Write = new String[strArr.length + 1];
            for (int i = 0; i < strArr.length; i++) {
                strArr_Write[i] = strArr[i] ;
            }
            strArr_Write[strArr_Write.length-1] = obj ;
        }
        for (int i = 0; i < strArr_Write.length; i++)
        {
            if (i == 0)
            {
                sb.append(strArr_Write[i] );
            }
            else
                sb.append(String.format("%s%s", SPLITMARK, strArr_Write[i] ));
        }
        this.Write(strTableName,strLabelName, sb.toString());

        sb.delete(0,sb.length()) ;
        sb = null ;

    }
    public void AppendUniqueItem(String strTableName, String strLabelName, String obj)
    {
        String[] strArr_Write = null;
        String[] strArr = FetchArray(strTableName, strLabelName);
        if (obj == null)
            return;
        if (strArr == null)
        {
            if (obj == null)
                return;
            String str = FetchValue(strTableName, strLabelName);
            if (str == null || str.equals(""))
            {
                Write(strTableName, strLabelName, obj.toString());
                return;
            }
            else
            {
                strArr_Write = new String[] {str, obj};
            }
        }
        else
        {
            strArr_Write = new String[strArr.length + 1];
            for (int i = 0; i < strArr.length; i++)
            {
                strArr_Write[i] = strArr[i];
            }
            strArr_Write[strArr_Write.length - 1] = obj;
        }
        StringBuilder sb = new StringBuilder();
        List<String> lst_Arr = new ArrayList<>();
        //  String str = obj.ToString() ;
        if (strArr == null)
        {
            for (int i = 0; i < strArr_Write.length; i++)
            {
                if (lst_Arr.contains(strArr_Write[i]))
                    continue;
                lst_Arr.add(strArr_Write[i]);
            }
        }
        else
        {
            for (int j = 0; j < strArr_Write.length; j++)
            {
                if (!lst_Arr.contains(strArr_Write[j]))
                    lst_Arr.add(strArr_Write[j]);
            }
        }


        for (int i = 0; i < lst_Arr.size(); i++)
        {
            if (i == 0)
            {
                sb.append(lst_Arr.get(i) );
            }
            else
                sb.append(String.format("%s%s", SPLITMARK, lst_Arr.get(i)));
        }
        this.Write(strTableName, strLabelName, sb.toString());
        sb.delete(0, sb.length());
        sb = null;
    }
    public void AppendUniqueItem( String strLabelName, String obj)
    {
        AppendUniqueItem(DefaultTable,obj);
    }
    public String[] FetchArray(String strLabelName)
    {

               /* String str = FetchValue(strLabelName);
                if (str.Contains(SPLITMARK.ToString()))
                    return str.Split(new char[] { SPLITMARK });
                else
                    return null;*/
        return FetchArray(DefaultTable, strLabelName);
    }
    public String[] FetchArray(String strTableName, String strLabelName)
    {
        String str = FetchValue(  strTableName,strLabelName);
        if(str== null)
            return null ;
        else  {
            if (str.contains(SPLITMARK))
                try {
                    String [] str_  = str.split("\\^");
                    return str_ ;
                }
                catch ( Exception e)
                {
                    int i = 56  ;
                    return null ;
                }
            else
                return null;
        }
    }



    protected boolean IsExistRecord(String strName, String Tablename)
    {
        String temp = this.UniqueResult(
                String.format("select count(*) from %s where Name='%s'"
                        , Tablename, strName
                ));
        try
        {
            if (Integer.parseInt(temp) > 0)
                return true;
            else
                return false;
        }
        catch (Exception e)
        {

            return false;
        }

    }
    public boolean TableExists(String strTableName)
    {
        strTableName = strTableName.trim() ;
        boolean bIsTableExists = false ;
         GetAllTableNames() ;
        for (int i = 0; i <lst_TableNames.size() ; i++) {
            if(lst_TableNames.get(i).equals(strTableName))
            {
                bIsTableExists = true ;
                break;
            }
        }
        return bIsTableExists ;
    }
}
