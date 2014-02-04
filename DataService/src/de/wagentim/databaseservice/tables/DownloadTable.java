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
	
	/**
	 * Persist entity
	 * <p>
	 * If the entity not exist before, then persist the whole object. Otherwise just update some information
	 */
	@Override
	public synchronized boolean persist( Object entity ) {
		
		if( !(entity instanceof DownloadFile ) )
		{
			log.log("Persis Object Type is error. Prefer " + DownloadFile.class.getName() + ", but get" + entity.getClass().getName(), Log.LEVEL_CRITICAL_ERROR);
			return false;
		}
		
		if( isAlreadyExist((DownloadFile) entity) )
		{
			updateValue((DownloadFile) entity);
			
		}else
		{
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		}
		
		return true;
	}
	
	private void updateValue(DownloadFile entity) {
		
		DownloadFile savedFile = em.find(DownloadFile.class, entity.getID());
		
		if( null == savedFile )
		{
			log.log("Cannot file saved Entity: " + entity.getID(), Log.LEVEL_CRITICAL_ERROR);
			
			return;
		}
		em.getTransaction().begin();
		savedFile.setUnFinishedBlock(entity.getUnFinishedBlock());
		em.getTransaction().commit();
	}

	private boolean isAlreadyExist(final DownloadFile entity) {
		
		return entity.getID() <= -1 ? false : true;
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
			if( !isAlreadyExist(df) )
			{
				em.persist(df);
			}else
			{
				DownloadFile tmp = em.find(DownloadFile.class, df.getID());
				
				if( null == tmp )
				{
					log.log("Cannot file saved Entity: " + df.getID(), Log.LEVEL_CRITICAL_ERROR);
					continue;
				}
				
				tmp.setUnFinishedBlock(df.getUnFinishedBlock());
			}
		}
		
		em.getTransaction().commit();
		
		return true;
	}
	
	
}
