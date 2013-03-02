package hr.fer.zemris.ktlab.sap.gui.test;
import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Test extends JFrame {
	private JSplitPane jSplitPane1;
	private JPanel jPanel1;

	public Test() {
		super();
		initGUI();
	}
	
	
	private void initGUI() {
		{
			jSplitPane1 = new JSplitPane();
			getContentPane().add(jSplitPane1, BorderLayout.CENTER);
			{
				jPanel1 = new JPanel();
				BorderLayout jPanel1Layout = new BorderLayout();
				jSplitPane1.add(jPanel1, JSplitPane.LEFT);
				//jSplitPane1.set
				jPanel1.setLayout(jPanel1Layout);
			}
		}
		{
			this.setSize(267, 223);
		}
		setVisible(true);
	}


	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			
				@Override
				public void run() {
					new Test();
			
				}
			
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
