import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.*;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class TextAreaPanel extends JPanel implements TextControlable{
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);
	private JTextField textField = new JTextField();
	
	private boolean editable = false;
	
	private String originalText = "";
	private ArrayList<ActionListener> onToggleEditableActionListeners = new ArrayList<>();
	
	public TextAreaPanel() {
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		textArea.setEnabled(editable);
		textArea.setDisabledTextColor(Color.GRAY);
		textArea.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent caretEvent) {
				JTextArea tA = (JTextArea)caretEvent.getSource();

                int linenum = 1;
                int columnnum = 1;

                try {
                    int caretpos = tA.getCaretPosition();
                    linenum = tA.getLineOfOffset(caretpos);

                    columnnum = caretpos - tA.getLineStartOffset(linenum);
                    
                    linenum += 1;
                }
                catch(Exception ex) { }
                textField.setText("Line: " + linenum + " Column: " + columnnum);
			}
			
		});

		setLayout(new BorderLayout());
		add(scrollPane, "Center");
		add(textField, "South");
	}
	
	public void toggleEditable(ActionEvent actionEvent){
		editable = !editable;
		textArea.setEnabled(editable);
		
		if(editable){
			setNewText(this.originalText);
		} else{
			setOriginalText(this.textArea.getText());
		}
		
		for(ActionListener a: onToggleEditableActionListeners){
			a.actionPerformed(actionEvent);
		}
	}
	
	public void addOnToggleEditableActionListener(ActionListener actionListener){
		this.onToggleEditableActionListeners.add(actionListener);
	}
	
	public void addCaretListener(CaretListener caretListener){
		this.textArea.addCaretListener(caretListener);
	}
	
	public boolean getEditable(){
		return editable;
	}
	
	@Override
	public void setNewText(String text) {
		textArea.setText(text);
		setOriginalText(text);
	}
	
	public void setOriginalText(String text){
		this.originalText = text;
	}
	
	public String getOriginalText(){
		return this.originalText;
	}
	
	public void setNewText(String text, ArrayList<Highlight> highlights){
		textArea.setText(text);
		if(highlights != null){
			DefaultHighlighter highlighter = (DefaultHighlighter)textArea.getHighlighter();
			highlighter.setDrawsLayeredHighlights(false);
			for(Highlight h: highlights){
				DefaultHighlighter.DefaultHighlightPainter painter = 
			    		new DefaultHighlighter.DefaultHighlightPainter(h.getColor());
			    try {
			    	if(h.getIsEntireLine()){
			    		int start =  textArea.getLineStartOffset(h.getLineNum());
			            int end =    textArea.getLineEndOffset(h.getLineNum());
						highlighter.addHighlight(start, end, painter);
			    	} else{
			    		highlighter.addHighlight(h.getStart(), h.getEnd(), painter);
			    	}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getText() {
		return textArea.getText();
	}
	
	public int getCaretLineNum(){
		try {
			return textArea.getLineOfOffset(textArea.getCaretPosition());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
