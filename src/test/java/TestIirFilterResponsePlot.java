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

import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.swing.SignalPlot;
import java.awt.EventQueue;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

// A simple test module to plot the impulse or step response of an IIR filter.
@SuppressWarnings("serial")
public class TestIirFilterResponsePlot extends JFrame {

public enum ResponseType {
   impulse, step};

private static final int     responseSignalOffset = 3;
private static final int     responseSignalLen = responseSignalOffset + 1000;

private static float[]       responseSignal;
private static double        plotYMin;
private static double        plotYMax;

private TestIirFilterResponsePlot() {
   setLocationByPlatform(true);
   setSize(new Dimension(1200, 800));
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   SignalPlot plot = new SignalPlot(responseSignal, responseSignalOffset, 0, 50, plotYMin, plotYMax);
   setContentPane(plot); }

//------------------------------------------------------------------------------

public static void start (IirFilterCoefficients coeffs, ResponseType responseType) {
   generateResponseSignal(coeffs, responseType);
   startGuiThread(); }

private static void generateResponseSignal (IirFilterCoefficients coeffs, ResponseType responseType) {
   int n = Math.max(coeffs.a.length, coeffs.b.length);
   responseSignal = new float[responseSignalLen];
   IirFilter iirFilter = new IirFilter(coeffs);
   double maxAbs = 0;
   for (int p = 0; p < responseSignalLen; p++) {
      double x;
      switch (responseType) {
         case impulse: x = (p == responseSignalOffset) ? 1 : 0; break;
         case step:    x = (p <  responseSignalOffset) ? 0 : 1; break;
         default:      throw new AssertionError(); }
      double y = iirFilter.step(x);
      responseSignal[p] = (float)y;
      maxAbs = Math.max(maxAbs, Math.abs(y)); }
   plotYMax = Math.max(maxAbs, 1) + 0.1;
   plotYMin = -plotYMax; }

private static void startGuiThread() {
   EventQueue.invokeLater( new Runnable() {
      @Override
      public void run() {
         guiThreadMain(); }}); }

private static void guiThreadMain() {
   try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      new TestIirFilterResponsePlot().setVisible(true); }
    catch (Throwable e) {
      System.err.print("Error: ");
      e.printStackTrace(System.err);
      JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(9); }}

}
