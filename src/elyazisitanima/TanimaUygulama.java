package elyazisitanima;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Canvas;
import jpen.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jfree.data.category.*;
import org.jfree.data.xy.*;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TanimaUygulama extends JFrame {

	private JPanel contentPane;
	private JTextField txtktAlan;
	private JLabel gizKatSayLbl;
	private JLabel gKatNodeSayýLabel;
	private JLabel learningRateLabel;
	private JLabel inputNumLabel;
	private JLabel outputNumLabel;
	private JTextField EpochSayýsýTextField;
	private JTextField batchSizeTextField;
	private RenderingHints renderingHints;
	private Stroke stroke = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);

	int x = -1, y = -1;
	double yazýKalýn = 10;
	MNISTReader reader;

	BufferedImage image;
	Graphics2D graphics2D;
	Canvas girdi;
	double[] imageData;
	MLNNetwork firstNetwork;
	private JTextField grfkAralýktxtfield;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TanimaUygulama frame = new TanimaUygulama();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TanimaUygulama() {

		if (girdi == null) {
			/*
			 * Map<Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();
			 * hintsMap.put(RenderingHints.KEY_RENDERING,
			 * RenderingHints.VALUE_RENDER_QUALITY);
			 * hintsMap.put(RenderingHints.KEY_DITHERING,
			 * RenderingHints.VALUE_DITHER_ENABLE);
			 * hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING,
			 * RenderingHints.VALUE_TEXT_ANTIALIAS_ON); renderingHints = new
			 * RenderingHints(hintsMap);
			 */
			image = new BufferedImage(/* girdi.getWidth() */200, /* girdi.getHeight() */ 180,
					BufferedImage.TYPE_INT_ARGB);
			graphics2D = (Graphics2D) image.createGraphics();
		}

		imageData = new double[28 * 28];

		reader = new MNISTReader("datasets//train-labels.idx1-ubyte", "datasets//train-images.idx3-ubyte");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1321, 477);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtktAlan = new JTextField();
		txtktAlan.setHorizontalAlignment(SwingConstants.CENTER);
		txtktAlan.setText("\u00C7\u0131kt\u0131 Alan\u0131");
		txtktAlan.setBounds(1011, 33, 280, 280);
		contentPane.add(txtktAlan);
		txtktAlan.setColumns(10);

		Canvas girdi = new Canvas();
		girdi.setForeground(Color.BLUE);
		girdi.setBackground(Color.WHITE);
		girdi.setBounds(35, 33, 280, 280);
		image = new BufferedImage(girdi.getWidth(), girdi.getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics2D = (Graphics2D) image.createGraphics();
		contentPane.add(girdi);
		girdi.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (x == -1) {
					x = e.getX();
					y = e.getY();
					yazýKalýn = 20;
				}

				girdi.getGraphics().fillOval(x, y, (int) yazýKalýn, (int) yazýKalýn);
				draw(e.getPoint());

				x = e.getX();
				y = e.getY();
			}
		});

		JButton kurButton = new JButton("A\u011EI KUR");
		kurButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					graphics2D.drawImage(image, 0, 0, girdi.getWidth(), girdi.getHeight(), girdi);
					ImageIO.write(image, "png", new File("temp\\deneme.jpeg"));

					firstNetwork = new MLNNetwork(Integer.parseInt(gizKatSayLbl.getText()) - 1,
							Integer.parseInt(gKatNodeSayýLabel.getText()), 784, 10,
							Double.parseDouble(learningRateLabel.getText()),
							Integer.parseInt(EpochSayýsýTextField.getText()),
							Integer.parseInt(batchSizeTextField.getText()), reader, reader);
					firstNetwork.setupNetwork();

				} catch (Exception exception) {
					// kod
				}
			}
		});
		kurButton.setBounds(785, 66, 201, 48);
		contentPane.add(kurButton);

		JButton egitButton = new JButton("A\u011EI E\u011E\u0130T");
		egitButton.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				egitButton.setText("Eðitim Devam Ediyor...");
			}

			@Override
			public void focusLost(FocusEvent e) {
				egitButton.setText("A\u011EI E\u011E\u0130T");
			}
		});

		egitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				firstNetwork.train();
				firstNetwork.test();

				String accuracy = "ACCURACY:" + Double.toString(firstNetwork.getAccuracy());
				
				int sensibitlity = Integer.parseInt(grfkAralýktxtfield.getText());
				int[] numFalsClass = firstNetwork.getNumFalseByClass();
				int[] numTrueClass = firstNetwork.getNumTrueByClass();
				CostEpoch_XYLineChart_AWT chart = new CostEpoch_XYLineChart_AWT("EPOCHS-COST VALUE", "EPOCHS-COST",
						firstNetwork.getNumberOfEpoch(), firstNetwork.getCostMeanSqr(),
						firstNetwork.getCostCrossEntropy(), sensibitlity);
				chart.pack();
				RefineryUtilities.centerFrameOnScreen(chart);
				chart.setVisible(true);

				BarChart_AWT chart2 = new BarChart_AWT("Class Statistic", accuracy,numFalsClass, numTrueClass);
				chart2.pack();
				RefineryUtilities.centerFrameOnScreen(chart2);
				chart2.setVisible(true);
			}
		});

		egitButton.setBounds(785, 149, 201, 48);
		contentPane.add(egitButton);

		JButton okuButton = new JButton("OKU");
		okuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*
				 * try {
				 * 
				 * graphics2D.drawImage(image, 0, 0, girdi.getWidth(), girdi.getHeight(),
				 * girdi); ImageIO.write(image, "png", new File("temp\\deneme.jpeg"));
				 * 
				 * } catch (Exception tempE) { // kod }
				 */
				String result = firstNetwork.classifyMNIST(imageData);
				txtktAlan.setText(result);

			}
		});
		okuButton.setBounds(70, 330, 215, 35);
		contentPane.add(okuButton);

		gizKatSayLbl = new JLabel("-");
		gizKatSayLbl.setBounds(702, 66, 56, 16);
		contentPane.add(gizKatSayLbl);

		JSlider layerSayisiSlider = new JSlider();
		layerSayisiSlider.setValue(2);
		layerSayisiSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				gizKatSayLbl.setText("" + layerSayisiSlider.getValue());
			}
		});
		layerSayisiSlider.setMaximum(500);
		layerSayisiSlider.setMinimum(1);
		layerSayisiSlider.setBounds(490, 66, 200, 26);
		contentPane.add(layerSayisiSlider);

		gKatNodeSayýLabel = new JLabel("-");
		gKatNodeSayýLabel.setBounds(702, 115, 56, 16);
		contentPane.add(gKatNodeSayýLabel);

		JSlider hiddenNodeSlider = new JSlider();
		hiddenNodeSlider.setValue(16);
		hiddenNodeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				gKatNodeSayýLabel.setText("" + hiddenNodeSlider.getValue());
			}
		});
		hiddenNodeSlider.setMaximum(1500);
		hiddenNodeSlider.setMinimum(9);
		hiddenNodeSlider.setBounds(490, 115, 200, 26);
		contentPane.add(hiddenNodeSlider);

		inputNumLabel = new JLabel("784");
		inputNumLabel.setBounds(490, 218, 56, 16);
		contentPane.add(inputNumLabel);

		outputNumLabel = new JLabel("10");
		outputNumLabel.setBounds(490, 258, 56, 16);
		contentPane.add(outputNumLabel);

		learningRateLabel = new JLabel("-");
		learningRateLabel.setBounds(702, 181, 56, 16);
		contentPane.add(learningRateLabel);

		JSlider learningRateSlider = new JSlider();
		learningRateSlider.setValue(20);
		learningRateSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				learningRateLabel.setText("" + ((double) learningRateSlider.getValue() / 1000));
			}
		});
		learningRateSlider.setMaximum(5000);
		learningRateSlider.setBounds(490, 171, 200, 26);
		contentPane.add(learningRateSlider);

		EpochSayýsýTextField = new JTextField();
		EpochSayýsýTextField.setText("100");
		EpochSayýsýTextField.setBounds(490, 291, 116, 22);
		contentPane.add(EpochSayýsýTextField);
		EpochSayýsýTextField.setColumns(10);

		batchSizeTextField = new JTextField();
		batchSizeTextField.setText("1");
		batchSizeTextField.setBounds(490, 326, 116, 22);
		contentPane.add(batchSizeTextField);
		batchSizeTextField.setColumns(10);

		JLabel lblGizliKatmanSays = new JLabel("G\u0130ZL\u0130 KATMAN SAYISI:");
		lblGizliKatmanSays.setBounds(321, 76, 130, 16);
		contentPane.add(lblGizliKatmanSays);

		JLabel lblHiddenKatmanNode = new JLabel("G. KATMAN NODE SAYISI:");
		lblHiddenKatmanNode.setBounds(321, 125, 150, 16);
		contentPane.add(lblHiddenKatmanNode);

		JLabel lblLearnngRate = new JLabel("LEARNING RATE:");
		lblLearnngRate.setBounds(321, 181, 130, 16);
		contentPane.add(lblLearnngRate);

		JLabel lblInputSays = new JLabel("INPUT SAYISI:");
		lblInputSays.setBounds(321, 218, 130, 16);
		contentPane.add(lblInputSays);

		JLabel lblOutputNodeSays = new JLabel("OUTPUT NODE SAYISI:");
		lblOutputNodeSays.setBounds(321, 258, 137, 16);
		contentPane.add(lblOutputNodeSays);

		JLabel lblEpochSays = new JLabel("EPOCH SAYISI:");
		lblEpochSays.setBounds(321, 294, 130, 16);
		contentPane.add(lblEpochSays);

		JLabel lblMnbatchBoyutu = new JLabel("MINI-BATCH BOYUTU:");
		lblMnbatchBoyutu.setBounds(321, 332, 130, 16);
		contentPane.add(lblMnbatchBoyutu);
		JLabel lblGrafikAralkSays = new JLabel("GRAF\u0130K HASSAS\u0130YET\u0130");
		lblGrafikAralkSays.setBounds(321, 361, 150, 16);
		contentPane.add(lblGrafikAralkSays);

		grfkAralýktxtfield = new JTextField();
		grfkAralýktxtfield.setText("1");
		grfkAralýktxtfield.setBounds(490, 358, 116, 22);
		contentPane.add(grfkAralýktxtfield);
		grfkAralýktxtfield.setColumns(10);

		JButton btnTemizle = new JButton("Temizle");
		btnTemizle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				imageData = Array.fillZero(imageData);

				girdi.getGraphics().clearRect(0, 0, girdi.getWidth(), girdi.getHeight());
			}
		});
		btnTemizle.setBounds(100, 378, 145, 26);
		contentPane.add(btnTemizle);
	}

	public void draw(Point point) {
		imageData[((int) (point.y / 10)) * 28 + (int) (point.x / 10)] = 0.99;
		imageData[((int) ((point.y / 10) - 1)) * 28 + (int) ((point.x / 10) - 1)] = 0.99;
		imageData[((int) ((point.y / 10) + 1)) * 28 + (int) ((point.x / 10) + 1)] = 0.99;// piksele yazdýrýlacak deðer
		graphics2D.dispose();
	}

}
