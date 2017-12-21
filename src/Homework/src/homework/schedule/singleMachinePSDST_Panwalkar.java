package homework.schedule;

public class singleMachinePSDST_Panwalkar {
    
    int n;   // Jobs
    int r;   // Mid_value of jobs
    int t[]; // Processing_Time
    int p[]; // Penalties
    double b; 

    public static void main(String[] args) {
        int[] t = {3, 4, 6, 9, 14, 18, 20};
        int[] p = {5, 11, 18};
        singleMachinePSDST_Panwalkar a = new singleMachinePSDST_Panwalkar();        
        a.setParameters(t, p, 4, 0.05);
        a.evaluateAll();
    }
    
    public void setParameters(int[] t, int[] p, int r, double b) {
        this.n = t.length; // jobs
        this.t = t; // jobs & processing_time
        this.p = p; // penalties
        this.r = r; // mid_value of jobs              
        this.b = b;    
    }

    public void evaluateAll() {
        
        double Equation1,Equation2,Equation3;
        double[] Gamma = new double[n];

        for (int i = 1; i <= Gamma.length; i++) {
            if (i==1){ // 1
                Gamma[0] = n*p[0];
            }
            else if (2 <= i && i <= r) { // 2~4
                Equation1 = 0; 
                for (int l=i+1;l<=r;l++){
                    Equation1 += (l-1);
                }
                Equation2 = 0;
                for (int l=r+1;l<=n;l++){
                    Equation2 += (n+1-l);
                }
                Gamma[i-1] = n*p[0]+((i-1)+b*(Equation1+Equation2))*p[1];                
            } else { // 5~7
                Equation3 = 0;
                for (int l = i+1; l <= n; l++) {
                    Equation3 += (n + 1 - l);
                }
                Gamma[i-1] = ((n+1-i) + b*Equation3) * p[2];
            }
            System.out.print(Math.rint(Gamma[i-1]*100)/100 + "  ");
        }
        System.out.println();
    }
}
