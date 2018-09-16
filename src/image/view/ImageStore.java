package image.view;

import javafx.scene.image.Image;

public class ImageStore {

	public String ID;
	public Image image;
	public String imageName;
	public long imageSize;
	public double imageDimensionsX;
	public double imageDimensionsY;

	public ImageStore(String ID, Image image, String imageName, long imageSize, double imageDimensionsX,
			double imageDimensionsY) {
		this.ID = ID;
		this.image = image;
		this.imageName = imageName;
		this.imageSize = imageSize;
		this.imageDimensionsX = imageDimensionsX;
		this.imageDimensionsY = imageDimensionsY;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public long getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}

	public double getImageDimensionsX() {
		return imageDimensionsX;
	}

	public void setImageDimensionsX(int imageDimensionsX) {
		this.imageDimensionsX = imageDimensionsX;
	}

	public double getImageDimensionsY() {
		return imageDimensionsY;
	}

	public void setImageDimensionsY(int imageDimensionsY) {
		this.imageDimensionsY = imageDimensionsY;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
