package benchmarks;
public class HeapSort {

    int dataSize;
    double[] pArray;

    public HeapSort() {
        dataSize = 5000000; // 5 million doubles
//        randomizer.resetRandomizer();

        // create test data array

        pArray = new double[dataSize];
//        for (int i=0; i<dataSize; ++i)
//            pArray[i] = randomizer.getRandomValue(1);
    }

    protected void heapsort(int n, double[] ra) {
        int l, ir;
        double rra;

        l = (n >> 1) + 1;
        ir = n;
        while(true) {
            if (l > 1)
                rra = ra[--l];
            else {
                rra = ra[ir];
                ra[ir] = ra[1];
                if (--ir == 1) {
                    ra[1] = rra;
                    return;
                }
            }

            int i = l;
            int j = l << 1;

            while (j <= ir) {

                if (j < ir && ra[j] < ra[j+1])
                    ++j;

                if (rra < ra[j]) {
                    ra[i] = ra[j];
                    j += (i = j);
                }
                else {
                    j = ir + 1;
                }
            }

            ra[i] = rra;
        }
    }
}
