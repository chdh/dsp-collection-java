// Copyright 2015 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
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
import biz.source_code.dsp.transform.Dft;
import biz.source_code.dsp.util.ArrayUtils;
import java.util.Random;

// Test program for the Dft class.
public class TestDft {

private static final double      eps = 1E-9;

private static Random            random = new Random(547123);

public static void main (String[] args) {
   testDft();
   testDftSynth();
   testDftSynthRandom();
   System.out.println("TestDft completed."); }

// Test directDft() and goertzel() with known result.
private static void testDft() {
   double[] a1 = new double[]{1, 3, 4, 3, 1, 2};
   Complex[] b1 = new Complex[] {
      // Result from http://calculator.vhex.net/calculator/fast-fourier-transform-calculator-fft/1d-discrete-fourier-transform
      new Complex( 14,  0        ),
      new Complex( -2, -3.464102 ),
      new Complex( -1,  1.732051 ),
      new Complex( -2,  0        ),
      new Complex( -1, -1.732051 ),
      new Complex( -2,  3.464102 )};
   checkDftResult(a1, b1, 1E-6);
   //
   double[] a2 = new double[]{-1, 3, 2, 8, 3};
   Complex[] b2 = new Complex[] {
      // Result from http://calculator.vhex.net/calculator/fast-fourier-transform-calculator-fft/1d-discrete-fourier-transform
      new Complex( 15       ,  0        ),
      new Complex( -7.236068,  3.526712 ),
      new Complex( -2.763932, -5.706339 ),
      new Complex( -2.763932,  5.706339 ),
      new Complex( -7.236068, -3.526712 )};
   checkDftResult(a2, b2, 1E-6); }

private static void checkDftResult (double[] a, Complex[] b, double eps) {
   Complex[] c1 = Dft.directDft(a);
   Complex[] c2 = Dft.goertzel(a);
   verifyEqual(c1, b, eps);
   verifyEqual(c2, b, eps); }

// Test directDftSpectrum() and goertzelSpectrum() with synthesizeFromSpectrum().
private static void testDftSynth() {
   double[] a1 = new double[]{1, 3, 4, 3, 1, 2};
   checkDftSynth(a1);
   //
   double[] a2 = new double[]{-1, 3, 2, 8, 3};
   checkDftSynth(a2); }

// Use random numbers to test directDftSpectrum() and goertzelSpectrum() with synthesizeFromSpectrum().
private static void testDftSynthRandom() {
   for (int i = 0; i < 1000000; i++) {
      if (i % 10000 == 0) {
         System.out.print("."); }
      int n = 1 + random.nextInt(20);
      double[] a = genRandomArray(n, 1E4);
      checkDftSynth(a); }
   System.out.println(); }

private static void checkDftSynth (double[] a) {
   Complex[] b1 = Dft.directDftSpectrum(a);
   Complex[] b2 = Dft.goertzelSpectrum(a);
   verifyEqual(b1, b2);
   boolean odd = a.length % 2 != 0;
   double[] c = Dft.synthesizeFromSpectrum(b1, odd);
   verifyEqual(a, c); }

static double[] genRandomArray (int n, double maxValue) {
   double[] a = new double[n];
   for (int i = 0; i < n; i++) {
      a[i] = (random.nextDouble() - 0.5) * 2 * maxValue; }
   return a; }

private static void verifyEqual (double[] a1, double[] a2, double eps) {
   if (a1.length != a2.length) {
      throw new RuntimeException("Array sizes are not equal."); }
   for (int i = 0; i < a1.length; i++) {
      if (Math.abs(a1[i] - a2[i]) > eps) {
         throw new RuntimeException("Difference detected in arrays at position " + i + ": " + a1[i] + " " + a2[i] + " diff=" + Math.abs(a1[i] - a2[i]) + " eps=" + eps + "."); }}}
private static void verifyEqual (double[] a1, double[] a2) {
   verifyEqual(a1, a2, eps); }

private static void verifyEqual (Complex[] a1, Complex[] a2, double eps) {
   if (a1.length != a2.length) {
      throw new RuntimeException("Array sizes are not equal."); }
   for (int i = 0; i < a1.length; i++) {
      if (!a1[i].equals(a2[i], eps)) {
         throw new RuntimeException("Difference detected in arrays at position " + i + ": " + a1[i] + " " + a2[i] + "."); }}}
private static void verifyEqual (Complex[] a1, Complex[] a2) {
   verifyEqual(a1, a2, eps); }

private static void dump (double[] a) {
   System.out.println(ArrayUtils.toString(a)); }

private static void dump (Complex[] a) {
   System.out.println(ArrayUtils.toString(a)); }

}
