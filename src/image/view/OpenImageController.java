
package image.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.sun.javafx.stage.StageHelper;

import image.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OpenImageController {

	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;

	public Main main;

	public ImageStore imageStore;

	public Stage imageStage;

	public ArrayList<ImageStore> multipleImages = new ArrayList<ImageStore>();

	public String ID;

	@FXML
	public StackPane imageViewPane;

	@FXML
	public ImageView imageView;

	@FXML
	public Line imageLine1;

	@FXML
	public Line imageLine2;

	@FXML
	public Line imageLine3;

	@FXML
	public Line imageLine4;

	@FXML
	public ImageView imageViewRed;

	@FXML
	public ImageView imageViewGreen;

	@FXML
	public ImageView imageViewBlue;

	@FXML
	public ImageView imageViewGreyScale;

	@FXML
	public ImageView imageViewRGB;

	@FXML
	public VBox imagesContainer;

	public WritableImage writableImageRed;

	public WritableImage writableImageGreen;

	public WritableImage writableImageBlue;

	@FXML
	private Label imageName;

	@FXML
	private Label imageDimensions;

	@FXML
	private Label imageSize;

	public Image image;

	public File selectedFile;

	public OpenImageController() {
		multipleImages = new ArrayList<ImageStore>();
	}

	public void setVisibility() {
		imageName.setVisible(true);
		imageDimensions.setVisible(true);
		imageSize.setVisible(true);
		imageViewPane.setVisible(true);
		imageLine1.setVisible(true);
		imageLine2.setVisible(true);
		imageLine3.setVisible(true);
		imageLine4.setVisible(true);
		imageView.setVisible(true);
	}

	protected String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String openFile() {

		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			image = new Image(selectedFile.toURI().toString());
			imageView.setImage(image);
			setVisibility();
			imageName.setText(selectedFile.getName());
			imageDimensions.setText(image.getHeight() + "x" + image.getWidth());

			long size = selectedFile.length() / 1024; // convert to KB
			long sizeMB = size / 1024; // convert to MB
			if (size > 999) {
				imageSize.setText(sizeMB + "MB"); // If image is > 999KB, display in MB
			} else {
				imageSize.setText(size + "KB");
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Open Image");
			alert.setHeaderText("No Image Selected!");
			alert.setContentText("Please Select A Valid Image File!");
			alert.showAndWait();
		}

		ID = getSaltString(); // generate new ID
		multipleImages.add(new ImageStore(ID, image, selectedFile.getName(), selectedFile.length(), image.getWidth(),
				image.getHeight()));
		imagesContainer.setSpacing(20);

		ImageView newImageView = new ImageView();
		newImageView.setId(ID); // imageView to store the image has the same ID
		newImageView.setFitHeight(400);
		newImageView.setFitWidth(600);
		newImageView.setPreserveRatio(true);
		newImageView.setSmooth(true);
		newImageView.setImage(image);
		imagesContainer.getChildren().addAll(newImageView);

		newImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {

				imageStage = new Stage();

				String clickedID = newImageView.getId();

				System.out.println("Clicked image view: " + clickedID);

				for (int i = 0; i < multipleImages.size(); i++) {

					if (multipleImages.get(i).getID().equals(clickedID)) { // if image ID matches imageView ID

						System.out.println("Image in array: " + multipleImages.get(i).getID());
						String name = multipleImages.get(i).getImageName();
						double getX = multipleImages.get(i).getImageDimensionsX();
						double getY = multipleImages.get(i).getImageDimensionsY();
						long size = multipleImages.get(i).getImageSize();

						long sizeKB = size / 1024;
						long sizeMB = sizeKB / 1024; // convert to MB

						Image arrayImage = multipleImages.get(i).getImage();

						StackPane imagePane = new StackPane();
						ImageView openImageScene = new ImageView();
						openImageScene.setFitHeight(getY / 2.2);
						openImageScene.setFitWidth(getX / 2.2);
						openImageScene.setImage(arrayImage);
						imagePane.getChildren().addAll(openImageScene);
						Scene imageScene = new Scene(imagePane, getX / 2.2, getY / 2.2);
						if (sizeKB > 999) {
							imageStage.setTitle(name + " | " + getX + "x" + getY + " | " + sizeMB + "MB");
						} else {
							imageStage.setTitle(name + " | " + getX + "x" + getY + " | " + sizeKB + "KB");
						}

						imageStage.setScene(imageScene);
						imageStage.show();
						break;
					} else {
						System.out.println("\nNo image found, incrementing i.");
					}
				}
			}
		});
		return ID;
	}

	public void makeGrey() { // apply grey filter to image view

		ColorAdjust desaturate = new ColorAdjust();
		desaturate.setSaturation(-1);
		imageViewGreyScale.setEffect(desaturate);
		imageViewRGB.setEffect(desaturate);

	}

	public void makeColor() { // undo grey filter applied
		ColorAdjust desaturate = new ColorAdjust();
		desaturate.setSaturation(0);
		imageViewRGB.setEffect(desaturate);
	}

	public void getColors() {

		// Source:
		// http://java-buddy.blogspot.ie/2012/12/retrieve-color-components-from-image.html

		try {
			imageViewRGB.setImage(null);
			PixelReader pixelReader = image.getPixelReader();

			int width = (int) image.getWidth();
			int height = (int) image.getHeight();

			// Copy from source to destination pixel by pixel
			WritableImage writableImage = new WritableImage(width, height);
			PixelWriter pixelWriter = writableImage.getPixelWriter();

			writableImageRed = new WritableImage(width, height);
			PixelWriter pixelWriterRed = writableImageRed.getPixelWriter();

			writableImageGreen = new WritableImage(width, height);
			PixelWriter pixelWriterGreen = writableImageGreen.getPixelWriter();

			writableImageBlue = new WritableImage(width, height);
			PixelWriter pixelWriterBlue = writableImageBlue.getPixelWriter();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Color color = pixelReader.getColor(x, y);
					pixelWriter.setColor(x, y, color);

					double red = color.getRed();
					double green = color.getGreen();
					double blue = color.getBlue();

					pixelWriterRed.setColor(x, y, new Color(red, 0.0, 0.0, 1.0));
					pixelWriterGreen.setColor(x, y, new Color(0.0, green, 0.0, 1.0));
					pixelWriterBlue.setColor(x, y, new Color(0.0, 0.0, blue, 1.0));

				}
			}

			imageViewRed.setImage(writableImageRed);
			imageViewBlue.setImage(writableImageBlue);
			imageViewGreen.setImage(writableImageGreen);
			imageViewGreyScale.setImage(image);
			makeGrey();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Image Error");
			alert.setHeaderText("No Image Found!");
			alert.setContentText("Please open an image to view its RGB Channels");
			alert.showAndWait();
		}
	}

	public void loadRed() {
		makeColor();
		imageViewRGB.setImage(writableImageRed);
	}

	public void loadGreen() {
		makeColor();
		imageViewRGB.setImage(writableImageGreen);
	}

	public void loadBlue() {
		makeColor();
		imageViewRGB.setImage(writableImageBlue);
	}

	public void loadGrey() {
		makeGrey();
		imageViewRGB.setImage(image);
	}

	public void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("By Luke Fox");
		alert.setContentText("1. Open an image in the 'File' menu"
				+ "\n2. To view RGB channels, click 'Image' menu then select the \n   'RGB Channels' Tab."
				+ "\n3. Check the 'Multiple Images tab to view every image you've \n   added. ");
		alert.showAndWait();
	}

	public void exit() {
		Platform.exit();
	}

	// Source:
	// http://java-buddy.blogspot.ie/2013/07/javafx-drag-and-move-something.html

	EventHandler<MouseEvent> paneOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Circle) (t.getSource())).getTranslateX();
			orgTranslateY = ((Circle) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> paneOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			((Circle) (t.getSource())).setTranslateX(newTranslateX);
			((Circle) (t.getSource())).setTranslateY(newTranslateY);
		}
	};

