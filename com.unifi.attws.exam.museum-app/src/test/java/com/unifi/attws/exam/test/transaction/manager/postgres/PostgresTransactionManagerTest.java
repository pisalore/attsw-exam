package com.unifi.attws.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.test.repository.postgres.MuseumPostgresRepositoryTest;
import com.unifi.attws.exam.transaction.manager.postgres.PostgresTransactionManager;
public class PostgresTransactionManagerTest {

	PostgresMuseumRepository postgresMuseumRepository;
	PostgresTransactionManager transactionManager;	
	
	private static final Logger LOGGER = LogManager.getLogger(MuseumPostgresRepositoryTest.class);

	@Before
	public void setUp() throws Exception {
		transactionManager = new PostgresTransactionManager();
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());

	}
	
	@Test
	public void testInsertNewMuseumInPostgresDatabseTransactionally() {
		Museum museum = createTestMuseum("Uffizi", 10);
		PostgresMuseumRepository repository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		transactionManager.doInTransaction(
			repoInstance -> { return repoInstance.addMuseum(museum); },
			repository);
		
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
	}
	
	
	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}
	

}
