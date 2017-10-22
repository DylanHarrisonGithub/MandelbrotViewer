import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class BufferedJPanel extends Observer {

	private BufferedImage img;
	private int[] pixBuffer;
	private int panelResX;
	private int panelResY;
	private int panelHalfResX;
	private int panelHalfResY;
	private int bufferResX;
	private int bufferResY;
	private int bufferHalfResX;
	private int bufferHalfResY;
	private double bufferUnitsPerPanelUnitsX;
	private double bufferUnitsPerPanelUnitsY;
	private int mx;
	private int my;
	private boolean fixedRes;
	public int backgroundColor = 0xff000000;
	public int foregroundColor = 0xff0000ff;
	private ComponentAdapter resizeListener;
	
	public BufferedJPanel() {
		
		fixedRes = false;
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		pixBuffer = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		updateBufferedJPanelResolutionInfo();

		addMouseMotionListener(new MouseAdapter() {
	        public void mouseMoved(MouseEvent e) {
	        	mx = (int) (e.getX()*bufferUnitsPerPanelUnitsX);
	        	my = (int) (e.getY()*bufferUnitsPerPanelUnitsY);
	        }
	        	        
		});
		
		addResizeListener();
		
	}
	
	public BufferedJPanel(int fixedResX, int fixedResY) {
		
		if (fixedResX < 1)
			fixedResX = 1;
		if (fixedResY < 1)
			fixedResY = 1;
		fixedRes = true;
		bufferResX = fixedResX;
		bufferResY = fixedResY;
		bufferHalfResX = bufferResX/2;
		bufferHalfResY = bufferResY/2;
		updateBufferedJPanelResolutionInfo();
		panelResX = 1;
		panelResY = 1;
		
		img = new BufferedImage(bufferResX, bufferResY, BufferedImage.TYPE_INT_ARGB);
		pixBuffer = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		addMouseMotionListener(new MouseAdapter() {
	        public void mouseMoved(MouseEvent e) {
	        	mx = (int) (e.getX()*bufferUnitsPerPanelUnitsX);
	        	my = (int) (e.getY()*bufferUnitsPerPanelUnitsY);
	        }
	        	        
		});
		
		addResizeListener();
				
	}
	
	private void addResizeListener() {
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				
				updateBufferedJPanelResolutionInfo();
				
				if (!fixedRes) {
					BufferedImage oldImg = img;
					img = new BufferedImage(panelResX, 
										panelResY, 
										BufferedImage.TYPE_INT_ARGB);
					pixBuffer = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
					cls();
					img.getGraphics().drawImage(oldImg, 0,  0,  null);
				}
	
			}			
		});
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (fixedRes) {
			g.drawImage(img.getScaledInstance(panelResX, panelResY, Image.SCALE_FAST), 0, 0, null);
		} else {
			g.drawImage(img, 0, 0, null);
		}		

	}

	public int[] getPixBuffer() {
		return pixBuffer;
	}

	public int getBufferResX() {
		return bufferResX;
	}

	public int getBufferResY() {
		return bufferResY;
	}

	public int getBufferHalfResX() {
		return bufferHalfResX;
	}

	public int getBufferHalfResY() {
		return bufferHalfResY;
	}

	public double getBufferUnitsPerPanelUnitsX() {
		return bufferUnitsPerPanelUnitsX;
	}

	public double getBufferUnitsPerPanelUnitsY() {
		return bufferUnitsPerPanelUnitsY;
	}

	public int getMx() {
		return mx;
	}

	public int getMy() {
		return my;
	}
	
	public void cls(int color) {
		
		Arrays.fill(pixBuffer, color);
		
	}
	
	public void cls() {
		
		Arrays.fill(pixBuffer, backgroundColor);
	
	}
	
	public void pSet(int x, int y, int c) {
		int offset = x + y*bufferResX;
		
		if (offset < (bufferResX*bufferResY)) {
			pixBuffer[offset] = c;
		}
	}
	
	public int pGet(int x, int y) {
		
		int offset = x + y*bufferResX;
		int pix = -1;
		
		if (offset < (bufferResX*bufferResY)) {
			pix = pixBuffer[offset];
		}
		
		return pix;
	}
	
	private void updateBufferedJPanelResolutionInfo() {
		
		panelResX = getWidth();
		panelResY = getHeight();
		panelHalfResX = panelResX / 2;
		panelHalfResY = panelResY / 2;
		
		if (!fixedRes) {
			bufferResX = panelResX;
			bufferResY = panelResY;
			bufferHalfResX = panelHalfResX;
			bufferHalfResY = panelHalfResY;			
		} 
		
		bufferUnitsPerPanelUnitsX = ((double) bufferResX) / ((double) panelResX);
		bufferUnitsPerPanelUnitsY = ((double) bufferResY) / ((double) panelResY);
	}

	public Graphics getBufferGraphics() {
		return img.getGraphics();
	}

}
