package de.wagentim.dataservice.export;


public interface DataService {
	
	public static final int TABLE_DOWNLOAD = 0;
	
	public Table getTable(final int tableName);
	
}
