
package homework.schedule;

public class singleMachinePSDST {
    
    int n;   // jobs
    int r;   // mid_value of jobs
    int p[]; // processing_time
    double b; 

    public static void main(String[] args) {
        int[] p = {1, 3, 6, 8, 11, 15, 21};
        singleMachinePSDST a = new singleMachinePSDST();        
        a.setParameters(p, 4, 0.05);
        a.evaluateAll();
    }
    
    public void setParameters(int[] p, int r, double b) {
        this.n = p.length; // jobs
        this.r = r; // mid_value of jobs
        this.p = p; // jobs & processing_time
        this.b = b;         
    }

    public void evaluateAll() {
        
        double Equation1,Equation2,Equation3;
        double[] Gamma = new double[n];

        for (int i = 1; i <= Gamma.length; i++) {
            if (1 <= i && i <= r) { // 1~4
                Equation1 = 0; 
                for (int l=i+1;l<=r;l++){
                    Equation1 += (l-1);
                }
                Equation2 = 0;
                for (int l=r+1;l<=n;l++){
                    Equation2 += (n+1-l);
                }
                Gamma[i-1] = (i-1)+b*(Equation1+Equation2);                
            } else { // 5~7
                Equation3 = 0;
                for (int l = i+1; l <= n; l++) {
                    Equation3 += (n + 1 - l);
                }
                Gamma[i-1] = (n + 1 - i) + b * Equation3;
            }
            System.out.print(Math.rint(Gamma[i-1]*100)/100 + " ");
        }
        System.out.println();
    }
}