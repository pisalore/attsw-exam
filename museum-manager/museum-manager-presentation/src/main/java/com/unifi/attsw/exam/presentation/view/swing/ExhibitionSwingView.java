package com.unifi.attsw.exam.presentation.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.unifi.attsw.exam.core.view.ExhibitionView;
import com.unifi.attsw.exam.presentation.controller.swing.MuseumSwingController;
import com.unifi.attsw.exam.repository.model.Exhibition;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class ExhibitionSwingView extends JFrame implements ExhibitionView {

	private transient MuseumSwingController museumSwingController;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField exhibitionTextField;
	private JTextField totalSeatsTextField;
	private JTextField museumNameTextField;
	private JLabel lblMuseum;
	private JTextField findMuseumTextField;
	private JButton btnFind;
	private JLabel lblMuseumName;
	private JButton btnAddExhibition;
	private JScrollPane scrollPaneAllExh;
	private JList<Exhibition> listAllExh;
	private DefaultListModel<Exhibition> allExhibitionsListModel;
	private JButton btnBook;
	private JButton btnDelete;
	private JButton btnFindAll;
	private JLabel lblExhibitionsListFor;
	private JScrollPane scrollPaneMuseumExh;
	private JList<Exhibition> listMuseumExh;
	private DefaultListModel<Exhibition> museumsExhibitionListModel;
	private JLabel lblError;
	private JButton btnMuseumsDashboard;

	/**
	 * Create the frame.
	 */
	public ExhibitionSwingView() {
		setTitle("Exhibitions Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 100, 180, 20, 100, 200 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 2, 30, 2 };
		gblContentPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE, 1.0, 1.0 };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPane.setLayout(gblContentPane);

		JLabel lblExhibition = new JLabel("Exhibition");
		GridBagConstraints gbcLblExhibition = new GridBagConstraints();
		gbcLblExhibition.anchor = GridBagConstraints.WEST;
		gbcLblExhibition.insets = new Insets(0, 0, 5, 5);
		gbcLblExhibition.gridx = 0;
		gbcLblExhibition.gridy = 0;
		contentPane.add(lblExhibition, gbcLblExhibition);

		exhibitionTextField = new JTextField();
		GridBagConstraints gbcExhibitionTextField = new GridBagConstraints();
		gbcExhibitionTextField.insets = new Insets(0, 0, 5, 5);
		gbcExhibitionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcExhibitionTextField.gridx = 1;
		gbcExhibitionTextField.gridy = 0;
		contentPane.add(exhibitionTextField, gbcExhibitionTextField);
		exhibitionTextField.setName("exhibitionTextField");
		exhibitionTextField.setColumns(10);

		lblMuseum = new JLabel("Museum Name");
		GridBagConstraints gbcLblMuseum = new GridBagConstraints();
		gbcLblMuseum.anchor = GridBagConstraints.EAST;
		gbcLblMuseum.gridwidth = 2;
		gbcLblMuseum.insets = new Insets(0, 0, 5, 5);
		gbcLblMuseum.gridx = 2;
		gbcLblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbcLblMuseum);

		findMuseumTextField = new JTextField();
		GridBagConstraints gbcFindMuseumTextField = new GridBagConstraints();
		gbcFindMuseumTextField.insets = new Insets(0, 0, 5, 0);
		gbcFindMuseumTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcFindMuseumTextField.gridx = 4;
		gbcFindMuseumTextField.gridy = 0;
		contentPane.add(findMuseumTextField, gbcFindMuseumTextField);
		findMuseumTextField.setName("findMuseumTextField");
		findMuseumTextField.setColumns(10);

		JLabel lblTotalSeats = new JLabel("Seats");
		GridBagConstraints gbcLblTotalSeats = new GridBagConstraints();
		gbcLblTotalSeats.anchor = GridBagConstraints.WEST;
		gbcLblTotalSeats.insets = new Insets(0, 0, 5, 5);
		gbcLblTotalSeats.gridx = 0;
		gbcLblTotalSeats.gridy = 1;
		contentPane.add(lblTotalSeats, gbcLblTotalSeats);

		totalSeatsTextField = new JTextField();
		GridBagConstraints gbcTotalSeatsTextField = new GridBagConstraints();
		gbcTotalSeatsTextField.insets = new Insets(0, 0, 5, 5);
		gbcTotalSeatsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcTotalSeatsTextField.gridx = 1;
		gbcTotalSeatsTextField.gridy = 1;
		contentPane.add(totalSeatsTextField, gbcTotalSeatsTextField);
		totalSeatsTextField.setName("totalSeatsTextField");
		totalSeatsTextField.setColumns(10);

		btnFind = new JButton("Find Museum Exh.");
		btnFind.setEnabled(false);

		KeyAdapter btnFindMuseumExhEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnFind.setEnabled(!findMuseumTextField.getText().trim().isEmpty());
			}
		};

		findMuseumTextField.addKeyListener(btnFindMuseumExhEnabler);

		GridBagConstraints gbcBtnFind = new GridBagConstraints();
		gbcBtnFind.fill = GridBagConstraints.VERTICAL;
		gbcBtnFind.anchor = GridBagConstraints.WEST;
		gbcBtnFind.insets = new Insets(0, 0, 5, 0);
		gbcBtnFind.gridx = 4;
		gbcBtnFind.gridy = 1;
		contentPane.add(btnFind, gbcBtnFind);

		lblMuseumName = new JLabel("Museum");
		GridBagConstraints gbcLblMuseum1 = new GridBagConstraints();
		gbcLblMuseum1.anchor = GridBagConstraints.WEST;
		gbcLblMuseum1.insets = new Insets(0, 0, 5, 5);
		gbcLblMuseum1.gridx = 0;
		gbcLblMuseum1.gridy = 2;
		contentPane.add(lblMuseumName, gbcLblMuseum1);

		museumNameTextField = new JTextField();
		GridBagConstraints gbcMuseumNameTextField = new GridBagConstraints();
		gbcMuseumNameTextField.insets = new Insets(0, 0, 5, 5);
		gbcMuseumNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcMuseumNameTextField.gridx = 1;
		gbcMuseumNameTextField.gridy = 2;
		contentPane.add(museumNameTextField, gbcMuseumNameTextField);
		museumNameTextField.setName("museumNameTextField");
		museumNameTextField.setColumns(10);

		btnAddExhibition = new JButton("Add Exhibition");
		btnAddExhibition.setEnabled(false);

		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnAddExhibition.setEnabled(!exhibitionTextField.getText().trim().isEmpty()
						&& !totalSeatsTextField.getText().trim().isEmpty()
						&& !museumNameTextField.getText().trim().isEmpty());
			}
		};

		exhibitionTextField.addKeyListener(btnAddEnabler);
		totalSeatsTextField.addKeyListener(btnAddEnabler);
		museumNameTextField.addKeyListener(btnAddEnabler);

		btnFindAll = new JButton("Find all");
		GridBagConstraints gbcBtnFindAll = new GridBagConstraints();
		gbcBtnFindAll.anchor = GridBagConstraints.SOUTHWEST;
		gbcBtnFindAll.insets = new Insets(0, 0, 5, 5);
		gbcBtnFindAll.gridx = 0;
		gbcBtnFindAll.gridy = 3;
		contentPane.add(btnFindAll, gbcBtnFindAll);

		btnFindAll.addActionListener(e -> museumSwingController.getAllExhibitions());

		GridBagConstraints gbcBtnAddExhibition = new GridBagConstraints();
		gbcBtnAddExhibition.anchor = GridBagConstraints.WEST;
		gbcBtnAddExhibition.insets = new Insets(0, 0, 5, 5);
		gbcBtnAddExhibition.gridx = 1;
		gbcBtnAddExhibition.gridy = 3;
		contentPane.add(btnAddExhibition, gbcBtnAddExhibition);

		lblExhibitionsListFor = new JLabel("Exhibitions list for Museum");
		GridBagConstraints gbcLblExhibitionsListFor = new GridBagConstraints();
		gbcLblExhibitionsListFor.gridwidth = 2;
		gbcLblExhibitionsListFor.anchor = GridBagConstraints.SOUTHWEST;
		gbcLblExhibitionsListFor.insets = new Insets(0, 0, 5, 0);
		gbcLblExhibitionsListFor.gridx = 3;
		gbcLblExhibitionsListFor.gridy = 3;
		contentPane.add(lblExhibitionsListFor, gbcLblExhibitionsListFor);

		scrollPaneAllExh = new JScrollPane();
		GridBagConstraints gbcScrollPaneAllExh = new GridBagConstraints();
		gbcScrollPaneAllExh.gridwidth = 2;
		gbcScrollPaneAllExh.insets = new Insets(0, 0, 5, 5);
		gbcScrollPaneAllExh.fill = GridBagConstraints.BOTH;
		gbcScrollPaneAllExh.gridx = 0;
		gbcScrollPaneAllExh.gridy = 4;
		contentPane.add(scrollPaneAllExh, gbcScrollPaneAllExh);

		DefaultListCellRenderer listRenderer = new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Exhibition exhibition = (Exhibition) value;
				return super.getListCellRendererComponent(list, getDisplayString(exhibition), index, isSelected,
						cellHasFocus);
			}
		};

		allExhibitionsListModel = new DefaultListModel<>();
		listAllExh = new JList<>(allExhibitionsListModel);
		listAllExh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAllExh.setName("listAllExh");
		listAllExh.setCellRenderer(listRenderer);
		scrollPaneAllExh.setViewportView(listAllExh);

		scrollPaneMuseumExh = new JScrollPane();
		scrollPaneMuseumExh.setEnabled(false);
		GridBagConstraints gbcScrollPaneMuseumExh = new GridBagConstraints();
		gbcScrollPaneMuseumExh.gridwidth = 2;
		gbcScrollPaneMuseumExh.insets = new Insets(0, 0, 5, 0);
		gbcScrollPaneMuseumExh.fill = GridBagConstraints.BOTH;
		gbcScrollPaneMuseumExh.gridx = 3;
		gbcScrollPaneMuseumExh.gridy = 4;
		contentPane.add(scrollPaneMuseumExh, gbcScrollPaneMuseumExh);

		museumsExhibitionListModel = new DefaultListModel<>();
		listMuseumExh = new JList<>(museumsExhibitionListModel);
		listMuseumExh.setEnabled(false);
		listMuseumExh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMuseumExh.setName("listMuseumExh");
		listMuseumExh.setCellRenderer(listRenderer);
		scrollPaneMuseumExh.setViewportView(listMuseumExh);

		GridBagConstraints gbcLblError1 = new GridBagConstraints();
		gbcLblError1.anchor = GridBagConstraints.WEST;
		gbcLblError1.insets = new Insets(0, 0, 5, 0);
		gbcLblError1.gridx = 4;
		gbcLblError1.gridy = 5;

		lblError = new JLabel(" ");
		lblError.setFont(new Font("Dialog", Font.BOLD, 11));
		lblError.setName("errorLabel");
		lblError.setForeground(Color.RED);
		GridBagConstraints gbcLblError = new GridBagConstraints();
		gbcLblError.gridwidth = 3;
		gbcLblError.anchor = GridBagConstraints.WEST;
		gbcLblError.insets = new Insets(0, 0, 5, 5);
		gbcLblError.gridx = 0;
		gbcLblError.gridy = 5;
		contentPane.add(lblError, gbcLblError);

		btnBook = new JButton("Book");
		btnBook.setEnabled(false);
		GridBagConstraints gbcBtnBook = new GridBagConstraints();
		gbcBtnBook.anchor = GridBagConstraints.SOUTH;
		gbcBtnBook.insets = new Insets(0, 0, 0, 5);
		gbcBtnBook.gridx = 0;
		gbcBtnBook.gridy = 6;
		contentPane.add(btnBook, gbcBtnBook);

		btnBook.addActionListener(e -> museumSwingController.bookExhibitionSeat(listAllExh.getSelectedValue()));

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.insets = new Insets(0, 0, 0, 5);
		gbcBtnDelete.anchor = GridBagConstraints.SOUTHWEST;
		gbcBtnDelete.gridx = 1;
		gbcBtnDelete.gridy = 6;
		contentPane.add(btnDelete, gbcBtnDelete);

		btnMuseumsDashboard = new JButton("Museums Dashboard");
		GridBagConstraints gbcBtnMuseumsDashboard = new GridBagConstraints();
		gbcBtnMuseumsDashboard.gridx = 4;
		gbcBtnMuseumsDashboard.gridy = 6;
		contentPane.add(btnMuseumsDashboard, gbcBtnMuseumsDashboard);

		/*
		 * Actions
		 */

		listAllExh.addListSelectionListener(e -> btnDelete.setEnabled(listAllExh.getSelectedIndex() != -1));
		listAllExh.addListSelectionListener(e -> btnBook.setEnabled(listAllExh.getSelectedIndex() != -1));
		btnAddExhibition.addActionListener(e -> museumSwingController.saveExhibition(museumNameTextField.getText(),
				new Exhibition(exhibitionTextField.getText(), Integer.parseInt(totalSeatsTextField.getText()))));

		btnFind.addActionListener(e -> museumSwingController.getAllMuseumExhibitions(findMuseumTextField.getText()));
		btnDelete.addActionListener(e -> museumSwingController.deleteExhibition(listAllExh.getSelectedValue()));
		btnMuseumsDashboard.addActionListener(e -> museumSwingController.openMuseumDashboard());
	}

	public DefaultListModel<Exhibition> getAllExhibitionsListModel() {
		return allExhibitionsListModel;
	}

	public DefaultListModel<Exhibition> getMuseumsExhibitionListModel() {
		return museumsExhibitionListModel;
	}

	public void setMuseumController(MuseumSwingController museumSwingController) {
		this.museumSwingController = museumSwingController;
	}

	private String getDisplayString(Exhibition exhibition) {
		return exhibition.getName() + " - Total Seats: " + exhibition.getTotalSeats() + " - Booked Seats: "
				+ exhibition.getBookedSeats();
	}

	@Override
	public void showAllExhibitions(List<Exhibition> exhibitions) {
		allExhibitionsListModel.clear();
		exhibitions.stream().forEach(allExhibitionsListModel::addElement);

	}

	@Override
	public void showMuseumExhibitions(List<Exhibition> exhibitions) {
		museumsExhibitionListModel.clear();
		exhibitions.stream().forEach(museumsExhibitionListModel::addElement);
		lblError.setText(" ");

	}

	@Override
	public void exhibitionAdded(Exhibition exhibition) {
		allExhibitionsListModel.addElement(exhibition);
		museumsExhibitionListModel.clear();
		lblError.setText(" ");
	}

	@Override
	public void exhibitionRemoved(Exhibition exhibition) {
		allExhibitionsListModel.removeElement(exhibition);
		lblError.setText(" ");
	}

	@Override
	public void showError(String message, Exhibition exhibition) {
		String exhibitionName = exhibition != null ? exhibition.getName() : "";
		lblError.setText(message + exhibitionName);

	}

	@Override
	public void exhibitionBooked() {
		museumsExhibitionListModel.clear();
		museumSwingController.getAllExhibitions();
		lblError.setText(" ");
	}

}
