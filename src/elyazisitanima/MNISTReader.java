package elyazisitanima;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MNISTReader {

	private int numLabels;
	private int numImages;
	private int numRows;
	private int numCols;
	private double[][] imageData;
	private double[][] normalizedImageData;
	private double[] labelData;

	public MNISTReader(String labelFilename, String imageFilename) {
		try {

			// veriler filedan buffer ediliyor.
			DataInputStream labels = new DataInputStream(new FileInputStream(labelFilename));
			DataInputStream images = new DataInputStream(new FileInputStream(imageFilename));
			int magicNumber = labels.readInt();

			// magic numaralar kontrol ediliyor..
			if (magicNumber != 2049) {
				System.out.println("Label file has wrong magic number: " + magicNumber + " (should be 2049)");
			}
			magicNumber = images.readInt();
			if (magicNumber != 2051) {
				System.out.println("Image file has wrong magic number: " + magicNumber + " (should be 2051)");
			}
			// MNIST dosyalarýndaki label ve image sayýlarý ve satýr sütun sayýlarý okunuyor
			this.numLabels = labels.readInt();
			this.numImages = images.readInt();
			this.numRows = images.readInt();
			this.numCols = images.readInt();
			if (numLabels != numImages) {
				StringBuilder str = new StringBuilder();
				str.append("Image file and label file do not contain the same number of entries.\n");
				str.append("  Label file contains: " + numLabels + "\n");
				str.append("  Image file contains: " + numImages + "\n");
				System.out.println(str.toString());
			}

			byte[] labelsByte = new byte[numLabels];
			labels.read(labelsByte);
			int imageVectorSize = numCols * numRows;
			byte[] imagesByte = new byte[numLabels * imageVectorSize];
			images.read(imagesByte);

			labelData = new double[this.numLabels];
			imageData = new double[numImages][imageVectorSize];
			normalizedImageData = new double[numImages][imageVectorSize];
			for (int i = 0, z = 0; i < this.numLabels; i++) {
				labelData[i] = (int) labelsByte[i];

				for (int j = 0; j < imageVectorSize; j++, z++) {
					imageData[i][j] = imagesByte[z] & 0xFF; // 0xFF çarpýmýný unsigned byte a dönüþüm için kullandým.
					normalizedImageData[i][j] = imageData[i][j] / 2550;
				}
			}

			images.close();
			labels.close();

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * @return the numLabels
	 */
	public int getNumLabels() {
		return numLabels;
	}

	/**
	 * @return the numImages
	 */
	public int getNumImages() {
		return numImages;
	}

	/**
	 * @return the numRows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * @return the numCols
	 */
	public int getNumCols() {
		return numCols;
	}

	/**
	 * @return the Image data
	 */
	public double[][] getImageData() {
		return imageData;
	}

	public double[][] getNormalizedImageData() {
		return normalizedImageData;
	}

	/**
	 * @return the Label data
	 */
	public double[] getLabelData() {
		return labelData;
	}

	public double[][] getFormatedLabelData() {
		double[][] formatedLabelData = new double[numLabels][10];
		for (int i = 0; i < numLabels; i++) {
			switch ((int) labelData[i]) {
			case 0:
				formatedLabelData[i][0] = 1;
				break;
			case 1:
				formatedLabelData[i][1] = 1;
				break;
			case 2:
				formatedLabelData[i][2] = 1;
				break;
			case 3:
				formatedLabelData[i][3] = 1;
				break;
			case 4:
				formatedLabelData[i][4] = 1;
				break;
			case 5:
				formatedLabelData[i][5] = 1;
				break;
			case 6:
				formatedLabelData[i][6] = 1;
				break;
			case 7:
				formatedLabelData[i][7] = 1;
				break;
			case 8:
				formatedLabelData[i][8] = 1;
				break;
			case 9:
				formatedLabelData[i][9] = 1;
				break;
			}
		}
		return formatedLabelData;
	}

}