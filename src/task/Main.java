package task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

	private static final String dataFile = "data.txt";

	static ArrayList<Integer> weights = new ArrayList<>();
	static ArrayList<Integer> values = new ArrayList<>();
	static ArrayList<StringBuilder> neighbors = new ArrayList<>();

	final static int numOfEl = 30;
	static int capacity;
	static int combnum = (int) Math.pow(2, numOfEl);
	static double temperature= 150;
	
	static int sumValue = 0;
	static int sumWeight = 0;
	static double prob = Double.MIN_VALUE;
	static double randNum= Double.MIN_VALUE;

	static String bestComb = "";
	private static int maxValue = 0;
	private static int maxWeight = 0;


	public static void readFile(String dataFile) throws IOException {
		FileReader read = new FileReader(dataFile);
		BufferedReader buff = new BufferedReader(read);
		String line;
		while ((line = buff.readLine()) != null) {

			String[] itemElems = line.split(" ");

			if (itemElems.length == 1)
				capacity = Integer.parseInt(itemElems[0]);
			else {
				values.add(Integer.parseInt(itemElems[0]));
				weights.add(Integer.parseInt(itemElems[1]));

			}
		}
		buff.close();
	}
	
	public static boolean isMaxValue(int val) {
		if (val > maxValue) {
			return true;
		}
		return false;
	}

	public static boolean isInCapacity(int weight) {
		if (weight <= capacity)
			return true;
		return false;
	}

	public static String ToBinarySB(int a) {		
		StringBuilder bin = new StringBuilder();

		while (a > 0) {
			bin.append(a % 2);
			a = a / 2;
		}
		return bin.reverse().toString();

	}

	//_______________________________METHODS FOR HC AND SA 
	public static void selectRandom() {
		boolean flag = true;
		while (flag) {
			int ran = new Random().nextInt((combnum) + 1);
			String randComb = ToBinarySB(ran);
			sumWeight = 0;
			sumValue = 0;
			for (int j = 0; j < randComb.length(); j++) {
				if (randComb.charAt(j) == '1') {
					sumWeight += weights.get(j);
					sumValue += values.get(j);
				}
				if (isInCapacity(sumWeight) && randComb.length()==30) {
					maxValue= sumValue;
					maxWeight= sumWeight;
					bestComb = randComb;
					flag = false;
				}
			}

		}
		
	}
	
	public static void pickNeighbors() {
		StringBuilder tempComb = new StringBuilder(bestComb);
		for (int j = 0; j < tempComb.length(); j++) {
			if (tempComb.charAt(j) == '0') {
				tempComb.setCharAt(j, '1');
				if (!neighbors.contains(tempComb)) {
					 neighbors.add(tempComb);
				}
			}
			tempComb= new StringBuilder(bestComb);
		}
	}
	
	public static void chooseNeighbor() {
		for (StringBuilder neighbor : neighbors) {
			sumValue = 0;
			sumWeight = 0;
			for (int j = 0; j < neighbor.length(); j++) {

				if (neighbor.charAt(j) == '1') {
					sumWeight += weights.get(j);
					sumValue += values.get(j);
				}

			}
			if (isInCapacity(sumWeight) && isMaxValue(sumValue)) {
				maxValue = sumValue;
				maxWeight = sumWeight;
				bestComb = neighbor.toString();
				System.out.println(
						"current: value=" + sumValue + ", weight=" + sumWeight + "\nBest combination= " + bestComb);
			}

		}
		neighbors.clear();
	}
	
	//METHODS MADE FOR SA 
	public static void calcProbability(double curr, double cand, double temp) {
		double euler = Math.E;
		 prob = Math.pow(euler, -(curr-cand)/temp);
		 randNum = Math.random();
		
	}
	
	public static void chooseNeighborSA() {
		for (StringBuilder neighbor : neighbors) {
			sumValue = 0;
			sumWeight = 0;
			for (int j = 0; j < neighbor.length(); j++) {

				if (neighbor.charAt(j) == '1') {
					sumWeight += weights.get(j);
					sumValue += values.get(j);
				}

			}
			if (isInCapacity(sumWeight) && isMaxValue(sumValue)) {
				maxValue = sumValue;
				maxWeight = sumWeight;
				bestComb = neighbor.toString();
				System.out.println(
						"current: value=" + sumValue + ", weight=" + sumWeight + "\nBest combination= " + bestComb);
			}
			if (isInCapacity(sumWeight) && sumValue < maxValue) {
				calcProbability(maxValue, sumValue, temperature);
				if (prob > randNum) {
					maxValue = sumValue;
					maxWeight = sumWeight;
					bestComb = neighbor.toString();
					System.out.println(
							"current: value=" + sumValue + ", weight=" + sumWeight + "\nBest combination= " + bestComb + "___DECREASED");
				} else {
					temperature*=0.9;
					System.out.println("temp dropped");
				}
			}

		}
		neighbors.clear();
	}
	
	//ALGORITHM METHODS
	public static void bruteForce() {
		for (int i = 1; i < combnum; ++i) {
			String comb = new StringBuilder(ToBinarySB(i)).reverse().toString();
			sumWeight = 0;
			sumValue = 0;
			for (int j = 0; j < comb.length(); j++) {
				if (comb.charAt(j) == '1') {
					sumWeight += weights.get(j);
					sumValue += values.get(j);
				}
			}
			if (isInCapacity(sumWeight) && isMaxValue(sumValue)) {
				maxValue = sumValue;
				maxWeight = sumWeight;
				bestComb = comb;

			}
			;
		}

	}
	
	public static void hillClimbing() {
		selectRandom();
		System.out.println(bestComb +"\n\n");	
		while(true) {
			int tempMaxValue = maxValue;
			pickNeighbors();	
			chooseNeighbor();
			if(tempMaxValue == maxValue) break;
		}
	}
	public static void simulatedAnnealing() {
		selectRandom();
		System.out.println(bestComb +"\n\n");	
		while(true) {
			int tempMaxValue = maxValue;
			pickNeighbors();	
			chooseNeighborSA();
			if(tempMaxValue == maxValue) break;
		}
	}

	public static void main(String[] args) {

		long start = System.currentTimeMillis();

		try {
			readFile(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//bruteForce();		
		
		//hillClimbing();
		simulatedAnnealing();
		
	
		
		//____ending summary____
		System.out.println("_____________");
		long end = System.currentTimeMillis() - start;
		System.out.println((double) end + "ms");
		System.out.println(capacity + " capacity");
		System.out.println(maxValue + " value, " + maxWeight + " weight");
		System.out.println(bestComb);

	}

}
