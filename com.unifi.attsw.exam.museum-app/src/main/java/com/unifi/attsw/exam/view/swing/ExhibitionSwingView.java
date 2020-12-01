package com.unifi.attsw.exam.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.view.ExhibitionView;
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

public class ExhibitionSwingView extends JFrame implements ExhibitionView {

	private MuseumController museumController;

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

	/**
	 * Create the frame.
	 */
	public ExhibitionSwingView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 100, 180, 20, 100, 200 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 2, 30, 2 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE, 1.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblExhibition = new JLabel("Exhibition");
		GridBagConstraints gbc_lblExhibition = new GridBagConstraints();
		gbc_lblExhibition.anchor = GridBagConstraints.WEST;
		gbc_lblExhibition.insets = new Insets(0, 0, 5, 5);
		gbc_lblExhibition.gridx = 0;
		gbc_lblExhibition.gridy = 0;
		contentPane.add(lblExhibition, gbc_lblExhibition);

		exhibitionTextField = new JTextField();
		GridBagConstraints gbc_exhibitionTextField = new GridBagConstraints();
		gbc_exhibitionTextField.insets = new Insets(0, 0, 5, 5);
		gbc_exhibitionTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_exhibitionTextField.gridx = 1;
		gbc_exhibitionTextField.gridy = 0;
		contentPane.add(exhibitionTextField, gbc_exhibitionTextField);
		exhibitionTextField.setName("exhibitionTextField");
		exhibitionTextField.setColumns(10);

