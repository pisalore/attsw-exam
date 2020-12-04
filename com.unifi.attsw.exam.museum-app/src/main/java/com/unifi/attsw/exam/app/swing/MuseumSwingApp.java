package com.unifi.attsw.exam.app.swing;

import java.awt.EventQueue;
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

@Command(mixinStandardHelpOptions = true)
public class MuseumSwingApp implements Callable<Void> {
	
	public static void main(String[] args) {
		// TODO: add commands
		new CommandLine(new MuseumSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("real.postgres");
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
//			museumController.getAllExhibitions();
		});
		return null;
	}

}
