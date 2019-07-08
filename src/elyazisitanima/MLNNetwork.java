package elyazisitanima;

public class MLNNetwork {

	private int numberOfLayer; // hiddenlayerlarýn sayýsý
	private int numberOfHiddenNode; // gizli katman nöron sayýsý
	private int numberOfInputNode; // input katmaný nöron sayýsý
	private int numberOfOutputNode; // output katmaný nöron sayýsý-softmaxkatmaný-

	private double learningRate; //
	private int numberOfEpoch; // Epoch sayýsý
	private int miniBatchSize; // mini-batch boyutu
	private int numberOfSampleInOneCycle;
	private double cost;

	private MNISTReader test;
	private MNISTReader training;

	private double[][][] hiddenWeights;// gizli katmanlarýn aðýrlýklarý
	private double[][] inputWeights;// ilk katmanýn aðýrlýklarý
	private double[][] outputWeights;// çýktý katmanýnýn -softmax- aðýrlýklarý

	private double[][][] deltaOfHiddenWeights;
	private double[][] deltaOfInputWeights;
	private double[][] deltaOfOutputWeights;

	private double[][] inputs; // eðitimde kullanýlacak girdiler
	private double[][] desiredOutputs;

	private double[][] testInputs;
	private double[][] testDesiredOutputs;

	private double[][][] sumValues;// her katmanda girdilerin aðýrlýkla çarpýlýp biasla toplanan deðerlerini
									// kaydediyoruz
	private double[][][] activatedValues;// her katmanda sumValues'in
	private double[][] outLSumValues;
	private double[][] outLActivatedValues;// hesaplanan output deðeri

	private double[][][][] localGradyanOfHiddenWeights;
	private double[][][] localGradyanOfInputWeights;
	private double[][][] localGradyanOfOutputWeights;

	private double[][] bias;
	private double[] outputBias;

	private double[][] deltaOfBias;
	private double[] deltaOutputLayerBias;

	private double accuracy;
	private int[] numTrueByClass;

	private int[] numFalseByClass;
	private double[] costCrossEntropy;
	private double[] costMeanSqr;

	public MLNNetwork(int numberOfLayer, int numberOfHiddenNode, int numberOfInputNode, int numberOfOutputNode,
			double learningRate, int numberOfEpoch, int miniBatchSize, MNISTReader test, MNISTReader training) {
		super();
		this.numberOfLayer = numberOfLayer;
		this.numberOfHiddenNode = numberOfHiddenNode;
		this.numberOfInputNode = numberOfInputNode;
		this.numberOfOutputNode = numberOfOutputNode;
		this.learningRate = learningRate;
		this.numberOfEpoch = numberOfEpoch;
		this.numberOfSampleInOneCycle = 1;
		this.miniBatchSize = miniBatchSize;
		this.test = test;
		this.training = training;
		System.out.println("NN oluþturuldu.");
		System.out.println("layer sayýsý:" + numberOfLayer);
		System.out.println("gizli katmandaki node sayýsý:" + numberOfHiddenNode);
		System.out.println("input sayýsý:" + numberOfInputNode);
		System.out.println("output sayýsý :" + numberOfOutputNode);
		System.out.println("learning rate :" + learningRate);
		System.out.println("epoch sayýsý :" + numberOfEpoch);
		System.out.println("mini-batch boyutu :" + miniBatchSize);
	}

