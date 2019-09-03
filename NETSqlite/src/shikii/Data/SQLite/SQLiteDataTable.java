package shikii.Data.SQLite;

import android.database.Cursor;
import android.widget.LinearLayout;

import shikii.Data.Common.DataTable;
import java.util.ArrayList;
import java.util.List;

import static android.database.Cursor.*;

public class SQLiteDataTable extends DataTable {
	public SQLiteDataTable(Cursor obj_Cursor) {

		GetTableWithCursor(obj_Cursor);
	}


	// For shikii.app.Android
	public void GetTableWithCursor(Cursor obj_Cursor) {

			if (!((Boolean) obj_Cursor.moveToFirst()))
				return;

			Columns = (Integer) obj_Cursor.getColumnCount();
			do {
				try {
					for (int i = 0; i < Columns; i++) {
						int nType = obj_Cursor.getType(i) ;
						switch (nType)
						{
						   case  FIELD_TYPE_BLOB: LinearDataCollection.add(  obj_Cursor.getBlob( i));break;
						  	case FIELD_TYPE_FLOAT:LinearDataCollection.add(  obj_Cursor.getFloat( i));break;
							case FIELD_TYPE_INTEGER:LinearDataCollection.add(  obj_Cursor.getInt( i));break;
							case FIELD_TYPE_STRING: LinearDataCollection.add(  obj_Cursor.getString( i));break;
						}


					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			} while ((Boolean)  obj_Cursor.moveToNext());

		Rows = LinearDataCollection.size() / Columns;

	}



	public List<String> GetAllData() {
		List<String> data = new ArrayList<>( );
		for (int i = 0; i < LinearDataCollection.size(); i++) {
			data.add( LinearDataCollection.get(i).toString()) ;
		}
		return data;
	}

	public void SetAllData(List<Object> lst_Data) {
		this.LinearDataCollection = lst_Data;
	}


}
//远程传递DataTable
//	public void GetTableFromBytes(Encoding en, byte [] bytes, int nIndexStart, int nTotalBytes)
//	{
//		int nLastIndexStart = 0 ;
//		char ch_enQ = 5 ;
//		StringBuilder sb = new StringBuilder() ;
//		sb.append(en.GetString(bytes,nIndexStart,nTotalBytes)) ;
//		 int n = sb.length() ;
//
//		for (int i = 0; i < n; i++) {
//			if(sb.charAt(i)==ch_enQ)
//			{
//				this.LinearDataCollection.add(sb.substring(nLastIndexStart,i) );
//				nLastIndexStart = i+1 ;
//			}
//		}
//		this.Rows = Convert.ToInt(this.LinearDataCollection.get(LinearDataCollection.size()-1)) ;
//		this.Columns =Convert.ToInt(sb.substring(nLastIndexStart,n)) ;
//		LinearDataCollection.remove(LinearDataCollection.size()-1) ;
//	}