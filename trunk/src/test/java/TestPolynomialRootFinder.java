// Copyright 2013 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

import biz.source_code.dsp.math.Complex;
import biz.source_code.dsp.math.PolynomialUtils;
import biz.source_code.dsp.math.PolynomialRootFinderLaguerre;
import biz.source_code.dsp.math.PolynomialRootFinderJenkinsTraub;
import biz.source_code.dsp.util.ArrayUtils;
import java.util.Random;

// Test program for the PolynomialRootFinder* classes.
public class TestPolynomialRootFinder {

private static final boolean useLaguerre = false;

private static final double      eps       = useLaguerre ? 1E-7 : 1E-10;
private static final double      randomEps = useLaguerre ? 1E-7 : 1E-5;

private static Random            random    = new Random(8742346);

public static void main (String[] args) {
   testRealZeros();
   testComplexZeros();
   testRandom();
   System.out.println("ok"); }

// Test with polynomials that have only real zeros.
private static void testRealZeros() {
   // Examples from http://www.purplemath.com/modules/solvpoly.htm
   // 2x^5 + 3x^4 - 30x^3 - 57x^2 - 2x + 24
   // Zeros: -1, -3.561552812, 0.5615528128, -1.5, 4
   verifyRealZeros(new double[]{2, 3, -30, -57, -2, 24}, new double[]{-1, (-3+Math.sqrt(17))/2, (-3-Math.sqrt(17))/2, -1.5, 4});
   // 30x^5 - 166x^4 - 542x^3 + 2838x^2 + 1520x - 800
   // Zeros: 5, 5, -4, -4/5, 1/3
   verifyRealZeros(new double[]{30, -166, -542, 2838, 1520, -800}, new double[]{5, 5, -4, -4.0/5, 1.0/3}); }

private static void verifyRealZeros (double[] coeffs, double[] expectedZeros) {
   Complex[] complexZeros = findRoots(coeffs);
   double[] realZeros = ArrayUtils.toDouble(complexZeros, eps);
   // dump(realZeros);
   double[] orderedRealZeros = ArrayUtils.sortByMagnitude(realZeros);
   double[] orderedExpectedZeros = ArrayUtils.sortByMagnitude(expectedZeros);
   verifyEqual(orderedRealZeros, orderedExpectedZeros); }

// Test with polynommials that have complex zeros.
private static void testComplexZeros() {
   // We use reverse Bessel polynomials to test.
   // Cofficients can be found at: http://en.wikipedia.org/wiki/Bessel_polynomials
   // Zeros can be found at: http://www.crbond.com/papers/bsf2.pdf
   // 2nd order reverse Bessel polynomial:
   verifyComplexZeros(
      new double[]{1, 3, 3},
      new Complex[]{new Complex(-1.5, +0.8660254038),
                    new Complex(-1.5, -0.8660254038)});
   // 5th order reverse Bessel polynomial:
   verifyComplexZeros(
      new double[]{1, 15, 105, 420, 945, 945},
      new Complex[]{new Complex(-2.3246743032,  3.5710229203),
                    new Complex(-2.3246743032, -3.5710229203),
                    new Complex(-3.3519563992,  1.7426614162),
                    new Complex(-3.3519563992, -1.7426614162),
                    new Complex(-3.6467385953,  0)}); }

private static void verifyComplexZeros (double[] coeffs, Complex[] expectedZeros) {
   Complex[] zeros = findRoots(coeffs);
   // dump(zeros);
   Complex[] orderedZeros = ArrayUtils.sortByImRe(zeros);
   Complex[] orderedExpectedZeros = ArrayUtils.sortByImRe(expectedZeros);
   // dump(orderedZeros);
   verifyEqual(orderedZeros, orderedExpectedZeros); }

// Test root finder with randomly generated polynomials.
private static void testRandom() {
   final int maxOrder = 15;
   for (int cnt = 0; cnt < 1000000; cnt++) {
      if (cnt % 100 == 0) {
         System.out.print("."); }
      int order = 1 + random.nextInt(maxOrder);
      double[] coeffs = genRandomCoefficients(order);
      verifyRandomZeros(coeffs);
      cnt++; }
   System.out.println(); }

private static void verifyRandomZeros (double[] coeffs) {
   // dump(coeffs);
   Complex[] zeros;
   try {
      zeros = findRoots(coeffs); }
    catch (RuntimeException e) {
      System.out.println();
      dump(coeffs);
      throw e; }
   // dump(zeros);
   Complex[] coeffs2Complex = PolynomialUtils.expand(zeros);
   double[] coeffs2Real = ArrayUtils.toDouble(coeffs2Complex, randomEps);
   double[] coeffsScaled = scaleCoefficients(coeffs);
   try {
      verifyEqual(coeffsScaled, coeffs2Real, randomEps); }
    catch (RuntimeException e) {
      System.out.println();
      dump(coeffs);
      throw e; }}

// Returns scaled polynomial coefficients so that the first coefficient is 1.
private static double[] scaleCoefficients (double[] a) {
   return ArrayUtils.divide(a, a[0]); }

private static double[] genRandomCoefficients (int order) {
   final double maxCoefficient = 1E6;
   double[] coeffs = new double[order + 1];
   for (int i = 0; i <= order; i++) {
      double c = (random.nextDouble() - 0.5) * 2 * maxCoefficient;
      int j = random.nextInt(100);
      if (j < 20 && c > 1 && c < 1E3) {                    // make more coefficients below 1
         c = 1 / c; }
//     else if (j == 99) {
//       c = 0; }                                          // set each 100th coefficient to 0
      coeffs[i] = c; }
   if (coeffs[0] == 0) {                                   // the first coefficient must not be 0
      coeffs[0] = 1; }
   return coeffs; }

private static Complex[] findRoots (double[] coeffs) {
   if (useLaguerre) {
      return PolynomialRootFinderLaguerre.findRoots(coeffs); }
    else {
      return PolynomialRootFinderJenkinsTraub.findRoots(coeffs); }}

private static void verifyEqual (double[] a1, double[] a2, double eps) {
   if (a1.length != a2.length) {
      throw new RuntimeException("Array sizes are not equal."); }
   for (int i = 0; i < a1.length; i++) {
      if (!isAboutEqual(a1[i], a2[i], eps)) {
         throw new RuntimeException("Difference detected in arrays at position " + i + ": " + a1[i] + " " + a2[i] + " diff=" + Math.abs(a1[i] - a2[i]) + " eps=" + eps + "."); }}}
private static void verifyEqual (double[] a1, double[] a2) {
   verifyEqual(a1, a2, eps); }

private static void verifyEqual (Complex[] a1, Complex[] a2) {
   if (a1.length != a2.length) {
      throw new RuntimeException("Array sizes are not equal."); }
   for (int i = 0; i < a1.length; i++) {
      if (!isAboutEqual(a1[i].re(), a2[i].re(), eps) || !isAboutEqual(a1[i].im(), a2[i].im(), eps)) {
         throw new RuntimeException("Difference detected in arrays at position " + i + ": " + a1[i] + " " + a2[i] + "."); }}}

private static boolean isAboutEqual (double v1, double v2, double eps) {
   double diff = Math.abs(v1 - v2);
   if (diff <= eps) {
      return true; }
   double mag = Math.max(Math.abs(v1), Math.abs(v2));
   return diff < mag * eps; }

private static void dump (double[] a) {
   System.out.println(ArrayUtils.toString(a)); }

private static void dump (Complex[] a) {
   System.out.println(ArrayUtils.toString(a)); }

}
