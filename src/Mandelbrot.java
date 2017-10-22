import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.*;


public class Mandelbrot extends JFrame {

	Complex Z, C;
	double exponent, exponentSquared;
	boolean isRendering;
	
	
	BufferedJPanel bufferedObserver = new BufferedJPanel() {
		
		Point2D.Double worldCoord = new Point2D.Double();
		int iterations;
		Graphics buffer;
		
		@Override
		public void paintComponent(Graphics g) {
			
			isRendering = true;
			bufferedObserver.cls();
			exponentSquared = exponent*exponent;
			for (int y = 0; y < bufferedObserver.getBufferResY(); y++) {

				for (int x = 0; x < bufferedObserver.getBufferResX(); x++) {
					
					worldCoord.x = x;
					worldCoord.y = y;
					worldCoord = bufferedObserver.screenToWorldCoords(worldCoord);

					iterations = 0;
					
					C.a = worldCoord.x;
					C.b = worldCoord.y;
					Z.a = 0;
					Z.b = 0;
					
					while ((Z.getQuadrance() < exponentSquared) && (iterations < 256)) {
						
						//Z.b *= -1;
						Z.exponentiateReal(exponent);
						Z.addComplex(C);
						iterations++;
						
					}

					//iterations = 255 - iterations;
					
					iterations = generateInterpolatedColor(bufferedObserver.backgroundColor, 
							0xff105010
							,
							((double) iterations) * 0.00390625, 255);
					
					//iterations = 0;
					//if (Z.getRadius() < 2) {
					//	int c1 = generateInterpolatedColor(0x00ff0000, 0x0000ff00, (Z.a + 2.0)/4, 255);
					//	int c2 = generateInterpolatedColor(0x000080ff, 0x000000ff, (Z.a + 2.0)/4, 255);
					//	int cf = generateInterpolatedColor(c1, c2, (Z.b + 2.0)/4, 255);
						
					//	iterations = cf;
					//}

					bufferedObserver.getPixBuffer()[x + y*bufferedObserver.getBufferResX()] = 0xff000000 | iterations; //((int) (Math.random()*16777215));
					
				}
			
			}
			//buffer = bufferedObserver.getBufferGraphics();
			//buffer.setColor(Color.BLACK);
			//buffer.drawString("Coordinates: " + bufferedObserver.getOrigin().x + ", " + bufferedObserver.getOrigin().y, 8, 16);
			//buffer.drawString("Zoom: " + bufferedObserver.getZoom().x + ", " + bufferedObserver.getZoom().y, 8, 30);
			//buffer.drawString("Orientation: " + bufferedObserver.getTheta(), 8, 44);
			
			super.paintComponent(g);
			isRendering = false;
			//exponent += 0.94567;
		}
	};
	
	public Mandelbrot() {
		
		Z = new Complex();
		C = new Complex();
		exponent = 2.0;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Mandelbrot set viewer");
		setSize(875, 356);
		add(bufferedObserver);
		bufferedObserver.backgroundColor = 0xffefefff;
		bufferedObserver.zoom(1200, 1200);
		setVisible(true);
		
		Timer increaseExponentTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isRendering) {
					//exponent += 0.00005;
					//bufferedObserver.repaint();
					//exponentSquared = exponent*exponent;
				}
				//System.out.println(exponent);
			}
			
		});
		increaseExponentTimer.start();
		
	}

	public static void main(String[] args) {

		new Mandelbrot();
		
	}
	
	public static int generateInterpolatedColor(int c1, int c2, double t, int alpha) {
		
		int c1R = (c1 >> 16) & 0xFF;
		int c1G = (c1 >> 8) & 0xFF;
		int c1B = c1 & 0xFF;
		int c2R = (c2 >> 16) & 0xFF;
		int c2G = (c2 >> 8) & 0xFF;
		int c2B = c2 & 0xFF;
		int r;
		int g;
		int b;
		
		r = c1R + (int) (t * (c2R - c1R));
		g = c1G + (int) (t * (c2G - c1G));
		b = c1B + (int) (t * (c2B - c1B));
		
		return ((alpha & 0xFF) << 24) + ((r & 0xFF) << 16) + ((g & 0xFF) << 8) + (b & 0xFF);

	}

}
