import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SwapperPanel extends JPanel{
	
	private JPanel subPanel;	
	private JButton toRightButton;
	private JButton toLeftButton;
	
	public SwapperPanel(){
		subPanel = new JPanel();
		
		toRightButton = new JButton(">>");		
		toLeftButton = new JButton("<<");
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
		subPanel.add(toRightButton);
		subPanel.add(toLeftButton);
		setLayout(new GridBagLayout());
		add(subPanel, new GridBagConstraints());
	}
	
	public void addOnSwapToRightActionListeners(ActionListener actionListener){
		toRightButton.addActionListener(actionListener);
	}
	
	public void addOnSwapToLeftActionListeners(ActionListener actionListener){
		toLeftButton.addActionListener(actionListener);
	}

}
