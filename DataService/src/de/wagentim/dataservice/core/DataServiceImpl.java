package de.wagentim.dataservice.core;

import de.wagentim.databaseservice.tables.DownloadTable;
import de.wagentim.dataservice.export.DataService;
import de.wagentim.dataservice.export.Table;

public class DataServiceImpl implements DataService{
	
	private DownloadTable dTable = null;
	
	
	@Override
	public Table getTable(final int tableName) {
		
		switch( tableName )
		{
			case TABLE_DOWNLOAD:
				
				if( null == dTable )
				{
					dTable = new DownloadTable();
				}
				
				return dTable;
				
		}
		
		return null;
	}
	
}
