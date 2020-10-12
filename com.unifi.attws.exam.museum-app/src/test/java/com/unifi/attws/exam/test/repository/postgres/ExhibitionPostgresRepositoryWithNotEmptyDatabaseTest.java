package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;

public class ExhibitionPostgresRepositoryWithNotEmptyDatabaseTest {

	private static final String MUSEUM_1 = "b433da18-ba5a-4b86-92af-ba11be6314e7";
	private static final String EXHIBITION_2 = "b2cb1474-24ff-41eb-a8d7-963f32f6822d";
	private static final String EXHIBITION_1 = "49d13e51-2277-4911-929f-c9c067e2e8b4";

	private ExhibitionRepository postgresExhibitionRepository;
	private static EntityManager entityManager;

	@Before
	public void setUp() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);
	}

	@Test
	public void testFindAllExhibitionsWhenSeveralExhibitionsArePersisted() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_2));

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2);
	}

	@Test
	public void testFindExhibitionByIdWhenSeveralExhibitionsArePersistedButIdDoesNotMatch() {

		assertThat(postgresExhibitionRepository.findExhibitionById(UUID.randomUUID())).isNull();
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenNoMuseumsArePersisted() {

		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(UUID.randomUUID())).isEmpty();
	}

	@Test
	public void testFindExhibitionByMuseumIdWhenGivenIdIsNullShouldThrow() {
		
		assertThatThrownBy(() -> postgresExhibitionRepository.findExhibitionsByMuseumId(null))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessage("Museum ID cannot be null.");
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenMuseumsArePersisted() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_2));

		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(UUID.fromString(MUSEUM_1)))
				.containsExactly(exhibition1, exhibition2);

	}

}
