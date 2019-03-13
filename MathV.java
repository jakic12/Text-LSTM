public class MathV{
    public static double[] add(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] += b[i];
        }
        return a;
    }

    public static double[] sub(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] -= b[i];
        }
        return a;
    }

    public static double[] multiply(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] *= b[i];
        }
        return a;
    }

    public static double[] div(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] /= b[i];
        }
        return a;
    }
}