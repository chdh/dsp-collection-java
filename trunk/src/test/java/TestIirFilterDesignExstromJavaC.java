// Copyright 2013 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  GPL, GNU General Public License, V2 or later, http://www.gnu.org/licenses/gpl.html
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.ArrayList;

// This program is used to verify that the Java port of the Exstrom Butterworth IIR
// filter design module produces the same results as the original C version
// from http://www.exstrom.com/journal/sigproc.
// It uses random numbers to test all possible cases.
public class TestIirFilterDesignExstromJavaC {

private static Random        random;
private static String        tempFileName;

public static void main (String[] args) throws Exception {
   random = new Random(532453232);
   tempFileName = File.createTempFile("tempTestButterworthJavaC", ".txt").getPath();
   long ctr = 0;
   while (true) {
      testOnce();
      if (ctr++ % 1000 == 0) {
         System.out.print("."); }}}

private static void testOnce() throws Exception {
   FilterPassType filterPassType = FilterPassType.class.getEnumConstants()[random.nextInt(FilterPassType.class.getEnumConstants().length)];
   int filterOrder = 1 + random.nextInt(10);
   double fcf1 = 0.05 + random.nextDouble() * 0.35;
   double fcf2 = fcf1 + 0.025 + random.nextDouble() * (0.4 - fcf1);
   String config = filterPassType + " " + filterOrder + " " + fcf1 + " " + fcf2;
   // System.out.println(config);
   // Calculate filter coefficients with Java version.
   IirFilterCoefficients coeffs1 = IirFilterDesignExstrom.design(filterPassType, filterOrder, fcf1, fcf2);
   // Calculate filter coefficients with C version.
   IirFilterCoefficients coeffs2 = calculateButterworthWithCVersion(filterPassType, filterOrder, fcf1, fcf2);
   // dump("a1", coeffs.a); dump("a2", coeffs2.a); dump("b1", coeffs1.b); dump("b2", coeffs2.b);
   // Compare the two results.
   if (!compareCoefficients(coeffs1.a, coeffs2.a) || !compareCoefficients(coeffs1.b, coeffs2.b)) {
      throw new Exception("Different coefficients detected for " + config); }}

private static IirFilterCoefficients calculateButterworthWithCVersion (FilterPassType filterPassType, int filterOrder, double fcf1, double fcf2) throws Exception {
   String programName;
   switch (filterPassType) {
      case lowpass:  programName = "bwlp.exe"; break;
      case highpass: programName = "bwhp.exe"; break;
      case bandpass: programName = "bwbp.exe"; break;
      case bandstop: programName = "bwbs.exe"; break;
      default: throw new AssertionError(); }
   ArrayList<String> cmd = new ArrayList<String>(8);
   cmd.add(programName);
   cmd.add(Integer.toString(filterOrder));
   cmd.add(Double.toString(fcf1 * 2));                  // C version uses frequencies relative to PI, we use 2 PI
   if (filterPassType == FilterPassType.bandpass || filterPassType == FilterPassType.bandstop) {
      cmd.add(Double.toString(fcf2 * 2)); }
   cmd.add("1");                                        // to scale the coefficients
   cmd.add(tempFileName);
   Process process = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
   process.waitFor();
   return readCoefficientsFromTextFile(tempFileName); }

private static IirFilterCoefficients readCoefficientsFromTextFile (String fileName) throws Exception {
   BufferedReader in = null;
   try {
      in = new BufferedReader(new FileReader(fileName));
      IirFilterCoefficients coeffs = new IirFilterCoefficients();
      coeffs.b = readCoefficients(in);
      coeffs.a = readCoefficients(in);
      return coeffs; }
    finally {
      if (in != null) {
         in.close(); }}}

private static double[] readCoefficients (BufferedReader in) throws Exception {
   int n;
   while (true) {
      String s = in.readLine();
      if (s == null) {
         throw new Exception("Unexpected EOF while reading coefficients from file."); }
      if (s.length() > 0 && s.charAt(0) != '#') {
         n = Integer.valueOf(s);
         break; }}
   double[] c = new double[n];
   for (int i = 0; i < n; i++) {
      String s = in.readLine();
      c[i] = Double.valueOf(s); }
   return c; }

private static boolean compareCoefficients (double[] c1, double[] c2) {
   if (c1.length != c2.length) {
      return false; }
   for (int i = 0; i < c1.length; i++) {
      double diff = Math.abs(c1[i] - c2[i]);
      if (diff > 1e-9) {
         System.out.println("relDiff: " + diff);
         return false; }}
   return true; }

private static void dump (String name, double[] a) {
   for (int i = 0; i < a.length; i++) {
      System.out.println(name + "[" + i + "] = " + a[i]); }}

}
