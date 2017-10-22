import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Observer extends JPanel {

	private Point2D.Double origin;
	private Point2D.Double zoom;
	private Point2D.Double res;
	private Point2D.Double halfRes;
	private Point2D.Double mouseScreen;
	private Point2D.Double mouseObserver;
	private Point2D.Double mouseWorld;
	private double theta;
	private Point2D.Double zoomRCP;
	private double sinTheta;
	private double cosTheta;
	public KeyListener keyControl;
	private static double twoPi = Math.PI * 2.0;
	private static double halfPi = Math.PI * 0.5;
	private boolean rbDown = false;
	
	
	public Observer() {
		
		origin = new Point2D.Double(-1.5, 0.0);
		zoom = new Point2D.Double(1.0, 1.0);
		zoomRCP = new Point2D.Double(1.0, 1.0);
		mouseScreen = new Point2D.Double();
		mouseObserver = new Point2D.Double();
		mouseWorld = new Point2D.Double();
		res = new Point2D.Double();
		halfRes = new Point2D.Double();
		theta = 0;
		sinTheta = Math.sin(theta);
		cosTheta = Math.cos(theta);
		
		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {
				JPanel frame = (JPanel) arg0.getComponent();
				res.x = frame.getWidth();
				res.y = frame.getHeight();
				halfRes.x = res.x*0.5;
				halfRes.y = res.y*0.5;
			}
			@Override
			public void componentShown(ComponentEvent arg0) {
				JFrame frame = (JFrame) arg0.getComponent();
				res.x = frame.getWidth();
				res.y = frame.getHeight();
				halfRes.x = res.x*0.5;
				halfRes.y = res.y*0.5;
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
	        public void mouseMoved(MouseEvent e) {
	        	mouseScreen.x = e.getX();
	        	mouseScreen.y = e.getY();
	        	
	        	mouseObserver = screenToObserverCoords(mouseScreen);
	        	mouseWorld = screenToWorldCoords(mouseScreen);
	    	    System.out.println("" + mouseWorld.x + ", " + mouseWorld.y);
	        }
		});
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (arg0.getButton() == arg0.BUTTON1) {
					mouseScreen.x = arg0.getX();
					mouseScreen.y = arg0.getY();
	        	
					mouseObserver = screenToObserverCoords(mouseScreen);
					mouseWorld = screenToWorldCoords(mouseScreen);
					origin.x = mouseWorld.x;
					origin.y = mouseWorld.y;
				
					repaint();
				}
	        }
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == arg0.BUTTON3) {
					rbDown = true;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getButton() == arg0.BUTTON3) {
					rbDown = false;					
				}
			}
			
		});
		
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				
				
				if (!rbDown) {
					double z = Math.signum(arg0.getPreciseWheelRotation());
					if (z > 0) {
						zoom(0.9, 0.9);				
					} else if (z < 0) {
						zoom(1.1, 1.1);
					}				
					repaint();
				} else {
					double r = Math.signum(arg0.getPreciseWheelRotation());
					rotate(r*Math.PI*0.005);
					repaint();					
				}
			}
			
		});
		
		keyControl = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				char c = arg0.getKeyChar();			
				
				switch (c) {
				
				case 'a':
					rotate(0.25);
					repaint();
					//System.out.println("keypress");
					break;
				
				case 'd':
					rotate(-0.25);
					repaint();
					break;
					
				case 'w':
					origin.x += Math.cos(theta + halfPi) * 10;
					origin.y += Math.sin(theta + halfPi) * 10;
					repaint();
					break;
					
				case 's':
					origin.x -= Math.cos(theta + halfPi) * 10;
					origin.y -= Math.sin(theta + halfPi) * 10;
					repaint();
					break;
					
				case 'q':
					zoom(2.0, 1.0);
					repaint();
					break;
					
				case 'e':
					zoom(1.0, 2.0);
					repaint();
					break;
					
				case 'z':
					zoom(0.5, 1.0);
					repaint();
					break;
				case 'c':
					zoom(1.0, 0.5);
					repaint();
					break;
					

				}
				

			}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		};

	}
	
	public Point2D.Double screenToObserverCoords(Point2D.Double screenCoord) {
		
		double x;
		double y;
		
		x = (screenCoord.x - halfRes.x) * zoomRCP.x;
		y = -(screenCoord.y - halfRes.y) * zoomRCP.y;
		
		return new Point2D.Double(x, y);
		
	}
	
	public Point2D.Double screenToWorldCoords(Point2D.Double screenCoord) {
		
		double x;
		double y;
		
		x = (((screenCoord.x - halfRes.x)) * cosTheta - (-(screenCoord.y - halfRes.y)) * sinTheta) * zoomRCP.x + origin.x;
		y = ((-(screenCoord.y - halfRes.y)) * cosTheta + ((screenCoord.x - halfRes.x)) * sinTheta) * zoomRCP.y + origin.y;
		
		return new Point2D.Double(x, y);
	}
	
	public Point2D.Double observerToScreenCoords(Point2D.Double observerCoord) {
	
			double x;
			double y;
			
			x = (observerCoord.x * zoom.x) + halfRes.x;
			y = -(observerCoord.y * zoom.y) + halfRes.y;
			
			return new Point2D.Double(x, y);
	}	
	
	public Point2D.Double worldToScreenCoords(Point2D.Double worldCoord) {
		
		double x;
		double y;
		
		x = (worldCoord.x - origin.x)*zoom.x*cosTheta + (worldCoord.y - origin.y)*zoom.y*sinTheta + halfRes.x;
		y = -((worldCoord.y - origin.y)*zoom.y*cosTheta - (worldCoord.x - origin.x)*zoom.x*sinTheta) + halfRes.y;
		
		return new Point2D.Double(x, y);
		
	}

	public void rotate(double deltaTheta) {
		
		theta += deltaTheta;
		
		if (theta > twoPi) {
			while (theta > twoPi) {
				theta -= twoPi;
			}
		}
		
		if (theta < 0.0) {
			while (theta < 0.0) {
				theta += twoPi;
			}
		}
		
		cosTheta = Math.cos(theta);
		sinTheta = Math.sin(theta);
		
	}
	
	public void zoom(double zoomFactorX, double zoomFactorY) {
		
		if (zoomFactorX != 0.0) {
			zoom.x *= zoomFactorX;
			zoomRCP.x = 1.0 / zoom.x;
		}

		if (zoomFactorY != 0.0) {
			zoom.y *= zoomFactorY;
			zoomRCP.y = 1.0 / zoom.y;
		}

		System.out.println("" + zoom.x + ", " + zoom.y );
	}

	public Point2D.Double getOrigin() {
		return origin;
	}

	public Point2D.Double getZoom() {
		return zoom;
	}

	public double getTheta() {
		return theta;
	}
	
}