//	public void fillCanvasBlack() {
//		Group root = new Group();
//		Canvas canvas = new Canvas(500, 500);
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//
//		int width = (int) image.getWidth();
//		int height = (int) image.getHeight();
//
//		PixelReader pixelReader = image.getPixelReader();
//		WritableImage writableImage = new WritableImage(height, width);
//
//		int coordX = 0;
//		int coordY = 0;
//		for (int i = 0; i < 3; i++) {
//
//			for (int y = 0; y < 100; y++) {
//				for (int x = 0; x < 100; x++) {
//					Color color = pixelReader.getColor(x, y);
//					gc.setFill(color);
//					gc.fillRect(coordX, coordY, 100, 100);
//
//				}
//
//				coordX = coordX + 200;
//			}
//			coordY = 100;
//			coordX = 100;
//			for (int j = 0; j < 3; j++) {
//				gc.fillRect(coordX, coordY, 100, 100);
//				// canvas.getChildren().add(rect);
//				coordX = coordX + 200;
//			}
//			coordY = 200;
//			coordX = 0;
//			for (int k = 0; k < 3; k++) {
//				gc.fillRect(coordX, coordY, 100, 100);
//				// canvas.getChildren().add(rect);
//				coordX = coordX + 200;
//			}
//			coordY = 300;
//			coordX = 100;
//			for (int l = 0; l < 3; l++) {
//				gc.fillRect(coordX, coordY, 100, 100);
//				// canvas.getChildren().add(rect);
//
//				coordX = coordX + 200;
//			}
//
//			blurImageStackPane.getChildren().addAll(canvas);
//		}
//	}
//
//	public void fillCanvasWhite() {
//		Group root = new Group();
//		Canvas canvas = new Canvas(500, 500);
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//
//		int coordX = 100;
//		int coordY = 0;
//		gc.setFill(Color.WHITE);
//		for (int i = 0; i < 3; i++) {
//			gc.fillRect(coordX, coordY, 100, 100);
//			coordX = coordX + 200;
//		}
//		coordY = 100;
//		coordX = 0;
//		for (int j = 0; j < 3; j++) {
//			gc.fillRect(coordX, coordY, 100, 100);
//			// canvas.getChildren().add(rect);
//			coordX = coordX + 200;
//		}
//		coordY = 200;
//		coordX = 100;
//		for (int k = 0; k < 3; k++) {
//			gc.fillRect(coordX, coordY, 100, 100);
//			// canvas.getChildren().add(rect);
//			coordX = coordX + 200;
//		}
//		coordY = 300;
//		coordX = 0;
//		for (int l = 0; l < 3; l++) {
//			gc.fillRect(coordX, coordY, 100, 100);
//			// canvas.getChildren().add(rect);
//
//			coordX = coordX + 200;
//		}
//
//		blurImageStackPane.getChildren().addAll(canvas);
//	}

//	public void fillCanvas() {
//		fillCanvasBlack();
//		fillCanvasWhite();
//	}

}
