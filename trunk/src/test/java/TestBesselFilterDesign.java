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

import biz.source_code.dsp.filter.BesselFilterDesign;
import biz.source_code.dsp.math.Complex;
import biz.source_code.dsp.util.ArrayUtils;

// Test program for the BesselFilterDesign class.
public class TestBesselFilterDesign {

public static void main (String[] args) {
   listPolynomialCoefficients();
   listFrequencyScalingFactors();
   listFilterPoles(); }

private static void listPolynomialCoefficients() {
   System.out.println("\nBessel polynomials:");
   for (int n = 1; n <= 10; n++) {
      double[] a = BesselFilterDesign.computePolynomialCoefficients(n);
      System.out.print(n + ":");
      for (int i = 0; i < a.length; i++) {
         System.out.print(" " + a[i]); }
      System.out.println(); }}

// Link to verify the scaling factors: http://www.crbond.com/papers/bsf2.pdf (table "Bessel Scale Factors")
private static void listFrequencyScalingFactors() {
   System.out.println("\nFrequency scaling factors:");
   for (int n = 1; n <= 10; n++) {
      double[] besselPolyCoeffs = BesselFilterDesign.computePolynomialCoefficients(n);
      double[] polyCoeffs = ArrayUtils.reverse(besselPolyCoeffs);
      double scalingFactor = BesselFilterDesign.findFrequencyScalingFactor(polyCoeffs);
      System.out.println(n + ": " + scalingFactor); }}

// Link to verify the pole values: http://www.crbond.com/papers/bsf2.pdf (table "Frequency Normalized Bessel Pole Locations")
private static void listFilterPoles() {
   System.out.println("\nBessel filter poles:");
   for (int n = 1; n <= 10; n++) {
      Complex[] poles = BesselFilterDesign.computePoles(n);
      System.out.println(n + ": " + ArrayUtils.toString(poles)); }}

}
