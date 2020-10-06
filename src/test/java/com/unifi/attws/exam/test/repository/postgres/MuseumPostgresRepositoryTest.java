package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryTest {
	
	MuseumRepository postgresMuseumRepository;

	@Before
	public void setUp() throws Exception {
		postgresMuseumRepository = new PostgresMuseumRepository();

	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();

	}
	
	@Test
	public void testAddNewMuseumToPostgresDB() {
		Museum museum = createMuseum("MoMa", 10);
		postgresMuseumRepository.addMuseum(museum);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		
	}
	
	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThat(postgresMuseumRepository.retrieveMuseumById(new Long(1))).isNull();
	}
	
	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		Museum museum1 = createMuseum("Pompidou", 50);
		Museum museum2 = createMuseum("Louvre", 10);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.addMuseum(museum2);
		assertThat(postgresMuseumRepository.retrieveMuseumById(new Long(1))).isEqualTo(museum1);
	}
	
	
	/*
	 * Museum utility
	 */
	
	public Museum createMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}
	

}
