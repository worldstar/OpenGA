package openga.chromosomes;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The first objective is maxization problem, the second objective is the minimization problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findParetoByObjectivesMaxMin extends findParetoByObjectives {
  public findParetoByObjectivesMaxMin() {
  }

  public boolean getObjcomparison(double _obj1[], double _obj2[]){
    boolean better = false;
    for(int i = 0 ; i < _obj1.length - 1 ; i ++ ){
      if((_obj1[i] > _obj2[i] && _obj1[i+1] <= _obj2[i+1]) ||
         (_obj1[i] >= _obj2[i] && _obj1[i+1] < _obj2[i+1])){
        better = true;
      }
      else{
        return false;
      }
    }
    return better;
  }


}