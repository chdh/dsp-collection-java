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

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.FilterCharacteristicsType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;
import biz.source_code.dsp.filter.IirFilterDesignFisher;
import java.util.Random;

// Test program for the two filter design modules, Fisher and Exstrom.
// This program uses random numbers to verify that the two filter design modules
// produce the same results. Note that only Butterworth filters can be tested,
// because the Exstrom module only supports Butterworth.
public class TestIirFilterDesignRandom {

private static final double  epsNormal   = 1e-14;
private static final double  epsBandpass = 1e-3;
   // The deviation between Fisher and Exstrom is relatively high for bandpass filters.
   // This is probably because gain normalization of the bandpass is done differently.
private static final double  epsBandstop = 1e-5;

private static Random        random;

public static void main (String[] args) {
   random = new Random(899424123);
   for (int i = 0; i < 1000000; i++) {
      testOneFilterDesign();
      if (i % 1000 == 0) {
         System.out.print("."); }}
   System.out.println(); }

private static void testOneFilterDesign() {
   FilterPassType filterPassType = FilterPassType.class.getEnumConstants()[random.nextInt(FilterPassType.class.getEnumConstants().length)];
   int filterOrder = 2 + random.nextInt(7);
   double fcf1 = 0.05 + random.nextDouble() * 0.35;
   double fcf2 = fcf1 + 0.025 + random.nextDouble() * (0.4 - fcf1);
   String config = filterPassType + " " + filterOrder + " " + fcf1 + " " + fcf2;
   IirFilterCoefficients coeffs1 = IirFilterDesignFisher.design(filterPassType, FilterCharacteristicsType.butterworth, filterOrder, 0, fcf1, fcf2);
   IirFilterCoefficients coeffs2 = IirFilterDesignExstrom.design(filterPassType, filterOrder, fcf1, fcf2);
   double eps;
   switch (filterPassType) {
      case bandpass:   eps = epsBandpass;  break;
      case bandstop:   eps = epsBandstop;  break;
      default:         eps = epsNormal; }
   if (!compareCoefficients(coeffs1.a, coeffs2.a, eps) || !compareCoefficients(coeffs1.b, coeffs2.b, eps)) {
      throw new RuntimeException("Different coefficients detected for " + config); }}

private static boolean compareCoefficients (double[] c1, double[] c2, double eps) {
   if (c1.length != c2.length) {
      return false; }
   for (int i = 0; i < c1.length; i++) {
      if (!isAboutEqual(c1[i], c2[i], eps)) {
         System.out.println("\nc1[" + i + "] = " + c1[i] + " c2[" + i + "] = " + c2[i]);
         return false; }}
   return true; }

private static boolean isAboutEqual (double v1, double v2, double eps) {
   double diff = Math.abs(v1 - v2);
   if (diff <= eps) {
      return true; }
   double mag = Math.max(Math.abs(v1), Math.abs(v2));
   return diff < mag * eps; }

}
