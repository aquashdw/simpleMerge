import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class TextEditorPanel extends JPanel{
	private TextAreaPanel textAreaPanel = new TextAreaPanel();
	private ControlsPanel controlsPanel = new ControlsPanel(textAreaPanel);
	
	public TextEditorPanel(){
		setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		setLayout(new BorderLayout());
		add(controlsPanel, "North");
		add(textAreaPanel, "Center");
		setVisible(true);
	}
	
	public TextAreaPanel getTextAreaPanel(){
		return textAreaPanel;
	}
	
	public ControlsPanel getControlsPanel(){
		return controlsPanel;
	}
}
