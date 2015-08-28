package openga.util.draw;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.Legend;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ScatterPlot extends ApplicationFrame{
  double[][] XY, paretoSet;
  String title;

  public ScatterPlot(String title) {
    super(title);
    this.title = title;
  }

  double minX, maxX;
  double minY, maxY;

  public void drawMethod(){
    //double domainValues[] = new double[]{5300, 6200, 250, 4500};
    //double domainValues[] = new double[]{2000, 2900, 0, 2000};//20/20
    double domainValues[] = new double[]{minX, maxX, minY, maxY};
    //XYDataset data = new SampleXYDataset2(XY.length, paretoSet, domainValues);
    XYDataset data = new SampleXYDataset2(2, XY.length, XY, paretoSet, domainValues);

    JFreeChart chart = ChartFactory.createScatterPlot(
        title,
        "X", "Y",
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    Legend legend = chart.getLegend();
    if (legend instanceof StandardLegend) {
        StandardLegend sl = (StandardLegend) legend;
        sl.setDisplaySeriesShapes(true);
    }
    NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
    domainAxis.setAutoRangeIncludesZero(false);
    domainAxis.setLowerBound(domainValues[0]);
    domainAxis.setUpperBound(domainValues[1]);
    //domainAxis.setMinimumAxisValue(10);
    domainAxis.setUpperMargin(domainValues[0]);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
    chartPanel.setVerticalAxisTrace(true);
    chartPanel.setHorizontalAxisTrace(true);
    chartPanel.setVerticalZoom(true);
    chartPanel.setHorizontalZoom(true);
    setContentPane(chartPanel);

    pack();
    RefineryUtilities.centerFrameOnScreen(this);

  }

  public void setXYData(double _XY[][]){
    this.XY = _XY;
    //openga.util.printClass printClass1 = new openga.util.printClass();
    openga.util.algorithm.getMin min1 = new openga.util.algorithm.getMin();
    openga.util.algorithm.getMax max1 = new openga.util.algorithm.getMax();
    minX = min1.setData(XY, 0);
    maxX  = max1.setData(XY, 0);
    minY = min1.setData(XY, 1);
    maxY = max1.setData(XY, 1);

    minX -= (maxX - minX)*0.1;
    minY -= (maxY - minY)*0.1;
    maxX += (maxX - minX)*0.8;
    maxY += (maxY - minY)*0.8;

    if(minX < 0){
      minX = 0;
    }

    if(minY < 0){
      minY = 0;
    }
  }

  public void setParetoSetData(double _paretoSetData[][]){
    this.paretoSet = _paretoSetData;
  }

  public static void main(String[] args) {
    ScatterPlot demo = new ScatterPlot("Plot all points");
    double testObjs[][] = {{20,32},{25,30},{27,23},{40,32},{38,25}};
    demo.setXYData(testObjs);
    demo.drawMethod();
    demo.setVisible(true);

  }

}