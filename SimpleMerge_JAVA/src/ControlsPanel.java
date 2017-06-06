import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsPanel extends JPanel{
	private TextControlable textControlable;
	private JLabel label = new JLabel();
	private JPanel subPanel = new JPanel();
	private JButton loadButton = new JButton("Load");
	private JButton editButton = new JButton("Edit");
	private JButton saveButton = new JButton("Save");
	
	private ArrayList<ActionListener> onTextSetActionListeners = new ArrayList<>();
	
	public ControlsPanel(TextControlable textControlable){
		this.textControlable = textControlable;
		subPanel.setLayout(new GridLayout(1, 3));
		subPanel.add(loadButton);
		subPanel.add(editButton);
		subPanel.add(saveButton);
		
		loadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				onLoad(actionEvent);
			}
		});
		
		editButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				textControlable.toggleEditable(actionEvent);
			}
		});
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				onSave(actionEvent);
			}
		});
		
		setLayout(new BorderLayout());
		add(label, "North");
		add(subPanel, "Center");
	}

	public void setLabelText(String text){
		label.setText(text);
	}
	
	public void addOnTextSetActionListener(ActionListener actionListener){
		onTextSetActionListeners.add(actionListener);
	}
	
	protected void onLoad(ActionEvent actionEvent){
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(ControlsPanel.this) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  BufferedReader in;
		  String text = "";
		  try {
			in = new BufferedReader(new FileReader(file));
			String line;
			line = in.readLine();
			while(line != null){
			    text += line + "\n";
			    line = in.readLine();
			}
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  
		  textControlable.setNewText(text);
		  setLabelText(file.getName());
		  
		  for(ActionListener a: onTextSetActionListeners){
			  a.actionPerformed(actionEvent);
		  }
		}
	}
	
	protected void onSave(ActionEvent actionEvent) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(ControlsPanel.this) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  BufferedWriter out;
		  try {
			out = new BufferedWriter(new FileWriter(file));
			textControlable.toggleEditable(actionEvent);
			out.write(textControlable.getText());
			out.close();
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		}
	}
}
