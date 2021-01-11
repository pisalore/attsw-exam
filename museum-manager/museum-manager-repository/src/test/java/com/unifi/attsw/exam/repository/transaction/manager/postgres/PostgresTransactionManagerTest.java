package com.unifi.attsw.exam.repository.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresMuseumRepository;

public class PostgresTransactionManagerTest {

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");
	private static final UUID MUSEUM_ID_2 = UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851");

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final int NUM_CONSTANT1 = 10;
	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

	private MuseumRepository postgresMuseumRepository;
	private ExhibitionRepository postgresExhibitionRepository;
	private PostgresTransactionManager transactionManager;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
	}

	@Before
	public void setUp() throws Exception {
		entityManager = sessionFactory.createEntityManager();

		transactionManager = new PostgresTransactionManager(entityManager);
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		postgresExhibitionRepository = new PostgresExhibitionRepository(transactionManager.getEntityManager());

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

	}

	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		transactionManager.doInTransactionMuseum(museumRepository -> {
			return museumRepository.addMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
	}

	@Test
	public void testInsertNullMuseumInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			return museumRepository.addMuseum(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testInsertNullExhibitionInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testInsertNewExhibitionWithoutMuseumShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertExhibitionWithInvalidMuseumIdShouldRollbackandThrow() {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibition.setMuseumId(invalidUUID);
			museumRepository.addMuseum(museum);
			exhibitionRepository.addNewExhibition(exhibition);
			return null;
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertNewExhibitionTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition.setMuseumId(museum.getId());
			exhibitionRepository.addNewExhibition(exhibition);
			return null;
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition)
				.extracting(Exhibition::getMuseumId).contains(museum.getId());
	}

	@Test
	public void testInsertSeveralExhibitionWithWrongMuseumIdInPostgresDatabaseShouldRollbackAndThrow()
			throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition1.setMuseumId(museum.getId());
			exhibition2.setMuseumId(invalidUUID);
			exhibitionRepository.addNewExhibition(exhibition1);
			exhibitionRepository.addNewExhibition(exhibition2);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testInsertSeveralExhibitionToOneMuseumInPostgresDatabaseCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition1.setMuseumId(museum.getId());
			exhibition2.setMuseumId(museum.getId());
			exhibitionRepository.addNewExhibition(exhibition1);
			exhibitionRepository.addNewExhibition(exhibition2);
			return null;
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2)
				.extracting(Exhibition::getMuseumId).contains(museum.getId());
	}

	@Test
	public void testInsertExhibitionWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		populateDatabase();
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2).extracting(Exhibition::getName)
				.contains(exhibition.getName());
	}

	@Test
	public void testInsertMuseumWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		populateDatabase();
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
			return museumRepository.addMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getName)
				.contains(museum.getName());
	}

	@Test
	public void testDeleteNullExhibitionInPostgresDatabaseShouldThrowAndRollback() {
		populateDatabase();
		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2);
	}

	@Test
	public void testDeleteRemovedExhibitionInPostgresDatabaseShouldThrowAndRollback() throws RepositoryException {
		populateDatabase();
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.doesNotContain(exhibition.getId()).contains(EXHIBITION_ID_2);
	}

	@Test
	public void testDeleteExhibitionInPostgresDatabaseCommit() throws RepositoryException {
		populateDatabase();
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.containsExactly(EXHIBITION_ID_2);

	}

	@Test
	public void testDeleteNullMuseumInPostgresDatabaseShouldThrowAndRollback() {
		populateDatabase();
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
			museumRepository.deleteMuseum(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2);
	}

	@Test
	public void testDeleteMuseumReferencedMuseumByExhibitionInPostgresDatabaseShouldThrowAndRollback()
			throws RepositoryException {
		populateDatabase();
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
			museumRepository.deleteMuseum(museum);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getId)
				.contains(MUSEUM_ID_1);
	}

	@Test
	public void testCorrectlyDeleteMuseumWithNoExhibitionsInPostgresDatabaseCommit() throws RepositoryException {
		populateDatabase();
		Museum museumToBeDeleted = postgresMuseumRepository.findMuseumById(MUSEUM_ID_2);
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);

		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		transactionManager.doInTransactionMuseum((museumRepository) -> {
			museumRepository.deleteMuseum(museumToBeDeleted);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2)
				.extracting(Exhibition::getMuseumId).contains(MUSEUM_ID_1);

		assertThat(postgresMuseumRepository.findAllMuseums()).contains(museum);
	}

	@Test
	public void testCorrectlyDeleteMuseumReferencedByExhibitionsInPostgresDatabaseCommit() throws RepositoryException {
		populateDatabase();
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(exhibition1);
			exhibitionRepository.deleteExhibition(exhibition2);
			museumRepository.deleteMuseum(museum);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1).extracting(Museum::getId)
				.contains(MUSEUM_ID_2);

	}

	@Test
	public void testUpdateNullMuseumInPostgresDatabaseShouldThrowAndRollback() {
		populateDatabase();
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			return museumRepository.updateMuseum(null);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testUpdateRemovedMuseumInPosgresDatabaseShouldThrowAndRollback() {
		populateDatabase();
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			museumRepository.deleteMuseum(museum);
			return museumRepository.updateMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getId).contains(MUSEUM_ID_1,
				MUSEUM_ID_2);

	}

	@Test
	public void testUpdateMuseumInPostgresDatabaseCommit() throws RepositoryException {
		populateDatabase();
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		transactionManager.doInTransactionMuseum((museumRepository) -> {
			museum.setOccupiedRooms(5);
			museum.setRooms(50);
			return museumRepository.updateMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findMuseumById(MUSEUM_ID_1).getOccupiedRooms()).isEqualTo(5);
		assertThat(postgresMuseumRepository.findMuseumById(MUSEUM_ID_1).getTotalRooms()).isEqualTo(50);
	}

	@Test
	public void testUpdateNullExhibitionInPostgresDatabaseShouldRollbackAndThrow() {
		populateDatabase();
		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.updateExhibition(null);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testUpdateRemovedExhibitionInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		populateDatabase();
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return exhibitionRepository.updateExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2).extracting(Exhibition::getId)
				.contains(EXHIBITION_ID_1, EXHIBITION_ID_2);
	}

	@Test
	public void testUpdateExhibitionInPostgresDatabaseCommit() throws RepositoryException {
		populateDatabase();
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibition.setBookedSeats(5);
			exhibition.setTotalSeats(50);
			return exhibitionRepository.updateExhibition(exhibition);
		});

		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getBookedSeats()).isEqualTo(5);
		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getTotalSeats()).isEqualTo(50);
	}

	@After
	public void after() {
		entityManager.clear();
	}

	@AfterClass
	public static void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	public Exhibition createExhibition(String exhibitionName, int numOfSeats) {
		return new Exhibition(exhibitionName, numOfSeats);

	}

	/*
	 * Utility method to populate database with data
	 */

	private void populateDatabase() {
		entityManager.getTransaction().begin();
		entityManager
				.createNativeQuery("INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)"
						+ "VALUES ( 'b433da18-ba5a-4b86-92af-ba11be6314e7' , 'museum1_test', 0, 10);")
				.executeUpdate();
		entityManager
				.createNativeQuery("INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)"
						+ "VALUES ( '94fe3013-9ebb-432e-ab55-e612dc797851' , 'museum2_test', 0, 10);")
				.executeUpdate();

		entityManager
				.createNativeQuery("INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)"
						+ "VALUES ('49d13e51-2277-4911-929f-c9c067e2e8b4', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition1_test', 100, 0);")
				.executeUpdate();
		entityManager
				.createNativeQuery("INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)"
						+ "VALUES ('b2cb1474-24ff-41eb-a8d7-963f32f6822d', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition2_test', 100, 0);")
				.executeUpdate();

		entityManager.getTransaction().commit();
	}

}
