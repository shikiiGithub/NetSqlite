# NetSqlite
使用java语言实现像使用ADO.NET一样在Android开发中操作Sqlite数据库。(Operate sqlite database for devloping android apps with this library which simply implement ado.net by disgusting java, so you can operate sqlite datebase in .net style )

## 相互学习(任意相关问题)：qq:1548492402,shikii@outlook.com

## release 版的jar可以直接下载使用 

## key-Value 特性

## 如何使用（How to use ?)
        SqliteDB db = new SqliteDB() ;
        boolean bConnected = db.Connect("your database file path") ;  
        if(bConnected)
          {
         // 默认表名称是“App_Extension_Data_Table"(The default table name is App_Extension_Data_Table)
          //使用默认表名写入或者更改单个键值对  （Write or update Key-Value into default table）
          db.Write("myKey","MyValue");
         //指定表名写入或者更改单个键值对   （Write or update Key-Value into  specific table ）
          db.Write("your table name","myKey","MyValue");
         //写入多个键值对  （Write Multiple Key-Value）
         //db.WriteArray();
        
        //读取用FetchValue  （Read Key-Value from default table）
         db.FetchValue("myKey") ;
       //读取用FetchValue  （Read Key-Value from  specific table）
         db.FetchValue("your table name","myKey") ;
         //ExecuteScalar
         String str =db.UniqueResult("select count(name) from tableName") ;
         int count =  0 ;
            if(str != null)
                count  = Integer.parseInt(str) ;
         //使用sql 语句得到一张查询表 （get a table） 
          DataTable dt =  db.ProvideTable(sql) ;
        //通过指定行，列得到指定值 （get unique result from a table ）
          dt.IndexOf(  nIndex_Row,   nIndex_Col) ;
        }
