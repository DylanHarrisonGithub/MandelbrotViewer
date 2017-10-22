import java.math.BigDecimal;

public class Complex {

	public double a, b;
	
	public Complex() {
	}
	
	public Complex(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	public double getQuadrance() {
		return a*a + b*b;
	}
	
	public double getRadius() {
		return Math.sqrt(a*a + b*b);
	}
	
	public double getAngle() {
		return Math.atan2(b, a);
	}
	
	public void addReal(double r) {
		a += r;
	}
	
	public void addImaginary(double i) {
		b += i;
	}
	
	public void addComplex(Complex Z) {
		a += Z.a;
		b += Z.b;
	}
	
	public void multiplyReal(double r) {
		a *= r;
		b *= r;
	}
	
	public void multiplyImaginary(double i) {
		
		double tempA;
		
		tempA = -b*i;
		b = a*i;
		a = tempA;
		
	}
	
	public void multiplyComplex(Complex Z) {

		double tempA;
		
		tempA = a*Z.a - b*Z.b;
		b = a*Z.b + b*Z.a;
		a = tempA;
		
	}
	
	public void square() {
		
		double tempA;
		
		tempA = a*a - b*b;
		b = 2*a*b;
		a = tempA;
		
	}
	
	public void exponentiateReal(double r) {
		
		double radius = getRadius();
		double theta = getAngle()*r;
		
		radius = Math.pow(radius, r);
		a = radius*Math.cos(theta);
		b = radius*Math.sin(theta);
		
	}
	
	public void exponentiateImaginary(double i) {
		
		double radius = Math.pow(Math.E,-getAngle()*i);
		double theta = i*Math.log(getRadius());

		a = radius*Math.cos(theta);
		b = radius*Math.sin(theta);
		
	}
	
	public void exponentiateComplex(Complex Z) {
		
		double rad = getRadius();
		double ang = getAngle();
		
		double radius = Math.pow(rad, Z.a) * Math.pow(Math.E, -ang*Z.b);
		double theta = Math.log(rad)*Z.b + ang*Z.a;
		
		a = radius*Math.cos(theta);
		b = radius*Math.sin(theta);

	}
	
}
