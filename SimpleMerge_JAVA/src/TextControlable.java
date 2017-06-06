import java.awt.event.ActionEvent;

public interface TextControlable {
	public String getText();
	public void setNewText(String text);
	public void toggleEditable(ActionEvent actionEvent);
}
