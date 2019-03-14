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

    public static double sigm(double x){
        return 1 / (1 + Math.exp(-x));
    }

    public static double[] sigmArray(double[] x){
        for(int i = 0; i < x.length; i++){
            x[i] = sigm(x[i]);
        }
        return x;
    }

    public static double disgm(double x){
        return sigm(x) * ( 1 - sigm(x));
    }

    public static double[] randomArray(int size){
        return randomArray(size, 0d, 1d);
    }

    public static double[] randomArray(int size, double min, double max) {
        double[] out = new double[size];

        for(int i = 0; i < size; i++){
            out[i] = Math.random()*(max-min) + min;
        }

        return out;
    }

    public static double[][] randomArray(int sizeX, int sizeY) {
        return randomArray(sizeX, sizeY, 0, 1);
    }

    public static double[][] randomArray(int sizeX, int sizeY, double min, double max) {
        double[][] out = new double[sizeY][sizeX];

        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[i].length; j++){
                out[i][j] = Math.random()*(max-min) + min;
            }
        }

        return out;
    }

}