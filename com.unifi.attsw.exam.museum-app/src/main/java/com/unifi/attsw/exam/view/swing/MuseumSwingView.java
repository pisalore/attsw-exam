package com.unifi.attsw.exam.view.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.view.MuseumView;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

public class MuseumSwingView extends JFrame implements MuseumView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMuseumname;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MuseumSwingView frame = new MuseumSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MuseumSwingView() {
		setTitle("Museum Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 879, 307);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 694, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblMuseum = new JLabel("Museum");
		GridBagConstraints gbc_lblMuseum = new GridBagConstraints();
		gbc_lblMuseum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuseum.anchor = GridBagConstraints.EAST;
		gbc_lblMuseum.gridx = 0;
		gbc_lblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbc_lblMuseum);
		
		txtMuseumname = new JTextField();
		txtMuseumname.setText("museum");
		GridBagConstraints gbc_txtMuseumname = new GridBagConstraints();
		gbc_txtMuseumname.insets = new Insets(0, 0, 5, 0);
		gbc_txtMuseumname.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMuseumname.gridx = 1;
		gbc_txtMuseumname.gridy = 0;
		contentPane.add(txtMuseumname, gbc_txtMuseumname);
		txtMuseumname.setColumns(10);
		
		JLabel lblRooms = new JLabel("Rooms");
		GridBagConstraints gbc_lblRooms = new GridBagConstraints();
		gbc_lblRooms.anchor = GridBagConstraints.EAST;
		gbc_lblRooms.insets = new Insets(0, 0, 0, 5);
		gbc_lblRooms.gridx = 0;
		gbc_lblRooms.gridy = 1;
		contentPane.add(lblRooms, gbc_lblRooms);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
	}

	@Override
	public void showError(String message, Museum museum) {
		// TODO Auto-generated method stub
		
	}

}
