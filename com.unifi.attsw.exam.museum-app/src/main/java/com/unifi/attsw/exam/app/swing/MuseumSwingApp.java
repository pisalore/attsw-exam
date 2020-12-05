package com.unifi.attsw.exam.app.swing;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;
import com.unifi.attsw.exam.view.swing.ExhibitionSwingView;
import com.unifi.attsw.exam.view.swing.MuseumSwingView;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class MuseumSwingApp implements Callable<Void> {

	@Option(names = { "--database-url" }, description = "Database url connection.")
	private String dbUrl = "jdbc:postgresql://localhost:5432/ATTSW_DB";

	@Option(names = { "--database-user" }, description = "Database user.")
	private String dbUser = "museum_manager";

	@Option(names = { "--database-password" }, description = "Database password.")
	private String dbPassword = "attsw";

	public static void main(String[] args) {
		new CommandLine(new MuseumSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {

			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.url", dbUrl);
			properties.put("javax.persistence.jdbc.user", dbUser);
			properties.put("javax.persistence.jdbc.password", dbPassword);

			EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("real.postgres", properties);
			EntityManager entityManager = sessionFactory.createEntityManager();
			TransactionManager transactionManager = new PostgresTransactionManager(entityManager);
			MuseumManagerService museumManagerService = new MuseumManagerServiceImpl(transactionManager);
			MuseumSwingView museumView = new MuseumSwingView();
			ExhibitionSwingView exhibitionView = new ExhibitionSwingView();
			MuseumController museumController = new MuseumController(museumManagerService, museumView, exhibitionView);
			museumView.setMuseumController(museumController);
			exhibitionView.setMuseumController(museumController);
			museumView.setVisible(true);
			exhibitionView.setVisible(true);
			museumController.getAllMuseums();
		});
		return null;
	}

}
