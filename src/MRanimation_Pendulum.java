
import java.awt.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import javax.swing.*;

public class MRanimation_Pendulum extends JPanel implements Runnable, MouseListener
 {

    private double angle = Math.PI / 2;
    private int length;
    private int BREAK = 20;
    public static double SPEED = 0.1;     //Start up Speed.
    public static double CURRENT_SPEED = 0.1;

    public MRanimation_Pendulum(int length) {
        this.length = length;
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        int anchorX = getWidth() / 2, anchorY = getHeight() / 4;
        int ballX = anchorX + (int) (Math.sin(angle) * length);
        int ballY = anchorY + (int) (Math.cos(angle) * length);
        g.drawLine(anchorX, anchorY, ballX, ballY);
        g.fillOval(anchorX - 3, anchorY - 4, 7, 7);
        g.fillOval(ballX - 7, ballY - 7, 14, 14);
    }

    public void swing() {
        double angleAccel, angleVelocity = 0;
        while (true) {
            angleAccel = -9.81 / length * Math.sin(angle);
            angleVelocity += angleAccel * SPEED;
            angle += angleVelocity * SPEED;
            repaint();
            try {
                Thread.sleep(BREAK);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    @Override
    public void run() {
        this.addMouseListener(this);
        swing();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * length + 50, length / 2 * 3);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Pendulum");
        MRanimation_Pendulum p = new MRanimation_Pendulum(200);
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
        new Thread(p).start();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                switch (ke.getID()) {
                    case KeyEvent.KEY_RELEASED:
                        //Up Arrow Key Preesed - Speed up up to 1.
                        if (ke.getKeyCode() == 38) {
                            if (SPEED <= 1) {
                                SPEED += 0.1;
                                CURRENT_SPEED = SPEED;
                            }
                        }
                        //Down Arrow Key Preesed - Speed Down up to 0.2.
                        if (ke.getKeyCode() == 40) {
                            if (SPEED > 0.1) {
                                SPEED -= 0.1;
                                CURRENT_SPEED = SPEED;
                            }
                        }
                        break;
                }                
                return true;
            }
        });

    }
    
    //Overridden Methods from MouseListener Interface
	//These methods are called automatically when corresponding mouse
	//event occurs 
	/////////////////////////////////////////////////
	public void mousePressed(MouseEvent e) 
	{            
	    if(e.getButton() == MouseEvent.BUTTON1)
	    {
                SPEED = 0;     //Speed zero for pause.
	    }	    
	    else if(e.getButton() == MouseEvent.BUTTON3)
	    {
                SPEED = CURRENT_SPEED;     //Againg assign to current speed for play.
	    }
	}
	
	public void mouseReleased(MouseEvent e)
	{}
	public void mouseEntered(MouseEvent e) 
	{}
	public void mouseExited(MouseEvent e) 
	{}
	public void mouseClicked(MouseEvent e) 
	{}

}
