package com.unifi.attsw.exam.presentation.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import com.unifi.attsw.exam.core.view.MuseumView;
import com.unifi.attsw.exam.presentation.controller.swing.MuseumSwingController;
import com.unifi.attsw.exam.repository.model.Museum;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ListSelectionModel;

/**
 * 
 * Swing implementation for {@link MuseumView}.
 *
 */
public class MuseumSwingView extends JFrame implements MuseumView {

	private transient MuseumSwingController museumSwingController;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMuseum;
	private JTextField txtRooms;
	private JList<Museum> museumList;
	private DefaultListModel<Museum> museumListModel;
	private JLabel errorMessageLabel;
	private JLabel lblMuseum;
	private JLabel lblRooms;
	private JButton btnFindAll;
	private JButton btnAdd;
	private JButton btnDeleteSelected;
	private JButton btnExhibitionsDashboard;

	/**
	 * Creates the frame.
	 */
	public MuseumSwingView() {
		setTitle("Museum Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 100, 100, 152, 100 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gblContentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gblContentPane);

		lblMuseum = new JLabel("Museum");
		GridBagConstraints gbcLblMuseum = new GridBagConstraints();
		gbcLblMuseum.anchor = GridBagConstraints.EAST;
		gbcLblMuseum.insets = new Insets(0, 0, 5, 5);
		gbcLblMuseum.gridx = 0;
		gbcLblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbcLblMuseum);

		txtMuseum = new JTextField();
		txtMuseum.setName("museum");
		GridBagConstraints gbcTxtMuseum = new GridBagConstraints();
		gbcTxtMuseum.insets = new Insets(0, 0, 5, 5);
		gbcTxtMuseum.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtMuseum.gridx = 1;
		gbcTxtMuseum.gridy = 0;
		contentPane.add(txtMuseum, gbcTxtMuseum);
		txtMuseum.setColumns(10);

		btnFindAll = new JButton("Find all");
		GridBagConstraints gbcBtnFindAll = new GridBagConstraints();
		gbcBtnFindAll.insets = new Insets(0, 0, 5, 0);
		gbcBtnFindAll.gridx = 3;
		gbcBtnFindAll.gridy = 0;
		contentPane.add(btnFindAll, gbcBtnFindAll);

		lblRooms = new JLabel("Rooms");
		GridBagConstraints gbcLblRooms = new GridBagConstraints();
		gbcLblRooms.anchor = GridBagConstraints.EAST;
		gbcLblRooms.insets = new Insets(0, 0, 5, 5);
		gbcLblRooms.gridx = 0;
		gbcLblRooms.gridy = 1;
		contentPane.add(lblRooms, gbcLblRooms);

		txtRooms = new JTextField();
		txtRooms.setName("rooms");
		txtRooms.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				errorMessageLabel.setText(" ");
				if (!Character.isDigit(ke.getKeyChar())) {
					errorMessageLabel.setText("Please, insert only integer numbers for Rooms.");
				}
			}
		});
		GridBagConstraints gbcTxtRooms = new GridBagConstraints();
		gbcTxtRooms.insets = new Insets(0, 0, 5, 5);
		gbcTxtRooms.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtRooms.gridx = 1;
		gbcTxtRooms.gridy = 1;
		contentPane.add(txtRooms, gbcTxtRooms);
		txtRooms.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);

		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnAdd.setEnabled(!txtMuseum.getText().trim().isEmpty() && !txtRooms.getText().trim().isEmpty()
						&& StringUtils.isNumeric(txtRooms.getText()));
			}
		};
		txtMuseum.addKeyListener(btnAddEnabler);
		txtRooms.addKeyListener(btnAddEnabler);

		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.insets = new Insets(0, 0, 5, 5);
		gbcBtnAdd.gridx = 1;
		gbcBtnAdd.gridy = 2;
		contentPane.add(btnAdd, gbcBtnAdd);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.gridwidth = 4;
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 3;
		contentPane.add(scrollPane, gbcScrollPane);

		museumListModel = new DefaultListModel<>();
		museumList = new JList<>(museumListModel);
		museumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		museumList.setName("museumList");
		museumList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Museum museum = (Museum) value;
				return super.getListCellRendererComponent(list, getDisplayString(museum), index, isSelected,
						cellHasFocus);
			}
		});

		scrollPane.setViewportView(museumList);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);

		GridBagConstraints gbcBtnDeleteSelected = new GridBagConstraints();
		gbcBtnDeleteSelected.gridwidth = 2;
		gbcBtnDeleteSelected.anchor = GridBagConstraints.WEST;
		gbcBtnDeleteSelected.insets = new Insets(0, 0, 5, 5);
		gbcBtnDeleteSelected.gridx = 0;
		gbcBtnDeleteSelected.gridy = 5;
		contentPane.add(btnDeleteSelected, gbcBtnDeleteSelected);

		errorMessageLabel = new JLabel(" ");
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setName("errorMessageLabel");
		GridBagConstraints gbcErrorMessageLabel = new GridBagConstraints();
		gbcErrorMessageLabel.anchor = GridBagConstraints.WEST;
		gbcErrorMessageLabel.gridwidth = 4;
		gbcErrorMessageLabel.insets = new Insets(0, 0, 5, 5);
		gbcErrorMessageLabel.gridx = 0;
		gbcErrorMessageLabel.gridy = 6;
		contentPane.add(errorMessageLabel, gbcErrorMessageLabel);

		btnExhibitionsDashboard = new JButton("Exhibitions Dashboard");
		GridBagConstraints gbcBtnExhibitionsDashboard = new GridBagConstraints();
		gbcBtnExhibitionsDashboard.gridx = 3;
		gbcBtnExhibitionsDashboard.gridy = 7;
		contentPane.add(btnExhibitionsDashboard, gbcBtnExhibitionsDashboard);

		// Actions
		btnExhibitionsDashboard.addActionListener(e -> {
			museumSwingController.getAllExhibitions();
			museumSwingController.openExhibitionsDashboard();
		});
		btnDeleteSelected.addActionListener(e -> museumSwingController.deleteMuseum(museumList.getSelectedValue()));
		museumList.addListSelectionListener(e -> btnDeleteSelected.setEnabled(museumList.getSelectedIndex() != -1));
		btnAdd.addActionListener(e -> museumSwingController
				.saveMuseum(new Museum(txtMuseum.getText(), Integer.parseInt(txtRooms.getText()))));
		btnFindAll.addActionListener(e -> museumSwingController.getAllMuseums());

	}

	@Override
	public void showAllMuseums(List<Museum> museums) {
		museumListModel.clear();
		museums.stream().forEach(museumListModel::addElement);
	}

	@Override
	public void showError(String message, Museum museum) {
		String museumName = museum != null ? museum.getName() : "";
		errorMessageLabel.setText(message + museumName);

	}

	@Override
	public void museumAdded(Museum museum) {
		museumListModel.addElement(museum);
		errorMessageLabel.setText(" ");

	}

	@Override
	public void museumRemoved(Museum museum) {
		museumListModel.removeElement(museum);

	}

	/**
	 * Get the List model for all Museums.
	 * 
	 * @return The List Model View for all Museums.
	 */
	public DefaultListModel<Museum> getMuseumListModel() {
		return museumListModel;
	}

	/**
	 * Display a formatted re-cap for the selected Exhibition.
	 * 
	 * @param museum The Museum for which display formatted info.
	 * @return The formatted String with Museum info.
	 */
	private String getDisplayString(Museum museum) {
		return museum.getName() + " - Total Rooms: " + museum.getTotalRooms() + " - Occupied Rooms: "
				+ museum.getOccupiedRooms();
	}

	/**
	 * Set the Swing controller for this Museum View.
	 * 
	 * @param museumSwingController The MuseumController to interact with.
	 */
	public void setMuseumController(MuseumSwingController museumSwingController) {
		this.museumSwingController = museumSwingController;
	}

}
