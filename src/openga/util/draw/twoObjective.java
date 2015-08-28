package openga.util.draw;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.
 * The most different characteristic of the component is to provide the multiobjective function.
 * Besides, it will build rich callable libraries, which is in the three operators.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class twoObjective extends Applet {
  private boolean isStandalone = false;
  PopCanvas popCanvas;

  protected void insertComponent(Component component,
      GridBagLayout gridbag, GridBagConstraints constr) {
      gridbag.setConstraints(component,constr);
          add(component);
  }


  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public twoObjective() {
  }
  //Initialize the applet
  public void init() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception {
    setBackground(Color.white);
    setForeground(Color.black);
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setFont(new Font("Timee New Romance",Font.PLAIN,10));
    setLayout(gridbag);
    c.fill = GridBagConstraints.NONE;
    //To set the West anchor
    c.gridwidth = 1;
    c.gridheight = 8;
    c.weightx = 1.0;
    c.insets = new Insets(0,10,0,10);
    c.anchor = GridBagConstraints.WEST;



    c.fill = GridBagConstraints.NONE;
    c.weightx = 0.0;
    c.anchor = GridBagConstraints.EAST;


  }

  public void paint(Graphics g) {
      // Dimension dim = getSize(); // actually not implemented in Netscape
      Dimension dim = new Dimension(400,400);
      double xScale = (new Integer(dim.width - 20)).doubleValue() /
          (new Integer(10)).doubleValue();
      double yScale = (new Integer(dim.height - 20)).doubleValue() /
          (new Integer(10)).doubleValue();
      //g.setFont(textFont);
      g.setColor(Color.black);
      g.drawString("profit",2,10);
      g.drawString("weight",dim.width-35,dim.height-120);
      int[] x1Points = {5,dim.width-40};
      int[] y1Points = {dim.height-40,dim.height-110};
      Polygon poly = new Polygon(x1Points,y1Points,x1Points.length);
      g.drawPolygon(poly);


  }

  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }
  //Main method
  public static void main(String[] args) {
    twoObjective applet = new twoObjective();
    applet.isStandalone = true;
    Frame frame;
    frame = new Frame();
    frame.setTitle("To draw 2-dimension objectives");
    frame.add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(400,400);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }
}
class PopCanvas extends Canvas {
    static final int radius = 5;
    //Population pop;
    Font textFont;
/*
    public PopCanvas(Population population) {
        setForeground(Color.black);
        setBackground(Color.white);
        textFont = new Font("Helvetica",Font.ITALIC,9);
        pop = population;
    }
*/
    public Dimension preferredSize() {
        return new Dimension(400,400);
    }

    public Dimension minimumSize() {
        return new Dimension(400,400);
    }

    private void plotPop(Graphics g, int xOffset, int yOffset,
        double xScale, double yScale) {
        g.setColor(Color.blue);

    }

    public void paint(Graphics g) {

    }
}
