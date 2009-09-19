package org.codehaus.groovy.geelab.jama.util;

import org.codehaus.groovy.geelab.jama.Matrix;

public class MagicSquare {

   public static Matrix magic (int n) {

          double[][] M = new double[n][n];

          // Odd order

          if ((n % 2) == 1) {
             int a = (n+1)/2;
             int b = (n+1);
             for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                   M[i][j] = n*((i+j+a) % n) + ((i+2*j+b) % n) + 1;
                }
             }

          // Doubly Even Order

          } else if ((n % 4) == 0) {
             for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                   if (((i+1)/2)%2 == ((j+1)/2)%2) {
                      M[i][j] = n*n-n*i-j;
                   } else {
                      M[i][j] = n*i+j+1;
                   }
                }
             }

          // Singly Even Order

          } else {
             int p = n/2;
             int k = (n-2)/4;
             Matrix A = magic(p);
             for (int j = 0; j < p; j++) {
                for (int i = 0; i < p; i++) {
                   double aij = A.get(i,j);
                   M[i][j] = aij;
                   M[i][j+p] = aij + 2*p*p;
                   M[i+p][j] = aij + 3*p*p;
                   M[i+p][j+p] = aij + p*p;
                }
             }
             for (int i = 0; i < p; i++) {
                for (int j = 0; j < k; j++) {
                   double t = M[i][j]; M[i][j] = M[i+p][j]; M[i+p][j] = t;
                }
                for (int j = n-k+1; j < n; j++) {
                   double t = M[i][j]; M[i][j] = M[i+p][j]; M[i+p][j] = t;
                }
             }
             double t = M[k][0]; M[k][0] = M[k+p][0]; M[k+p][0] = t;
             t = M[k][k]; M[k][k] = M[k+p][k]; M[k+p][k] = t;
          }
          return new Matrix(M);
       }
}
