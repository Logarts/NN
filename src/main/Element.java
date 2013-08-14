package main;

public class Element {
	double health;
	double knife;
	double gun;
	double enemy;
	double out[];
	Element() {
		out = new double[Net.OUTPUT_NEURONS];
	}
	public void set(double h, double k, double g, double e, double a, double r, double w, double hh) {
		health = h;
		knife = k;
		gun = g;
		enemy = e;
		out[0] = a;
		out[1] = r;
		out[2] = w;
		out[3] = hh;
	}
}
