package com.unifi.attws.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.postgres.PostgresTransactionManager;
public class PostgresTransactionManagerTest {

	PostgresMuseumRepository postgresMuseumRepository;
	PostgresTransactionManager transactionManager;	
	
	@Before
	public void setUp() throws Exception {
		transactionManager = new PostgresTransactionManager();
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());

	}
	
	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyCommit() {
		Museum museum = createTestMuseum("Uffizi", 10);
		PostgresMuseumRepository repository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		transactionManager.doInTransaction(
			repoInstance -> { return repoInstance.addMuseum(museum); },
			repository);
		
		assertThat(postgresMuseumRepository.findAllMuseums()).contains(museum);
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(3);
	}
	
	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyRollBack() {
		Museum museum1 = postgresMuseumRepository.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		Museum museum2 = new Museum("test3", 20);
		museum2.setId(museum1.getId());
		PostgresMuseumRepository repository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		transactionManager.doInTransaction(
			repoInstance -> { return repoInstance.addMuseum(museum2); },
			repository);
		
		assertThat(postgresMuseumRepository.findAllMuseums()).doesNotContain(museum2);
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2);
	}
	
	
	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}
	

}