	public void setupNetwork() {
		numberOfInputNode = training.getNumRows() * training.getNumCols();
		numberOfOutputNode = 10; // Consracturede dýþardan istenecek

		bias = Array.random(numberOfLayer + 1, numberOfHiddenNode, 0.0, 2.0);
		outputBias = Array.random(numberOfOutputNode, 0.0, 2.0);

		sumValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
		activatedValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
		outLSumValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];
		outLActivatedValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];

		hiddenWeights = Array.random(numberOfLayer, numberOfHiddenNode, numberOfHiddenNode, 0.0, 1.0);
		inputWeights = Array.random(numberOfInputNode, numberOfHiddenNode, 0.0, 1.0);
		outputWeights = Array.random(numberOfHiddenNode, numberOfOutputNode, 0.0, 1.0);

		deltaOfHiddenWeights = new double[numberOfLayer][numberOfHiddenNode][numberOfHiddenNode];
		deltaOfInputWeights = new double[numberOfInputNode][numberOfHiddenNode];
		deltaOfOutputWeights = new double[numberOfHiddenNode][numberOfOutputNode];
		deltaOfBias = new double[numberOfLayer+1][numberOfHiddenNode];
		deltaOutputLayerBias = new double[numberOfOutputNode];

		inputs = training.getNormalizedImageData();
		desiredOutputs = training.getFormatedLabelData();

		localGradyanOfHiddenWeights = new double[numberOfLayer][numberOfSampleInOneCycle][numberOfHiddenNode][numberOfHiddenNode];
		localGradyanOfInputWeights = new double[numberOfInputNode][numberOfSampleInOneCycle][numberOfHiddenNode];
		localGradyanOfOutputWeights = new double[numberOfHiddenNode][numberOfSampleInOneCycle][numberOfOutputNode];

		costCrossEntropy = new double[numberOfEpoch];
		costMeanSqr = new double[numberOfEpoch];

		System.out.println("Network kuruldu ");
	}

	public void train() {
		double[][] tempInput;
		double[][] tempDOutput;

		for (int i = 0; i < numberOfEpoch; i++) {
			tempInput = Array.partialCopy(inputs, (i * numberOfSampleInOneCycle) % 60000, numberOfSampleInOneCycle);
			tempDOutput = Array.partialCopy(desiredOutputs, (i * numberOfSampleInOneCycle) % 60000,
					numberOfSampleInOneCycle);

			for (int b = 0; b < miniBatchSize; b++) {

				if (i == 0) {
					System.out.println("ilk epoch forward propagation basladý");
				}

				// FORWARD PROPAGATION

				// input layer FP
				sumValues[0] = Array.add(Array.dot(tempInput, inputWeights), bias[0]);
				activatedValues[0] = Array.sigmoid(sumValues[0]);

				if (i == 0) {
					System.out.println("ilk epoch inputlayer deðerleri hesaplandý");
				}

				// hidden layers FP
				for (int j = 1; j <= numberOfLayer; j++) {
					sumValues[j] = Array.add(Array.dot(activatedValues[j - 1], hiddenWeights[j - 1]), bias[j]);
					activatedValues[j] = Array.sigmoid(sumValues[j]);
				}

				// output layer FP
				outLSumValues = Array.add(Array.dot(activatedValues[numberOfLayer], outputWeights), outputBias);
				// outLActivatedValues = Array.softmax(outLSumValues);
				outLActivatedValues = Array.sigmoid(outLSumValues);

				
				
				
				if (i % 1 == 0) {
					System.out.println((i + 1) + ". epoch cost deðeri :" + cost);
				}

				// BACK PROPAGATION

				localGradyanOfOutputWeights = outputGradyanHesapla(numberOfHiddenNode, outLActivatedValues,
						tempDOutput);

				localGradyanOfHiddenWeights = localGradyanHesapla(numberOfLayer, numberOfLayer, outputWeights,
						hiddenWeights, localGradyanOfOutputWeights, localGradyanOfHiddenWeights, activatedValues);

				localGradyanOfInputWeights = inputGradyanHesapla(numberOfInputNode, activatedValues, hiddenWeights,
						localGradyanOfHiddenWeights);

				deltaOfOutputWeights = Array.add(deltaOfOutputWeights,
						deltaOutputHesapla(learningRate, activatedValues[numberOfLayer], localGradyanOfOutputWeights));

				deltaOfHiddenWeights = Array.add(deltaOfHiddenWeights,
						deltaHiddenHesapla(learningRate, activatedValues, localGradyanOfHiddenWeights));

				deltaOfInputWeights = Array.add(deltaOfInputWeights, deltaInputHesapla(learningRate, tempInput, localGradyanOfInputWeights));

				deltaOfBias = Array.add(deltaOfBias,deltaOfBiasHesapla(learningRate, localGradyanOfHiddenWeights, localGradyanOfInputWeights,
						hiddenWeights));
				
				deltaOutputLayerBias = Array.add(deltaOutputLayerBias, deltaOutputLayerBiasHesapla(learningRate, localGradyanOfOutputWeights,
						outputWeights));

				if(b == miniBatchSize-1)
				{
				costCrossEntropy[i] = Array.cross_entropy(numberOfSampleInOneCycle, tempDOutput, outLActivatedValues);
				costMeanSqr[i] = Array.costMeanSq(tempDOutput, outLActivatedValues);

				cost = Array.cross_entropy(numberOfSampleInOneCycle, tempDOutput, outLActivatedValues);
				
				}

				sumValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
				activatedValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
				outLSumValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];
				outLActivatedValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];

				localGradyanOfHiddenWeights = new double[numberOfLayer][numberOfSampleInOneCycle][numberOfHiddenNode][numberOfHiddenNode];
				localGradyanOfInputWeights = new double[numberOfInputNode][numberOfSampleInOneCycle][numberOfHiddenNode];
				localGradyanOfOutputWeights = new double[numberOfHiddenNode][numberOfSampleInOneCycle][numberOfOutputNode];

			}

			hiddenWeights = Array.subtract(hiddenWeights, deltaOfHiddenWeights);
			inputWeights = Array.subtract(inputWeights, deltaOfInputWeights);
			outputWeights = Array.subtract(outputWeights, deltaOfOutputWeights);
			bias = Array.subtract(bias, deltaOfBias);
			outputBias = Array.subtract(outputBias, deltaOutputLayerBias);

			deltaOfHiddenWeights = new double[numberOfLayer][numberOfHiddenNode][numberOfHiddenNode];
			deltaOfInputWeights = new double[numberOfInputNode][numberOfHiddenNode];
			deltaOfOutputWeights = new double[numberOfHiddenNode][numberOfOutputNode];
			deltaOfBias = new double[numberOfLayer + 1][numberOfHiddenNode];
			deltaOutputLayerBias = new double[numberOfOutputNode];

		}

		// test esnasýnda ayný deðiþkenler kullanýldýðýndan problem çýkmamasý için
		// koydum tekrardan bakýp düzelt belki gerek olmayabilir.

		sumValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
		activatedValues = new double[numberOfLayer + 1][numberOfSampleInOneCycle][numberOfHiddenNode];
		outLSumValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];
		outLActivatedValues = new double[numberOfSampleInOneCycle][numberOfOutputNode];

		deltaOfHiddenWeights = new double[numberOfLayer][numberOfHiddenNode][numberOfHiddenNode];
		deltaOfInputWeights = new double[numberOfInputNode][numberOfHiddenNode];
		deltaOfOutputWeights = new double[numberOfHiddenNode][numberOfOutputNode];

		localGradyanOfHiddenWeights = new double[numberOfLayer][numberOfSampleInOneCycle][numberOfHiddenNode][numberOfHiddenNode];
		localGradyanOfInputWeights = new double[numberOfInputNode][numberOfSampleInOneCycle][numberOfHiddenNode];
		localGradyanOfOutputWeights = new double[numberOfHiddenNode][numberOfSampleInOneCycle][numberOfOutputNode];

		deltaOfBias = new double[numberOfLayer + 1][numberOfHiddenNode];
		deltaOutputLayerBias = new double[numberOfOutputNode];
	}

	public void test() {
		testInputs = test.getNormalizedImageData();
		testDesiredOutputs = test.getFormatedLabelData();
		double[][] tempInput;
		double[][] tempDOutput;
		double numWroClass = 0;
		double numCorClass = 0;
		numTrueByClass = new int[10];
		numFalseByClass = new int[10];

		for (int i = 0; i < 10000; i++) {
			tempInput = Array.partialCopy(testInputs, i, 1);
			tempDOutput = Array.partialCopy(testDesiredOutputs, i, 1);

			sumValues = new double[numberOfLayer + 1][1][numberOfHiddenNode];
			activatedValues = new double[numberOfLayer + 1][1][numberOfHiddenNode];
			outLSumValues = new double[1][numberOfOutputNode];
			outLActivatedValues = new double[1][numberOfOutputNode];

			sumValues[0] = Array.add(Array.dot(tempInput, inputWeights), bias[0]);
			activatedValues[0] = Array.sigmoid(sumValues[0]);

			for (int j = 1; j <= numberOfLayer; j++) {
				sumValues[j] = Array.add(Array.dot(activatedValues[j - 1], hiddenWeights[j - 1]), bias[j]);
				activatedValues[j] = Array.sigmoid(sumValues[j]);
			}

			outLSumValues = Array.add(Array.dot(activatedValues[numberOfLayer], outputWeights), outputBias);
			// outLActivatedValues = Array.softmax(outLSumValues);
			outLActivatedValues = Array.sigmoid(outLSumValues);

			int calcClass = Array.findMaxValIndex(outLActivatedValues[0]);
			int trueClass = Array.findMaxValIndex(tempDOutput[0]);

			if (calcClass == trueClass) {
				numTrueByClass[trueClass]++;
				numCorClass++;
			} else {
				numFalseByClass[trueClass]++;
				numWroClass++;
			}

		}
		accuracy = numCorClass / 10000;

	}

	public String classifyMNIST(double[] sample) {
		double[][] tempInput = new double[1][sample.length];

		sumValues = new double[numberOfLayer + 1][1][numberOfHiddenNode];
		activatedValues = new double[numberOfLayer + 1][1][numberOfHiddenNode];
		outLSumValues = new double[1][numberOfOutputNode];
		outLActivatedValues = new double[1][numberOfOutputNode];

		for (int i = 0; i < sample.length; i++) {
			tempInput[0][i] = sample[i];
		}

		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				System.out.print(sample[i * 28 + j]);
			}
			System.out.println("");
		}

		sumValues[0] = Array.add(Array.dot(tempInput, inputWeights), bias[0]);
		activatedValues[0] = Array.sigmoid(sumValues[0]);

		for (int j = 1; j <= numberOfLayer; j++) {
			sumValues[j] = Array.add(Array.dot(activatedValues[j - 1], hiddenWeights[j - 1]), bias[j]);
			activatedValues[j] = Array.sigmoid(sumValues[j]);
		}

		outLSumValues = Array.add(Array.dot(activatedValues[numberOfLayer], outputWeights), outputBias);
		outLActivatedValues = Array.softmax(outLSumValues);

		switch (Array.findMaxValIndex(outLActivatedValues[0])) {
		case 0:
			return "0";
		case 1:
			return "1";
		case 2:
			return "2";
		case 3:
			return "3";
		case 4:
			return "4";
		case 5:
			return "5";
		case 6:
			return "6";
		case 7:
			return "7";
		case 8:
			return "8";
		case 9:
			return "9";
		}
		return "---";
	}

	private double[][] deltaOfBiasHesapla(double learnRate, double[][][][] locGradHid, double[][][] locGradIn,
			double[][][] hiddenw) {

		int nLayer = locGradHid.length;
		int nBatch = locGradHid[0].length;
		int nhidden = locGradHid[0][0].length;

		double[][] dBias = new double[nLayer + 1][nhidden];

		for (int i = 0; i <= nLayer; i++)
			for (int j = 0; j < nBatch; j++) {
				for (int k = 0; k < nhidden; k++) {
					if (i == 0) {
						dBias[i][k] += learnRate * locGradIn[j][0][k];
					} else {
						dBias[i][k] += learnRate * locGradHid[i - 1][j][0][k];
					}
				}
			}

		return dBias;
	}

	private double[] deltaOutputLayerBiasHesapla(double learnRate, double[][][] locGradOut, double[][] outputW) {

		int nBatch = locGradOut.length;
		int nOut = locGradOut[0][0].length;

		double[] dBias = new double[nOut];

		for (int i = 0; i < nBatch; i++) {
			for (int j = 0; j < nOut; j++) {
				dBias[j] += locGradOut[i][0][j];// tüm satýrlarýn j. elemaný ayný
			}
		}

		return Array.multiply(learnRate, dBias);
	}

	private double[][] deltaInputHesapla(double learnRate, double[][] tInput, double[][][] locGradIn) {
		int nBatch = locGradIn.length;
		int nInputNode = locGradIn[0].length;
		int nHiddenNode = locGradIn[0][0].length;

		double[][] deltaInput = new double[nInputNode][nHiddenNode];

		for (int k = 0; k < nBatch; k++) {
			for (int i = 0; i < nInputNode; i++) {
				for (int j = 0; j < nHiddenNode; j++) {
					deltaInput[i][j] += learnRate * locGradIn[k][i][j] * tInput[k][i];
				}
			}
		}

		return deltaInput;
	}

	private double[][][] deltaHiddenHesapla(double learnRate, double[][][] activatedVals, double[][][][] locGradHid) {
		int nlayer = locGradHid.length;
		int nHiddenNode = locGradHid[0][0].length;
		int nBatch = locGradHid[0].length;

		double[][][] deltaHidden = new double[nlayer][nHiddenNode][nHiddenNode];

		for (int i = 0; i < nlayer; i++) {
			for (int k = 0; k < nBatch; k++) {
				for (int j = 0; j < nHiddenNode; j++) {
					for (int z = 0; z < nHiddenNode; z++) {
						deltaHidden[i][j][z] += learnRate * locGradHid[i][k][j][z] * activatedVals[i][k][j];
					}
				}
			}
		}

		return deltaHidden;
	}

	private double[][] deltaOutputHesapla(double learnRate, double[][] activatedVals, double[][][] locGradOut) {
		int mBatch = locGradOut.length;
		int numberOfHiddenNode = locGradOut[0].length;
		int numOut = locGradOut[0][0].length;

		double[][] deltaOutput = new double[numberOfHiddenNode][numOut];

		// deltaOutput = Array.multiply(learnRate, Array.multiply( activatedVals[0],
		// locGradOut));
		for (int h = 0; h < mBatch; h++) {
			for (int i = 0; i < numberOfHiddenNode; i++) {
				for (int j = 0; j < numOut; j++) {
					deltaOutput[i][j] += learnRate * activatedVals[h][i] * locGradOut[h][i][j];
				}
			}
		}

		return deltaOutput;
	}

	private double[][][] outputGradyanHesapla(int numHid, double[][] outActivated, double[][] outDesired) {

		int numOut = outActivated[0].length; // number of outputs
		int mBatch = outActivated.length;

		double[][] derivativeOfSoftMax;
		double[][] dSoftMultipyError;
		double[][][] gradyan = new double[mBatch][numHid][numOut];

		double[][] error = Array.subtract(outActivated, outDesired);

		derivativeOfSoftMax = Array.derivativeSigmoid(outActivated, "sigmoid türevi düzenlendi");
		dSoftMultipyError = Array.multiply(derivativeOfSoftMax, error);

		for (int i = 0; i < mBatch; i++) {
			for (int j = 0; j < numHid; j++) {
				for (int k = 0; k < numOut; k++) {
					gradyan[i][j][k] = dSoftMultipyError[i][k];
				}
			}
		}

		return gradyan;
	}

	private double[][][][] localGradyanHesapla(int indexLayer, int numberOfLayer, double[][] outWeights,
			double[][][] hidWeights, double[][][] locGradyanOW, double[][][][] locGradyanHW,
			double[][][] activatedVal) {
		if (indexLayer == 0) {
			return locGradyanHW;
		}
		if (indexLayer == numberOfLayer) {

			int nBatch = locGradyanHW[0].length; // number of batch size
			int nHidden = locGradyanHW[0][0].length; // number of hidden node
			int nOutput = locGradyanOW[0][0].length; // number of output node

			for (int i = 0; i < nBatch; i++) {
				for (int j = 0; j < nHidden; j++) {
					for (int k = 0; k < nHidden; k++) {
						for (int l = 0; l < nOutput; l++) {
							locGradyanHW[indexLayer - 1][i][j][k] += (locGradyanOW[i][k][l] * outWeights[k][l]);
						}
					}
				}
				locGradyanHW[indexLayer - 1][i] = Array.multiply(
						Array.derivativeSigmoid(activatedVal[indexLayer][i], "düzenle!!!!"),
						locGradyanHW[indexLayer - 1][i]);
			}
			return localGradyanHesapla(indexLayer - 1, numberOfLayer, outWeights, hidWeights, locGradyanOW,
					locGradyanHW, activatedVal);
		}

		int nBatch = locGradyanHW[0].length; // number of batch size
		int nHidden = locGradyanHW[0][0].length; // number of hidden node

		for (int i = 0; i < nBatch; i++) {
			for (int j = 0; j < nHidden; j++) {
				for (int k = 0; k < nHidden; k++) {
					for (int l = 0; l < nHidden; l++) {
						locGradyanHW[indexLayer - 1][i][j][k] += (locGradyanHW[indexLayer][i][k][l])
								* (hidWeights[indexLayer][k][l]);
					}
				}
			}
			locGradyanHW[indexLayer - 1][i] = Array.multiply(
					Array.derivativeSigmoid(activatedVal[indexLayer][i], "düzenleeee!!"),
					locGradyanHW[indexLayer - 1][i]);
		}
		return localGradyanHesapla(indexLayer - 1, numberOfLayer, outWeights, hidWeights, locGradyanOW, locGradyanHW,
				activatedVal);
	}

	private double[][][] inputGradyanHesapla(int nInput, double[][][] actVals, double[][][] hidWeights,
			double[][][][] locGradyanHW) {

		int mBatch = locGradyanHW[0].length;
		int nHidden = locGradyanHW[0][0].length;

		double[][][] inputGradyan = new double[mBatch][nInput][nHidden];
		double[][] derivativeSum = Array.derivativeSigmoid(actVals[0], "düzenle!!!!!!!!!!");

		for (int i = 0; i < mBatch; i++) {
			for (int j = 0; j < nInput; j++) {
				for (int k = 0; k < nHidden; k++) {
					for (int l = 0; l < nHidden; l++) {
						inputGradyan[i][j][k] += locGradyanHW[0][i][k][l] * hidWeights[0][k][l];
					}
					inputGradyan[i][j][k] = inputGradyan[i][j][k] * derivativeSum[i][k];
				}
			}
		}
		return inputGradyan;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double[] getCostCrossEntropy() {
		return costCrossEntropy;
	}

	public double[] getCostMeanSqr() {
		return costMeanSqr;
	}

	public int getNumberOfEpoch() {
		return numberOfEpoch;
	}

	public int[] getNumTrueByClass() {
		return numTrueByClass;
	}

	public int[] getNumFalseByClass() {
		return numFalseByClass;
	}
}
