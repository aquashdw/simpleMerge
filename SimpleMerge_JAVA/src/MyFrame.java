import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class MyFrame extends JFrame {
	
	private TextEditorPanel leftPanel;
	private TextEditorPanel rightPanel;
	private SwapperPanel swapperPanel;

	private ArrayList<Highlight> leftHighlights;
	private ArrayList<Highlight> rightHighlights;
	
	private ArrayList<Highlight> leftSelectHighlights;
	private ArrayList<Highlight> rightSelectHighlights;
	
	private int caretLineNum = 0;

	public MyFrame() {
		JPanel subPanel = new JPanel();
		leftPanel = new TextEditorPanel();
		rightPanel = new TextEditorPanel();
		swapperPanel = new SwapperPanel();
		
		leftHighlights = new ArrayList<>();
		rightHighlights = new ArrayList<>();
		
		leftSelectHighlights = new ArrayList<>();
		rightSelectHighlights = new ArrayList<>(); 

		leftPanel.getTextAreaPanel().addOnToggleEditableActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comapareAndHighlight();
			}
		});
		
		leftPanel.getTextAreaPanel().addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e) {
				int c = leftPanel.getTextAreaPanel().getCaretLineNum();
				highlightSelectedBlock(leftPanel.getTextAreaPanel(),
						leftHighlights, leftSelectHighlights, c);
				highlightSelectedBlock(rightPanel.getTextAreaPanel(),
						rightHighlights, rightSelectHighlights, c);
			}
		});

		leftPanel.getControlsPanel().addOnTextSetActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				comapareAndHighlight();

			}
		});

		rightPanel.getTextAreaPanel().addOnToggleEditableActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comapareAndHighlight();
			}
		});
		
		rightPanel.getTextAreaPanel().addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e) {
				int c = rightPanel.getTextAreaPanel().getCaretLineNum();
				highlightSelectedBlock(leftPanel.getTextAreaPanel(),
						leftHighlights, leftSelectHighlights, c);
				highlightSelectedBlock(rightPanel.getTextAreaPanel(),
						rightHighlights, rightSelectHighlights, c);
			}
		});

		rightPanel.getControlsPanel().addOnTextSetActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				comapareAndHighlight();

			}
		});

		leftPanel.getControlsPanel().setLabelText("left");
		rightPanel.getControlsPanel().setLabelText("right");

		swapperPanel.addOnSwapToRightActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				//left to right by default
				swap(leftPanel.getTextAreaPanel(), rightPanel.getTextAreaPanel(),
						leftHighlights, rightHighlights, caretLineNum);
			}

		});

		swapperPanel.addOnSwapToLeftActionListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				//right to left
				swap(rightPanel.getTextAreaPanel(), leftPanel.getTextAreaPanel(),
						rightHighlights, leftHighlights, caretLineNum);
			}

		});

		setSize(512, 256);

		subPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		subPanel.add(leftPanel, c);
		subPanel.add(swapperPanel);
		subPanel.add(rightPanel, c);

		setLayout(new BorderLayout());
		add(subPanel, "Center");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void highlightSelectedBlock(TextAreaPanel textAreaPanel, ArrayList<Highlight> highlights,
			ArrayList<Highlight> selectHighlights, int caretLineNum){
		if (!leftPanel.getTextAreaPanel().getEditable() &&
				!rightPanel.getTextAreaPanel().getEditable()){
			if(highlights != null){
				BlockInfo blockInfo = getBlockInfo(highlights, caretLineNum);
				if(blockInfo != null){
					this.caretLineNum = caretLineNum;
					for(Highlight h: selectHighlights){
						h.changeToNormal();
					}
					selectHighlights.removeAll(selectHighlights);
					addBlockHighlights(blockInfo, selectHighlights,highlights);
					for(Highlight h: selectHighlights){
						h.changeToSelect();
					}
					textAreaPanel.setNewText(textAreaPanel.getText(), highlights);
				}
			}
		}
	}

	private void comapareAndHighlight() {
		TextAreaPanel leftTextAreaPanel = leftPanel.getTextAreaPanel();
		TextAreaPanel rightTextAreaPanel = rightPanel.getTextAreaPanel();

		if (!leftTextAreaPanel.getEditable() && !rightTextAreaPanel.getEditable()) {
			DP.setTexts(leftTextAreaPanel.getOriginalText(), rightTextAreaPanel.getOriginalText());

			String newLeftText = DP.getNewLeftText();
			leftHighlights = DP.getLeftHighlights();
			leftTextAreaPanel.setNewText(newLeftText, leftHighlights);

			String newRightText = DP.getNewRightText();
			rightHighlights = DP.getRightHighlights();
			rightTextAreaPanel.setNewText(newRightText, rightHighlights);
		}
		return;
	}

	private void swap(TextAreaPanel originPanel, TextAreaPanel destinationPanel,
			ArrayList<Highlight> originHighlights, ArrayList<Highlight> destinationHighlights, int caretLineNum) {
		if (!originPanel.getEditable() && !destinationPanel.getEditable()) {
			
			//add dummy
			String originLines[] = (originPanel.getText() + "dummy").split("\n");
			String destinationLines[] = (destinationPanel.getText() + "dummy").split("\n");
			//erase dummy
			originLines = Arrays.copyOf(originLines, originLines.length - 1);
			destinationLines = Arrays.copyOf(destinationLines, destinationLines.length - 1);

			BlockInfo blockInfo = getBlockInfo(leftHighlights, caretLineNum);
			if(blockInfo == null){
				return;
			}
			
			//copy block of strings to destination from origin
			for(int i = blockInfo.getStart() + 1; i < blockInfo.getEnd(); i++){
				destinationLines[i] = originLines[i];
				System.out.println(originLines[i]);
			}

			ArrayList<Highlight> originBlockHighlights = new ArrayList<>();
			ArrayList<Highlight> destinationBlockHighlights = new ArrayList<>();
			
			//find highlights for block
			addBlockHighlights(blockInfo, originBlockHighlights, originHighlights);
			addBlockHighlights(blockInfo, destinationBlockHighlights, destinationHighlights);
			
			//change highlights for block of strings
			destinationHighlights.removeAll(destinationBlockHighlights);
			destinationHighlights.addAll(originBlockHighlights);

			//find all gray lines
			ArrayList<Integer> grayLines = new ArrayList<>();
			for (Highlight h : destinationHighlights) {
				if (h.getIsEntireLine() && (h.getColor() == Highlight.GRAY || h.getColor() == Highlight.GRAY_SELECT)) {
					grayLines.add(h.getLineNum());
				}
			}
			
			//make new original text
			String newOriginal = "";
			for (int i = 1; i < destinationLines.length; i++) {
				if (!grayLines.contains(i)) {
					newOriginal += destinationLines[i] + "\n";
				}
			}
			//erase unnecessary new lines
			newOriginal = newOriginal.substring(0, newOriginal.length() - 2);

			destinationPanel.setOriginalText(newOriginal);

			comapareAndHighlight();
		}
		return;
	}
	
	//return start and end line number of block where caret is in
	//block is sequence of highlighted lines
	private BlockInfo getBlockInfo(ArrayList<Highlight> highlights, int caretLineNum){
		int start = 0;
		int end = 0;
		int lastLine = 0;
		boolean wasBreaked = false;
		for (Highlight h : highlights) {
			if (h.getIsEntireLine()) {
				if (h.getLineNum() != lastLine + 1) {
					end = lastLine + 1;
					if (start < caretLineNum && caretLineNum < end) {
						wasBreaked = true;
						break;
					}
					start = h.getLineNum() - 1;
				}
				lastLine = h.getLineNum();
			}
		}

		if (!wasBreaked) {
			end = lastLine + 1;
		}
		
		if(start < caretLineNum && caretLineNum < end){
			return new BlockInfo(start, end);
		} else {
			return null;
		}
		
	}
	
	//find highlights for specific block
	private void addBlockHighlights(BlockInfo blockInfo,
			ArrayList<Highlight> blockHighlights, ArrayList<Highlight> highlights){
		
		for (Highlight h : highlights) {
			if (blockInfo.getStart() < h.getLineNum() && h.getLineNum() < blockInfo.getEnd()) {
				blockHighlights.add(h);
			}
		}
	}
}
