package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.gui.DemoBoard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class SettingsWindow extends JDialog {
	

	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	private JPanel jPanel4;
	private JLabel jLabel5;
	private JButton jButton6;
	private JLabel jLabel7;
	private JPanel jPanel3;
	private JButton jButton5;
	private JLabel jLabel6;
	private JButton jButton4;
	private JButton jButton3;
	private JLabel jLabel4;
	private JPanel jPanel2;
	private JButton jBtnListColor2;
	private JCheckBox jCheckBox1;
	private JButton jButton7;
	private JTextField jTextField2;
	private JLabel jLabel9;
	private JPanel jPanel7;
	private JPanel jPanel6;
	private JTextField jTextField1;
	private JLabel jLabel8;
	private JRadioButton jRadioButton2;
	private JRadioButton jRadioButton1;
	private ButtonGroup buttonGroup1;
	private JPanel jPanel5;
	private JButton jButton1;
	private JLabel jLabel3;
	private JButton jBtnListColor1;
	private JLabel jLblListColor1;
	private JButton jBtnUnsafeColor;
	private JLabel jLabel2;
	private JButton jButton2;
	private JComboBox jComboBox1;
	private ComboBoxModel jComboBox1Model;
	private JLabel jLabel1;
	private JPanel jPanel1;
	private JTabbedPane jTabbedPane1;
	private DemoBoard demoBoard;

	/**
	 * Javni konstruktor razreda.
	 * @param mainWindow Glavni prozor programa.
	 */
	public SettingsWindow(MainWindow mainWindow) {
		super(mainWindow);
		this.mainWindow = mainWindow;
		initGUI();
//		if (MainWindow.config.get("sboardShowMenu").equals("true")) {
//			jRadioButton1.setSelected(true);
//		} else {
//			jRadioButton2.setSelected(true);
//		}
	}

	/**
	 * Metoda pomoću koje se inicijalizira grafičko sučelje.
	 */
	private void initGUI() {

		BorderLayout thisLayout = new BorderLayout();
		this.setResizable(false);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setSize(368, 387);
		this.setLocationRelativeTo(mainWindow);
		getContentPane().setLayout(thisLayout);
		{
			{
				buttonGroup1 = new ButtonGroup();
			}
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
			jTabbedPane1.setPreferredSize(new java.awt.Dimension(360, 326));
			{
				jPanel4 = new JPanel();
				jTabbedPane1.addTab("General", null, jPanel4, null);
				jPanel4.setLayout(null);
				{
					jPanel5 = new JPanel();
					jPanel4.add(jPanel5);
					jPanel5.setBounds(7, 7, 343, 224);
					jPanel5.setBorder(BorderFactory.createTitledBorder("Switchboard settings"));
					jPanel5.setLayout(null);
					{
						jRadioButton1 = new JRadioButton();
						jPanel5.add(jRadioButton1);
						jRadioButton1.setText("Show menu on right click on connection");
						jRadioButton1.setBounds(14, 21, 315, 21);
						buttonGroup1.add(jRadioButton1);
						if (MainWindow.config.get("sboardShowMenu").equals("true")) 
							jRadioButton1.setSelected(true);
					}
					{
						jRadioButton2 = new JRadioButton();
						jPanel5.add(jRadioButton2);
						jRadioButton2.setText("Remove connection on right click");
						jRadioButton2.setBounds(14, 42, 322, 21);
						buttonGroup1.add(jRadioButton2);
						if (MainWindow.config.get("sboardShowMenu").equals("false")) 
							jRadioButton2.setSelected(true);
					}
					{
						jLabel8 = new JLabel();
						jPanel5.add(jLabel8);
						jLabel8.setText("Mouse wheel scroll amount:");
						jLabel8.setBounds(14, 63, 322, 28);
						jLabel8.setSize(322, 20);
					}
					{
						jTextField1 = new JTextField();
						jPanel5.add(jTextField1);
						jTextField1.setText((String) MainWindow.config.get("sboardScrollAmount"));
						jTextField1.setBounds(14, 84, 63, 21);
						jTextField1.setSize(90, 21);
					}
					{
						demoBoard = new DemoBoard(mainWindow.sboard.getSelectionTreshold());
						jPanel5.add(demoBoard);
						demoBoard.setBounds(130, 125, 174, 57);
					}
					{
						jLabel9 = new JLabel();
						jPanel5.add(jLabel9);
						jLabel9.setText("Connection mouseover selection area:");
						jLabel9.setBounds(17, 111, 309, 14);
					}
					{
						jTextField2 = new JTextField();
						jPanel5.add(jTextField2);
						jTextField2.setText(Short.toString(mainWindow.sboard.getSelectionTreshold()));
						jTextField2.setBounds(13, 131, 90, 21);
					}
					{
						jButton7 = new JButton();
						jPanel5.add(jButton7);
						jButton7.setText("Set");
						jButton7.setBounds(14, 158, 90, 24);
						jButton7.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton7ActionPerformed(evt);
							}
						});
					}
				}
				{
					jPanel7 = new JPanel();
					jPanel4.add(jPanel7);
					jPanel7.setBounds(7, 237, 336, 50);
					jPanel7.setBorder(BorderFactory.createTitledBorder("Other"));
					jPanel7.setLayout(null);
					{
						jCheckBox1 = new JCheckBox();
						jPanel7.add(jCheckBox1);
						jCheckBox1.setText("Mark as OK on select");
						jCheckBox1.setBounds(14, 14, 315, 28);
						if (MainWindow.config.getProperty("markOKOnSelect").equals("true")) {
							jCheckBox1.setSelected(true);
						}
					}
				}
			}
			{
				jPanel1 = new JPanel();
				jTabbedPane1.addTab("Appearance", null, jPanel1, null);
				jPanel1.setLayout(null);
				{
					jPanel2 = new JPanel();
					jPanel1.add(jPanel2);
					jPanel2.setBounds(7, 7, 343, 123);
					jPanel2.setLayout(null);
					jPanel2.setBorder(BorderFactory
						.createTitledBorder("Font settings"));
					{
						jComboBox1Model = new DefaultComboBoxModel(
							new Integer[] { 8, 9, 10, 11, 12, 13, 14, 15, 16,
									17, 18, 19, 20 });
						jComboBox1 = new JComboBox();
						jPanel2.add(jComboBox1);
						jComboBox1Model.setSelectedItem(new Integer(
							MainWindow.config.getProperty("fontSize")));
						jComboBox1.setModel(jComboBox1Model);
						jComboBox1.setBounds(14, 42, 49, 21);
						jComboBox1.setSize(50, 21);
					}
					{
						jLabel1 = new JLabel();
						jPanel2.add(jLabel1);
						jLabel1.setText("Font size:");
						jLabel1.setBounds(14, 21, 112, 14);
					}
					{
						jLabel4 = new JLabel();
						jPanel2.add(jLabel4);
						jLabel4.setText("Font color 1:");
						jLabel4.setBounds(184, 14, 154, 28);
					}
					{
						jLabel5 = new JLabel();
						jPanel2.add(jLabel5);
						jLabel5.setText("Font color 2:");
						jLabel5.setBounds(182, 63, 154, 28);
					}
					{
						jButton3 = new JButton();
						jPanel2.add(jButton3);
						jButton3.setBounds(180, 43, 50, 21);
						jButton3.setContentAreaFilled(false);
						jButton3.setOpaque(true);
						jButton3.setBackground(Color.decode(MainWindow.config
							.getProperty("fontColor1")));
						jButton3.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								colorButtonActionPerformed(
									evt,
									jButton3,
									"fontColor1");
							}
						});
					}
					{
						jButton4 = new JButton();
						jPanel2.add(jButton4);
						jButton4.setBounds(182, 91, 63, 28);
						jButton4.setSize(50, 21);
						jButton4.setContentAreaFilled(false);
						jButton4.setOpaque(true);
						jButton4.setBackground(Color.decode(MainWindow.config
							.getProperty("fontColor2")));
						jButton4.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								colorButtonActionPerformed(
									evt,
									jButton4,
									"fontColor2");
							}
						});
					}
					{
						jLabel6 = new JLabel();
						jPanel2.add(jLabel6);
						jLabel6.setText("Selected font color:");
						jLabel6.setBounds(14, 63, 154, 28);
					}
					{
						jButton5 = new JButton();
						jPanel2.add(jButton5);
						jButton5.setBounds(14, 91, 63, 28);
						jButton5.setSize(50, 21);
						jButton5.setContentAreaFilled(false);
						jButton5.setOpaque(true);
						jButton5.setBackground(Color.decode(MainWindow.config
							.getProperty("selectedFontColor")));
						jButton5.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								colorButtonActionPerformed(
									evt,
									jButton5,
									"selectedFontColor");
							}
						});
					}
				}
				{
					jPanel3 = new JPanel();
					jPanel1.add(jPanel3);
					jPanel3.setBounds(7, 142, 344, 134);
					jPanel3.setLayout(null);
					jPanel3.setBorder(BorderFactory
						.createTitledBorder("List settings"));
					{
						jLabel2 = new JLabel();
						jPanel3.add(jLabel2);
						jLabel2.setText("Unsafe split background:");
						jLabel2.setBounds(14, 14, 133, 28);
					}
					{
						jBtnUnsafeColor = new JButton();
						jPanel3.add(jBtnUnsafeColor);
						jBtnUnsafeColor.setBounds(14, 42, 63, 28);
						jBtnUnsafeColor.setSize(50, 21);
						jBtnUnsafeColor.setContentAreaFilled(false);
						jBtnUnsafeColor.setOpaque(true);
						jBtnUnsafeColor.setBackground(Color
							.decode((String) MainWindow.config
								.get("unsafeSplitColor")));
						jBtnUnsafeColor.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jBtnUnsafeColorActionPerformed(evt);
							}
						});
					}
					{
						jLblListColor1 = new JLabel();
						jPanel3.add(jLblListColor1);
						jLblListColor1.setText("List background 1:");
						jLblListColor1.setBounds(182, 14, 154, 28);
					}
					{
						jBtnListColor1 = new JButton();
						jPanel3.add(jBtnListColor1);
						jBtnListColor1.setBounds(182, 42, 63, 28);
						jBtnListColor1.setSize(50, 21);
						jBtnListColor1.setContentAreaFilled(false);
						jBtnListColor1.setOpaque(true);
						jBtnListColor1
							.setBackground(Color.decode(MainWindow.config
								.getProperty("listColor1")));
						jBtnListColor1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jBtnListColor1ActionPerformed(evt);
							}
						});
					}
					{
						jLabel3 = new JLabel();
						jPanel3.add(jLabel3);
						jLabel3.setText("List background 2:");
						jLabel3.setBounds(182, 70, 154, 28);
					}
					{
						jBtnListColor2 = new JButton();
						jPanel3.add(jBtnListColor2);
						jBtnListColor2.setBounds(182, 98, 63, 28);
						jBtnListColor2.setSize(50, 21);
						jBtnListColor2.setContentAreaFilled(false);
						jBtnListColor2.setOpaque(true);
						jBtnListColor2
							.setBackground(Color.decode(MainWindow.config
								.getProperty("listColor2")));
						jBtnListColor2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jBtnListColor2ActionPerformed(evt);
							}
						});
					}
					{
						jLabel7 = new JLabel();
						jPanel3.add(jLabel7);
						jLabel7.setText("Selected background:");
						jLabel7.setBounds(14, 70, 133, 28);
					}
					{
						jButton6 = new JButton();
						jPanel3.add(jButton6);
						jButton6.setBounds(14, 98, 63, 28);
						jButton6.setSize(50, 21);
						jButton6.setContentAreaFilled(false);
						jButton6.setOpaque(true);
						jButton6.setBackground(Color.decode(MainWindow.config
							.getProperty("listSelectedColor")));
						jButton6.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								colorButtonActionPerformed(
									evt,
									jButton6,
									"listSelectedColor");
							}
						});
					}
				}
			}
		}
		{
			jPanel6 = new JPanel();
			getContentPane().add(jPanel6, BorderLayout.SOUTH);
			jPanel6.setLayout(null);
			jPanel6.setPreferredSize(new java.awt.Dimension(360, 33));
			{
				jButton2 = new JButton();
				jPanel6.add(jButton2);
				jButton2.setText("Cancel");
				jButton2.setBounds(182, 7, 70, 21);
				jButton2.setSize(80, 21);
				jButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2ActionPerformed(evt);
					}
				});
			}
			{
				jButton1 = new JButton();
				jPanel6.add(jButton1);
				jButton1.setText("OK");
				jButton1.setBounds(98, 7, 70, 21);
				jButton1.setSize(80, 21);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
		}
		this.setTitle("Settings");
		this.setVisible(true);
		
	}

	protected void jButton5ActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jButton4ActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void colorButtonActionPerformed(ActionEvent evt, JButton button, String name) {
		Color color = chooseColor();
		if (color != null) {
			MainWindow.config.put(name, encodeColor(color));
			button.setBackground(color);
		}
	}

	protected void jBtnListColor2ActionPerformed(ActionEvent evt) {
		Color color = chooseColor();
		if (color != null) {
			MainWindow.config.put("listColor2", encodeColor(color));
			jBtnListColor2.setBackground(color);
		}

	}

	/**
	 * Metoda poziva dialog za odabir boja.
	 * 
	 * @return Odabrana boja.
	 */
	private Color chooseColor() {
		Color color = JColorChooser.showDialog(this, "Choose color", getBackground());

		return color;
	}

	/**
	 * Metoda koja zapis boje razreda Color pretvara u heksadecimalni zapis.
	 * 
	 * @param color
	 *            Boja koju treba zapisati u heksadecimalnom obliku.
	 * @return Heksadecimalni zapis boje.
	 */
	private String encodeColor(Color color) {
		String encoded = Integer.toHexString(0xffffff & color.getRGB());

		if (encoded.length() < 6) {
			// fill string with zeros so it has always length of 6
			encoded = "000000".substring(encoded.length()) + encoded;
		}

		return "0x" + encoded;
	}

	private void jButton1ActionPerformed(ActionEvent evt) {
		Integer tmpInteger = (Integer) jComboBox1Model.getSelectedItem();
		MainWindow.config.put("fontSize", tmpInteger.toString());
		MainWindow.config.put("sboardScrollAmount", jTextField1.getText());
		if (jRadioButton1.isSelected()) {
			MainWindow.config.put("sboardShowMenu", "true");
			mainWindow.sboard.setConnectionPopupMenu(true);
		}	
		else {
			MainWindow.config.put("sboardShowMenu", "false");
			mainWindow.sboard.setConnectionPopupMenu(false);
		}
		if (jCheckBox1.isSelected()) {
			MainWindow.config.put("markOKOnSelect", "true");
		} else {
			MainWindow.config.put("markOKOnSelect", "false");
		}
		MainWindow.config.put("sboardTreshold", jTextField2.getText());
		mainWindow.sboard.setSelectionTreshold(Short.parseShort(jTextField2.getText()));
			
		
		mainWindow.repaint();
		this.dispose();
	}

	private void jButton2ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void jBtnUnsafeColorActionPerformed(ActionEvent evt) {
		Color color = chooseColor();
		if (color != null) {
			MainWindow.config.put("unsafeSplitColor", encodeColor(color));
			jBtnUnsafeColor.setBackground(color);
		}
	}

	private void jBtnListColor1ActionPerformed(ActionEvent evt) {
		Color color = chooseColor();
		if (color != null) {
			MainWindow.config.put("listColor1", encodeColor(color));
			jBtnListColor1.setBackground(color);
		}
	}
	
	private void jButton7ActionPerformed(ActionEvent evt) {
		short treshold = Short.parseShort(jTextField2.getText());
		demoBoard.setSelectionTreshold(treshold);
		demoBoard.repaint();
		
	}

}