		lblMuseum = new JLabel("Museum Name");
		GridBagConstraints gbc_lblMuseum = new GridBagConstraints();
		gbc_lblMuseum.anchor = GridBagConstraints.EAST;
		gbc_lblMuseum.gridwidth = 2;
		gbc_lblMuseum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuseum.gridx = 2;
		gbc_lblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbc_lblMuseum);

		findMuseumTextField = new JTextField();
		GridBagConstraints gbc_findMuseumTextField = new GridBagConstraints();
		gbc_findMuseumTextField.insets = new Insets(0, 0, 5, 0);
		gbc_findMuseumTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_findMuseumTextField.gridx = 4;
		gbc_findMuseumTextField.gridy = 0;
		contentPane.add(findMuseumTextField, gbc_findMuseumTextField);
		findMuseumTextField.setName("findMuseumTextField");
		findMuseumTextField.setColumns(10);

		JLabel lblTotalSeats = new JLabel("Seats");
		GridBagConstraints gbc_lblTotalSeats = new GridBagConstraints();
		gbc_lblTotalSeats.anchor = GridBagConstraints.WEST;
		gbc_lblTotalSeats.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalSeats.gridx = 0;
		gbc_lblTotalSeats.gridy = 1;
		contentPane.add(lblTotalSeats, gbc_lblTotalSeats);

		totalSeatsTextField = new JTextField();
		GridBagConstraints gbc_totalSeatsTextField = new GridBagConstraints();
		gbc_totalSeatsTextField.insets = new Insets(0, 0, 5, 5);
		gbc_totalSeatsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_totalSeatsTextField.gridx = 1;
		gbc_totalSeatsTextField.gridy = 1;
		contentPane.add(totalSeatsTextField, gbc_totalSeatsTextField);
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

		GridBagConstraints gbc_btnFind = new GridBagConstraints();
		gbc_btnFind.fill = GridBagConstraints.VERTICAL;
		gbc_btnFind.anchor = GridBagConstraints.WEST;
		gbc_btnFind.insets = new Insets(0, 0, 5, 0);
		gbc_btnFind.gridx = 4;
		gbc_btnFind.gridy = 1;
		contentPane.add(btnFind, gbc_btnFind);

		lblMuseumName = new JLabel("Museum");
		GridBagConstraints gbc_lblMuseum_1 = new GridBagConstraints();
		gbc_lblMuseum_1.anchor = GridBagConstraints.WEST;
		gbc_lblMuseum_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuseum_1.gridx = 0;
		gbc_lblMuseum_1.gridy = 2;
		contentPane.add(lblMuseumName, gbc_lblMuseum_1);

		museumNameTextField = new JTextField();
		GridBagConstraints gbc_museumNameTextField = new GridBagConstraints();
		gbc_museumNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_museumNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_museumNameTextField.gridx = 1;
		gbc_museumNameTextField.gridy = 2;
		contentPane.add(museumNameTextField, gbc_museumNameTextField);
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

		GridBagConstraints gbc_btnAddExhibition = new GridBagConstraints();
		gbc_btnAddExhibition.anchor = GridBagConstraints.WEST;
		gbc_btnAddExhibition.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddExhibition.gridx = 1;
		gbc_btnAddExhibition.gridy = 3;
		contentPane.add(btnAddExhibition, gbc_btnAddExhibition);

		lblExhibitionsListFor = new JLabel("Exhibitions list for Museum");
		GridBagConstraints gbc_lblExhibitionsListFor = new GridBagConstraints();
		gbc_lblExhibitionsListFor.gridwidth = 2;
		gbc_lblExhibitionsListFor.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblExhibitionsListFor.insets = new Insets(0, 0, 5, 5);
		gbc_lblExhibitionsListFor.gridx = 3;
		gbc_lblExhibitionsListFor.gridy = 3;
		contentPane.add(lblExhibitionsListFor, gbc_lblExhibitionsListFor);

		scrollPaneAllExh = new JScrollPane();
		GridBagConstraints gbc_scrollPaneAllExh = new GridBagConstraints();
		gbc_scrollPaneAllExh.gridwidth = 2;
		gbc_scrollPaneAllExh.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneAllExh.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneAllExh.gridx = 0;
		gbc_scrollPaneAllExh.gridy = 4;
		contentPane.add(scrollPaneAllExh, gbc_scrollPaneAllExh);

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
		GridBagConstraints gbc_scrollPaneMuseumExh = new GridBagConstraints();
		gbc_scrollPaneMuseumExh.gridwidth = 2;
		gbc_scrollPaneMuseumExh.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneMuseumExh.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMuseumExh.gridx = 3;
		gbc_scrollPaneMuseumExh.gridy = 4;
		contentPane.add(scrollPaneMuseumExh, gbc_scrollPaneMuseumExh);

		museumsExhibitionListModel = new DefaultListModel<>();
		listMuseumExh = new JList<>(museumsExhibitionListModel);
		listMuseumExh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMuseumExh.setName("listMuseumExh");
		listMuseumExh.setCellRenderer(listRenderer);
		scrollPaneMuseumExh.setViewportView(listMuseumExh);

		GridBagConstraints gbc_lblError_1 = new GridBagConstraints();
		gbc_lblError_1.anchor = GridBagConstraints.WEST;
		gbc_lblError_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblError_1.gridx = 4;
		gbc_lblError_1.gridy = 5;

		lblError = new JLabel(" ");
		lblError.setName("errorLabel");
		lblError.setForeground(Color.RED);
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.anchor = GridBagConstraints.WEST;
		gbc_lblError.insets = new Insets(0, 0, 5, 5);
		gbc_lblError.gridx = 2;
		gbc_lblError.gridy = 5;
		contentPane.add(lblError, gbc_lblError);

		btnFindAll = new JButton("Find all");
		GridBagConstraints gbc_btnFindAll = new GridBagConstraints();
		gbc_btnFindAll.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnFindAll.insets = new Insets(0, 0, 0, 5);
		gbc_btnFindAll.gridx = 1;
		gbc_btnFindAll.gridy = 6;
		contentPane.add(btnFindAll, gbc_btnFindAll);

		btnBook = new JButton("Book");
		btnBook.setEnabled(false);
		GridBagConstraints gbc_btnBook = new GridBagConstraints();
		gbc_btnBook.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnBook.insets = new Insets(0, 0, 0, 5);
		gbc_btnBook.gridx = 2;
		gbc_btnBook.gridy = 6;
		contentPane.add(btnBook, gbc_btnBook);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnDelete.gridx = 4;
		gbc_btnDelete.gridy = 6;
		contentPane.add(btnDelete, gbc_btnDelete);

		/*
		 * Actions
		 */

		listAllExh.addListSelectionListener(e -> btnDelete.setEnabled(listAllExh.getSelectedIndex() != -1));
		listMuseumExh.addListSelectionListener(e -> btnDelete.setEnabled(listMuseumExh.getSelectedIndex() != -1));
		listAllExh.addListSelectionListener(e -> btnBook.setEnabled(listAllExh.getSelectedIndex() != -1));
		listMuseumExh.addListSelectionListener(e -> btnBook.setEnabled(listMuseumExh.getSelectedIndex() != -1));

		btnFindAll.addActionListener(e -> museumController.getAllExhibitions());
		btnAddExhibition.addActionListener(e -> museumController.saveExhibition(museumNameTextField.getText(),
				new Exhibition(exhibitionTextField.getText(), Integer.parseInt(totalSeatsTextField.getText()))));

		btnDelete.addActionListener(e -> museumController.deleteExhibition(listAllExh.getSelectedValue()));

		btnBook.addActionListener(e -> museumController.bookExhibitionSeat(listAllExh.getSelectedValue()));

		btnFind.addActionListener(e -> museumController.getAllMuseumExhibitions(findMuseumTextField.getText()));
	}

	public DefaultListModel<Exhibition> getAllExhibitionsListModel() {
		return allExhibitionsListModel;
	}

	public DefaultListModel<Exhibition> getMuseumsExhibitionListModel() {
		return museumsExhibitionListModel;
	}

	public void setMuseumController(MuseumController museumController) {
		this.museumController = museumController;
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
		allExhibitionsListModel.clear();
		exhibitions.stream().forEach(museumsExhibitionListModel::addElement);

	}

	@Override
	public void exhibitionAdded(Exhibition exhibition) {
		allExhibitionsListModel.addElement(exhibition);
		findMuseumTextField.setText(" ");
		museumsExhibitionListModel.clear();
		lblError.setText(" ");
	}

	@Override
	public void exhibitionRemoved(Exhibition exhibition) {
		allExhibitionsListModel.removeElement(exhibition);
	}

	@Override
	public void showError(String message, Exhibition exhibition) {
		String exhibitionName = exhibition != null ? exhibition.getName() : "";
		lblError.setText(message + exhibitionName);

	}

}
