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
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The scattor plot for the three data set.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ScatterPlot2 extends ScatterPlot {
  public ScatterPlot2(String title) {
    super(title);
    this.title = title;
  }
  double data3[][];

  public void drawMethod(){
    //double domainValues[] = new double[]{3300, 3800, 0, 1200};
    double domainValues[] = new double[]{minX, maxX, minY, maxY};
    //XYDataset data = new SampleXYDataset2(XY.length, paretoSet, domainValues);
    XYDataset data = new SampleXYDataset3(3, XY.length, XY, paretoSet, data3, domainValues);

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

  public void setXYData(double _XY[][], double data3[][]){
    this.XY = _XY;
    this.data3 = data3;
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


}