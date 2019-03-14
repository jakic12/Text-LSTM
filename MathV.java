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

    public static double[][] dot(double[][] a, double[][] b){
        double[][] out;
        if(a.length == b[0].length){
            throw new RuntimeException("vector and matrix dimension mismatch");
        }else{
            out = new double[a.length][b[0].length];

            for(int v = 0; v < a.length; v++){

                for(int i = 0; i < b[0].length; i++){
                    for(int j = 0; j < a[0].length; j++){
                        out[v][i] += a[v][j] * b[j][i];
                    }
                }

            }
        }

        return out;
    }

    public static double[] dot(double[] a, double[][] b) {
        double[] out;
        if (a.length == b[0].length) {
            throw new RuntimeException("vector and matrix dimension mismatch");
        } else {
            out = new double[b[0].length];
            for (int i = 0; i < b[0].length; i++) {
                for (int j = 0; j < a.length; j++) {
                    out[i] += a[j] * b[j][i];
                }
            }
        }

        return out;
    }
}