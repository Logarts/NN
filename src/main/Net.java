package main;

import java.util.Random;

public class Net {
	final static int INPUT_NEURONS = 4;
	final static int HIDDEN_NEURONS = 3;
	final static int OUTPUT_NEURONS = 4;
	final static double LEARN_RATE = 0.2;
	final static int MAX_SAMPLES = 18;
	final double RAND_WEIGHT;
	private double wih[][];
	
	private double who[][];
	
	private double inputs[];
	private double hidden[];
	private double target[];
	private double actual[];
	
	private double erro[];
	private double errh[];
		
	private Element[] samples = {new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), new Element(), };
	
	private String strings[] = {"Attack", "Run", "Wander", "Hide"};
	
	private Random rand;
	Net() {
		wih = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];
		who = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];
		inputs = new double[INPUT_NEURONS];
		hidden = new double[HIDDEN_NEURONS];
		target = new double[OUTPUT_NEURONS];
		actual = new double[OUTPUT_NEURONS];
		erro = new double[OUTPUT_NEURONS];
		errh = new double[HIDDEN_NEURONS];
		
		rand = new Random();
		RAND_WEIGHT = (rand.nextDouble() - 0.5);
		assignRandomWeights();
		assignSamples();
	}
	private double sqr(double x) {
		return x * x;
	}
	private void assignSamples() {
		//                  h  k  g  e  a  r  w  h
			samples[0].set (2, 0, 0, 0, 0, 0, 1, 0);
			samples[1].set (2, 0, 0, 1, 0, 0, 1, 0);
			samples[2].set (2, 0, 1, 1, 1, 0, 0, 0);
			samples[3].set (2, 0, 1, 2, 1, 0, 0, 0);
			samples[4].set (2, 1, 0, 2, 0, 0, 0, 1);
			samples[5].set (2, 1, 0, 1, 1, 0, 0, 0);
			
			samples[6].set (1, 0, 0, 0, 0, 0, 1, 0);
			samples[7].set (1, 0, 0, 1, 0, 0, 0, 1);
			samples[8].set (1, 0, 1, 1, 1, 0, 0, 0);
			samples[9].set (1, 0, 1, 2, 0, 0, 0, 1);
			samples[10].set(1, 1, 0, 2, 0, 0, 0, 1);
			samples[11].set(1, 1, 0, 1, 0, 0, 0, 1);
			
			samples[12].set(0, 0, 0, 0, 0, 0, 1, 0);
			samples[13].set(0, 0, 0, 1, 0, 0, 0, 1);
			samples[14].set(0, 0, 1, 1, 0, 0, 0, 1);
			samples[15].set(0, 0, 1, 2, 0, 1, 0, 0);
			samples[16].set(0, 1, 0, 2, 0, 1, 0, 0);
			samples[17].set(0, 1, 0, 1, 0, 0, 0, 1);

	}
	private void assignRandomWeights() {
		int hid, inp, out;
		for (inp = 0; inp < INPUT_NEURONS + 1; inp++) {
			for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
				wih[inp][hid] = RAND_WEIGHT;
			}
		}
		for (hid = 0; hid < HIDDEN_NEURONS + 1; hid++) {
			for (out = 0; out < OUTPUT_NEURONS; out++) {
				who[hid][out] = RAND_WEIGHT;
			}
		}
	}
	
	private double sigmoid(double value) {
		return (1.0 / (1.0 + Math.exp(-value)));
	}
	private double sigmoidDerivative (double value) {
		return (value * (1.0 - value));
	}
	
	private void feedForward() {
		int inp, hid, out;
		double sum;
		for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
			sum = 0.0;
			for (inp = 0; inp < INPUT_NEURONS; inp++) {
				sum += inputs[inp] * wih[inp][hid];
			}
			sum += wih[INPUT_NEURONS][hid];
			hidden[hid] = sigmoid(sum);
		}
		for (out = 0; out < OUTPUT_NEURONS; out++) {
			sum = 0.0;
			for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
				sum += hidden[hid] * who[hid][out];
			}
			sum += who[HIDDEN_NEURONS][out];
			actual[out] = sigmoid(sum);
		}
	}
	
	private void backPropagate() {
		int inp, hid, out;
		for (out = 0; out < OUTPUT_NEURONS; out++) {
			erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
		}
		for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
			errh[hid] = 0.0;
			for (out = 0; out < OUTPUT_NEURONS; out++) {
				errh[hid] *= erro[out] * who[hid][out];
			}
			errh[hid] *= sigmoidDerivative(hidden[hid]);
		}
		for (out = 0; out < OUTPUT_NEURONS; out++) {
			for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
				who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
			}
			who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]);
		}
		
		for (hid = 0; hid < HIDDEN_NEURONS; hid++) {
			for (inp = 0; inp < INPUT_NEURONS; inp++) {
				wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
			}
			wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]);
		}
	}
	
	
	
	public void learnNet() {
		int i;
		int sample = 0;
		int iterations = 0;
		double err;
		while(true) {
			if (++sample == MAX_SAMPLES) {
				sample = 0;
			}
			inputs[0] = samples[sample].health;
			inputs[1] = samples[sample].knife;
			inputs[2] = samples[sample].gun;
			inputs[3] = samples[sample].enemy;
			
			target[0] = samples[sample].out[0];
			target[1] = samples[sample].out[1];
			target[2] = samples[sample].out[2];
			target[3] = samples[sample].out[3];
			
			feedForward();
			
			err = 0;
			for (i = 0; i < OUTPUT_NEURONS; i++) {
				err += sqr(samples[sample].out[i] - actual[i]);
			}
			err = 0.5 * err;
			System.out.println("mse = " + err);
			if (iterations++ > 100000) {
				break;
			}
			backPropagate();
			System.out.println(iterations);
		}
	}
	
	private int action(double array[]) {
		int index, sel;
		double max;
		sel = 0;
		max = array[sel];
		for (index = 1; index < OUTPUT_NEURONS; index ++) {
			if (array[index] > max) {
				max = array[index];
				sel = index;
			}
		}
		return sel;
	}
	
	public void chekNet() {
		int i;
		int sum = 0;
		for (i = 0; i < MAX_SAMPLES; i++) {
			inputs[0] = samples[i].health;
			inputs[1] = samples[i].knife;
			inputs[2] = samples[i].gun;
			inputs[3] = samples[i].enemy;
			inputs[0] = samples[i].out[0];
			inputs[1] = samples[i].out[1];
			inputs[2] = samples[i].out[2];
			inputs[3] = samples[i].out[3];
			feedForward();
			if (action(actual) != action(target)) {
				System.out.println(inputs[0] + ", " + inputs[1] + ", " + inputs[2] + ", " + inputs[3] + ", " + strings[action(actual)] + ", " + strings[action(target)]);
			} else {
				sum++;
			}
			
		}
		System.out.println("Network " + (((float)sum / (float)MAX_SAMPLES) * 100.0) + " correct");
	}
	
	public void tests() {
		inputs[0] = 2; //Health
		inputs[1] = 1; //Knife
		inputs[2] = 1; //Gun
		inputs[3] = 1; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 1; //Health
		inputs[1] = 1; //Knife
		inputs[2] = 1; //Gun
		inputs[3] = 2; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 0; //Health
		inputs[1] = 0; //Knife
		inputs[2] = 0; //Gun
		inputs[3] = 0; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 0; //Health
		inputs[1] = 1; //Knife
		inputs[2] = 1; //Gun
		inputs[3] = 1; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 2; //Health
		inputs[1] = 0; //Knife
		inputs[2] = 1; //Gun
		inputs[3] = 3; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 2; //Health
		inputs[1] = 1; //Knife
		inputs[2] = 0; //Gun
		inputs[3] = 3; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		inputs[0] = 0; //Health
		inputs[1] = 1; //Knife
		inputs[2] = 0; //Gun
		inputs[3] = 3; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
		
		
		
	}
	public void makeTest(double h, double k, double g, double e) {
		inputs[0] = h; //Health
		inputs[1] = k; //Knife
		inputs[2] = g; //Gun
		inputs[3] = e; //Enemy
		feedForward();
		System.out.println((int)inputs[0] + " " + (int)inputs[1] + " " + (int)inputs[2] + " " + (int)inputs[3] + ", action: " + strings[action(actual)]);
	}

}
