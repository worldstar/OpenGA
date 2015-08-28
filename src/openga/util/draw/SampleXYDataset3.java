package openga.util.draw;
import org.jfree.data.AbstractSeriesDataset;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.XYDataset;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SampleXYDataset3 extends SampleXYDataset2 {
  public SampleXYDataset3() {
  }

  /** The series count. */
  private static final int DEFAULT_SERIES_COUNT = 1;

  /** The item count. */
  private static final int DEFAULT_ITEM_COUNT = 5;

  /** The range. */
  private static final double DEFAULT_RANGE = 200;

  /** The x values. */
  private Double[][] xValues;

  /** The y values. */
  private Double[][] yValues;

  /** The number of series. */
  private int seriesCount;

  /** The number of items. */
  private int itemCount;

  /** The minimum domain value. */
  private Number domainMin;

  /** The maximum domain value. */
  private Number domainMax;

  /** The minimum range value. */
  private Number rangeMin;

  /** The maximum range value. */
  private Number rangeMax;

  /** The range of the domain. */
  private Range domainRange;

  /** The range. */
  private Range range;


  /**
   * Creates a dataset.
   * @param itemCount  the number of items.
   */
  public SampleXYDataset3(int series, int itemCount, double _dataXY[][], double[][] paretoSet, double _data3[][], double domainValues[]){
    seriesCount = series;
    this.itemCount = itemCount;
    this.xValues = new Double[seriesCount][itemCount];
    this.yValues = new Double[seriesCount][itemCount];

    double minX = Double.POSITIVE_INFINITY;
    double maxX = Double.NEGATIVE_INFINITY;
    double minY = Double.POSITIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;

    //assign X and Y values
    for(int i = 0 ; i < _dataXY.length ; i ++){
      xValues[0][i] = new Double(_dataXY[i][0]);
      yValues[0][i] = new Double(_dataXY[i][1]);
    }

    //set domain values
    //setDomain(3300, 3800, 0, 1500);
    setDomain(domainValues);

    //set the pareto optimal values
    //paretoSet
    for(int i = 0 ; i < paretoSet.length ; i ++){
      xValues[1][i] = new Double(paretoSet[i][0]);
      yValues[1][i] = new Double(paretoSet[i][1]);
    }

    for(int i = 0 ; i < _data3.length ; i ++){
      xValues[2][i] = new Double(_data3[i][0]);
      yValues[2][i] = new Double(_data3[i][1]);
    }
  }

}