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
import biz.source_code.dsp.util.ArrayUtils;

// Test program for the PolynomialUtils class.
public class TestPolynomialUtils {

private static double eps        = 1E-8;

public static void main (String[] args) {
   testRealMultiply();
   testExpand();
   testDeflate();
   System.out.println("ok"); }

private static void testRealMultiply() {
   // Examples from http://www.purplemath.com/modules/polymult3.htm
   // (4x^2 - 4x - 7) * (x + 3) = 4x3 + 8x^2 - 19x - 21
   verifyMultiply(new double[]{4, -4, -7}, new double[]{1, 3}, new double[]{4, 8, -19, -21});
   // 3x^2 - 9x + 5) * (2x^2 + 4x - 7) = 6x^4 - 6x^3 - 47x^2 + 83x - 35
   verifyMultiply(new double[]{3, -9, 5}, new double[]{2, 4, -7}, new double[]{6, -6, -47, 83, -35});
   // (x^3 + 2x^2 + 4) * (2x^3 + x + 1) =  2x^6 + 4x^5 + x^4 + 11x^3 + 2x^2 + 4x + 4
   verifyMultiply(new double[]{1, 2, 0, 4}, new double[]{2, 0, 1, 1}, new double[]{2, 4, 1, 11, 2, 4, 4}); }

private static void verifyMultiply (double[] a1, double[] a2, double[] expectedResult) {
   double[] a3 = PolynomialUtils.multiply(a1, a2);
   verifyEqual(a3, expectedResult); }

private static void testExpand() {
   // To verify our implementation we use the roots of the reverse Bessel polynomials from http://www.crbond.com/papers/bsf2.pdf
   // The Bessel polynomial coefficients can be found at http://en.wikipedia.org/wiki/Bessel_polynomials
   // 2nd order reverse Bessel polynomial:
   verifyExpand(
      new Complex[]{new Complex(-1.5, +0.8660254038),
                    new Complex(-1.5, -0.8660254038)},
      new double[]{1, 3, 3});
   // 5th order reverse Bessel polynomial:
   verifyExpand(
      new Complex[]{new Complex(-2.3246743032,  3.5710229203),
                    new Complex(-2.3246743032, -3.5710229203),
                    new Complex(-3.3519563992,  1.7426614162),
                    new Complex(-3.3519563992, -1.7426614162),
                    new Complex(-3.6467385953,  0)},
      new double[]{1, 15, 105, 420, 945, 945}); }

private static void verifyExpand (Complex[] zeros, double[] expectedCoefficients) {
   Complex[] a = PolynomialUtils.expand(zeros);
   double[] a2 = ArrayUtils.toDouble(a, eps);
   verifyEqual(a2, expectedCoefficients); }

private static void testDeflate() {
   // Example from http://www.purplemath.com/modules/polymult3.htm
   verifyDeflate(new double[]{1, -9, -10}, -1, new double[]{1, -10});
   // Example from http://en.wikipedia.org/wiki/Polynomial_long_division (the remainder 5 is first subtracted).
   verifyDeflate(new double[]{1, -2, 0, -4 - 5}, 3, new double[]{1, 1, 3}); }

private static void verifyDeflate (double[] a, double z, double[] expectedQuotient) {
   Complex[] complexA = ArrayUtils.toComplex(a);
   Complex[] complexQuotient = PolynomialUtils.deflate(complexA, new Complex(z), eps);
   double[] quotient = ArrayUtils.toDouble(complexQuotient, eps);
   verifyEqual(quotient, expectedQuotient); }

private static void verifyEqual (double[] a1, double[] a2) {
   if (a1.length != a2.length) {
      throw new RuntimeException("Array sizes are not equal."); }
   for (int i = 0; i < a1.length; i++) {
      if (Math.abs(a1[i] - a2[i]) > eps) {
         throw new RuntimeException("Difference detected in arrays at position " + i + ": " + a1[i] + " " + a2[i] + "."); }}}

private static void dump (double[] a) {
   System.out.println(ArrayUtils.toString(a)); }

private static void dump (Complex[] a) {
   System.out.println(ArrayUtils.toString(a)); }

}
