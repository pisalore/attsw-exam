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
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Color;

public class ExhibitionSwingView extends JFrame implements ExhibitionView {
	
	private MuseumController museumController;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField exhibitionTextField;
	private JTextField totalSeatstextField;
	private JLabel lblMuseum;
	private JTextField findMuseumtextField;
	private JButton btnFind;
	private JLabel lblMuseumName;
	private JTextField museumNameTextField;
	private JButton btnAddExhibition;
	private JScrollPane scrollPaneAllExh;
	private JList listAllExh;
	private JButton btnBook;
	private JButton btnDelete;
	private JButton btnFindAll;
	private JLabel lblExhibitionsListFor;
	private JScrollPane scrollPaneMuseumExh;
	private JList listMuseumExh;
	private JLabel lblError;
	private JLabel lblError_1;

	/**
	 * Create the frame.
	 */
	public ExhibitionSwingView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {100, 180, 20, 100, 200};
		gbl_contentPane.rowHeights = new int[] {0, 0, 0, 0, 2, 30, 2};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE, 1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblExhibition = new JLabel("Exhibition");
		GridBagConstraints gbc_lblExhibition = new GridBagConstraints();
		gbc_lblExhibition.anchor = GridBagConstraints.EAST;
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
		gbc_lblMuseum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuseum.gridx = 3;
		gbc_lblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbc_lblMuseum);
		
		findMuseumtextField = new JTextField();
		GridBagConstraints gbc_findMuseumtextField = new GridBagConstraints();
		gbc_findMuseumtextField.insets = new Insets(0, 0, 5, 0);
		gbc_findMuseumtextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_findMuseumtextField.gridx = 4;
		gbc_findMuseumtextField.gridy = 0;
		contentPane.add(findMuseumtextField, gbc_findMuseumtextField);
		findMuseumtextField.setName("findMuseumtextField");
		findMuseumtextField.setColumns(10);
		
		JLabel lblTotalSeats = new JLabel("Total Seats");
		GridBagConstraints gbc_lblTotalSeats = new GridBagConstraints();
		gbc_lblTotalSeats.anchor = GridBagConstraints.EAST;
		gbc_lblTotalSeats.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalSeats.gridx = 0;
		gbc_lblTotalSeats.gridy = 1;
		contentPane.add(lblTotalSeats, gbc_lblTotalSeats);
		
		totalSeatstextField = new JTextField();
		GridBagConstraints gbc_totalSeatstextField = new GridBagConstraints();
		gbc_totalSeatstextField.insets = new Insets(0, 0, 5, 5);
		gbc_totalSeatstextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_totalSeatstextField.gridx = 1;
		gbc_totalSeatstextField.gridy = 1;
		contentPane.add(totalSeatstextField, gbc_totalSeatstextField);
		totalSeatstextField.setName("totalSeatstextField");
		totalSeatstextField.setColumns(10);
		
		btnFind = new JButton("Find Museum Exh.");
		btnFind.setEnabled(false);
		GridBagConstraints gbc_btnFind = new GridBagConstraints();
		gbc_btnFind.fill = GridBagConstraints.VERTICAL;
		gbc_btnFind.anchor = GridBagConstraints.WEST;
		gbc_btnFind.insets = new Insets(0, 0, 5, 0);
		gbc_btnFind.gridx = 4;
		gbc_btnFind.gridy = 1;
		contentPane.add(btnFind, gbc_btnFind);
		
		lblMuseumName = new JLabel("Museum");
		GridBagConstraints gbc_lblMuseum_1 = new GridBagConstraints();
		gbc_lblMuseum_1.anchor = GridBagConstraints.EAST;
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
		GridBagConstraints gbc_btnAddExhibition = new GridBagConstraints();
		gbc_btnAddExhibition.anchor = GridBagConstraints.WEST;
		gbc_btnAddExhibition.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddExhibition.gridx = 1;
		gbc_btnAddExhibition.gridy = 3;
		contentPane.add(btnAddExhibition, gbc_btnAddExhibition);
		
		lblExhibitionsListFor = new JLabel("Exhibitions list for Museum");
		GridBagConstraints gbc_lblExhibitionsListFor = new GridBagConstraints();
		gbc_lblExhibitionsListFor.anchor = GridBagConstraints.SOUTH;
		gbc_lblExhibitionsListFor.insets = new Insets(0, 0, 5, 0);
		gbc_lblExhibitionsListFor.gridx = 4;
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
		
		listAllExh = new JList();
		listAllExh.setName("listAllExh");
		scrollPaneAllExh.setViewportView(listAllExh);
		
		scrollPaneMuseumExh = new JScrollPane();
		GridBagConstraints gbc_scrollPaneMuseumExh = new GridBagConstraints();
		gbc_scrollPaneMuseumExh.gridwidth = 2;
		gbc_scrollPaneMuseumExh.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneMuseumExh.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMuseumExh.gridx = 3;
		gbc_scrollPaneMuseumExh.gridy = 4;
		contentPane.add(scrollPaneMuseumExh, gbc_scrollPaneMuseumExh);
		
		listMuseumExh = new JList();
		listMuseumExh.setName("listMuseumExh");
		scrollPaneMuseumExh.setViewportView(listMuseumExh);
		
		lblError = new JLabel("error1");
		lblError.setForeground(Color.RED);
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.anchor = GridBagConstraints.WEST;
		gbc_lblError.insets = new Insets(0, 0, 5, 5);
		gbc_lblError.gridx = 1;
		gbc_lblError.gridy = 5;
		contentPane.add(lblError, gbc_lblError);
		
		lblError_1 = new JLabel("error2");
		lblError_1.setForeground(Color.RED);
		GridBagConstraints gbc_lblError_1 = new GridBagConstraints();
		gbc_lblError_1.anchor = GridBagConstraints.WEST;
		gbc_lblError_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblError_1.gridx = 4;
		gbc_lblError_1.gridy = 5;
		contentPane.add(lblError_1, gbc_lblError_1);
		
		btnFindAll = new JButton("Find all");
		GridBagConstraints gbc_btnFindAll = new GridBagConstraints();
		gbc_btnFindAll.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnFindAll.insets = new Insets(0, 0, 0, 5);
		gbc_btnFindAll.gridx = 0;
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
		gbc_btnDelete.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnDelete.gridx = 4;
		gbc_btnDelete.gridy = 6;
		contentPane.add(btnDelete, gbc_btnDelete);
	}
	
	public void setMuseumController(MuseumController museumController) {
		this.museumController = museumController;
	}

	@Override
	public void showError(String message, Exhibition exhibition) {
		// TODO Auto-generated method stub
		
	}

}
