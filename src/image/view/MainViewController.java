package image.view;

import java.io.IOException;

import image.Main;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class MainViewController {
	
	private Main main;
	private OpenImageController openImage;
	
	
	@FXML
	private void goOpenImage() throws IOException {
		main.showOpenImage();
	}
	


}
