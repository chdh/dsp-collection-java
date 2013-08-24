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

import biz.source_code.dsp.swing.FunctionPlot;
import biz.source_code.dsp.swing.FunctionPlot.SimplePlotFunction;
import java.awt.EventQueue;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

// Test program for the FunctionPlot class.
@SuppressWarnings("serial")
public class TestFunctionPlot extends JFrame {

private TestFunctionPlot() {
   setLocationByPlatform(true);
   setSize(new Dimension(800, 600));
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   FunctionPlot functionPlot = new FunctionPlot(new TestPlotFunction(), -30, 30, -0.3, 1.1);
   setContentPane(functionPlot); }

private static class TestPlotFunction extends SimplePlotFunction {
   @Override
   public double getY (double x) {
      return Math.sin(x) / x; }}

//------------------------------------------------------------------------------

public static void main (String[] args) {
   EventQueue.invokeLater( new Runnable() {
      @Override
      public void run() {
         guiThreadMain(); }}); }

private static void guiThreadMain() {
   try {
      guiThreadInit(); }
    catch (Throwable e) {
      System.err.print("Error: ");
      e.printStackTrace(System.err);
      JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(9); }}

private static void guiThreadInit() throws Exception {
   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
   new TestFunctionPlot().setVisible(true); }

}
