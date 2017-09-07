/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class ObjFunctionPFSSOAWTWithTOUTariffs {
    private double on_peakPrice = 0.1327;
    private double mid_peakPrice = 0.0750;
    private double off_peakPrice = 0.0422;
    
    private int[] power = new int[]{20,30,15,10,15,30,10,20,10,10};
    
    private Date startTime;
    private Date endTime;
    
    /**
     * @param args the command line arguments
     */
    public void setData(String startTime,String endTime) throws ParseException
    {
        SimpleDateFormat simple = new SimpleDateFormat();
        simple.applyPattern("HH:mm");
        this.startTime = simple.parse(startTime);
        this.endTime = simple.parse(endTime);
    }
    
    public void calculation(int machine) throws ParseException
    {
        double start;
        double end;
        double totalPrice;
        
        start = (int) (startTime.getTime()/(1000*60));
        end = (int) (endTime.getTime()/(1000*60));
        totalPrice = 0.0;
        int period =(int) (endTime.getTime() - startTime.getTime()) /(1000*60);
//                         System.out.println(period);
        if(start < -60)//Off_peak
        {
            if(end > -60)
            {
                totalPrice += ( (((-60 - start)  / 60 )* off_peakPrice) * (((-60 - start)  / period ) * power[machine]) ) ;
                start = -60;
            }else
            {
                totalPrice += ( (((end - start) / 60 )* off_peakPrice) * (((end - start)  / period ) * power[machine]) );
            }
        }
        if(start >= -60 && start < 420)//Mid-peak 07:00 ~ 15:00
        {
            if(end > 420)
            {
                totalPrice += ( (((420 - start) / 60 )* mid_peakPrice) * (((420 - start)  / period ) * power[machine]) );
                start = 420;
            }else
            {
                totalPrice += ( (((end - start) / 60 )* mid_peakPrice) * (((end - start)  / period ) * power[machine]) );
            }
        }
        if(start >= 420 && start < 720)//On-peak 15:00 ~ 20:00
        {
            if(end > 720)
            {
                totalPrice += ( (((720 - start) / 60 )* on_peakPrice) * (((720 - start)  / period ) * power[machine]) );
                start = 720;
            }else
            {
                totalPrice += ( (((end - start) / 60 )* on_peakPrice) * (((end - start)  / period ) * power[machine]) );
            }
        }
        if(start >= 720 && start < 840)//Mid-peak 20:00 ~ 22:00
        {
            if(end > 840)
            {
                totalPrice += ( (((840 - start) / 60 )* mid_peakPrice) * (((840 - start)  / period ) * power[machine]) );
                start = 840;
            }else
            {
                totalPrice += ( (((end - start) / 60 )* mid_peakPrice) * (((end - start)  / period ) * power[machine]) );
            }
        }
        if(start >= 840)//Off_peak
        {
            {
                totalPrice += ( (((end - start) / 60 )* off_peakPrice) * (((end - start)  / period ) * power[machine]) );
            }
        }
    System.out.println(machine + " : " + totalPrice);


//        System.out.println((endTime.getTime()-startTime.getTime()) / (60 * 1000));
//        System.out.println((startTime.getTime()/(1000*60)));
    }
    
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        ObjFunctionPFSSOAWTWithTOUTariffs TouT = new ObjFunctionPFSSOAWTWithTOUTariffs();
        
        TouT.setData("05:00", "8:00");
        TouT.calculation(1);
    }
    
}
