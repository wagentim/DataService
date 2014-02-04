package de.wagentim.databaseservice.tables;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.wagentim.dataservice.export.Table;
import de.wagentim.entity.DownloadFile;
import de.wagentim.qlogger.channel.DefaultChannel;
import de.wagentim.qlogger.channel.LogChannel;
import de.wagentim.qlogger.logger.Log;
import de.wagentim.qlogger.service.QLoggerService;

public class DownloadTable implements Table{
	
	private static final String TABLE_DOWNLOAD = "./database/download.odb";
	private EntityManagerFactory emf = null;
	private EntityManager em = null;
	private LogChannel log = QLoggerService.getChannel(QLoggerService.addChannel(new DefaultChannel("DownloadTable")));

	public DownloadTable() {
		
		emf = Persistence.createEntityManagerFactory(TABLE_DOWNLOAD);
		em = emf.createEntityManager();
	}
	
	@Override
	public synchronized boolean persis(Object entitiy) {
		
		if( !(entitiy instanceof DownloadFile ) )
		{
			log.log("Persis Object Type is error. Prefer " + DownloadFile.class.getName() + ", but get" + entitiy.getClass().getName(), Log.LEVEL_CRITICAL_ERROR);
			return false;
		}
		
		em.getTransaction().begin();
		em.persist(entitiy);
		em.getTransaction().commit();
		
		return true;
	}
	
	public synchronized boolean persis(final List<DownloadFile> entities)
	{
		if( null == entities || entities.isEmpty() )
		{
			log.log("no data to persis", Log.LEVEL_INFO);
			return false;
		}
		
		em.getTransaction().begin();
		
		for( DownloadFile df : entities )
		{
			em.persist(df);
		}
		
		em.getTransaction().commit();
		
		return true;
	}
}
