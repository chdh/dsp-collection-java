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

import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.math.Complex;
import biz.source_code.dsp.math.PolynomialUtils.RationalFraction;
import biz.source_code.dsp.swing.TransferFunctionPlot;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;

// A simple test module to plot the transfer function curve of an IIR filter.
@SuppressWarnings("serial")
public class TestIirFilterTransferPlot extends JFrame {

private static RationalFraction        tf;

private TestIirFilterTransferPlot() {
   setLocationByPlatform(true);
   setSize(new Dimension(1000, 1000));
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   //
   TransferFunctionPlot plot1 = new TransferFunctionPlot(tf, true);
   TransferFunctionPlot plot2 = new TransferFunctionPlot(tf, false);
   JSeparator sep = new JSeparator();
   GroupLayout layout = new GroupLayout(getContentPane());
   getContentPane().setLayout(layout);
   GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
   hGroup.addComponent(plot1);
   hGroup.addComponent(sep);
   hGroup.addComponent(plot2);
   GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
   vGroup.addComponent(plot1);
   vGroup.addComponent(sep, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
   vGroup.addComponent(plot2);
   layout.setHorizontalGroup(hGroup);
   layout.setVerticalGroup(vGroup); }

//------------------------------------------------------------------------------

public static void start (IirFilterCoefficients coeffs) {
   tf = new RationalFraction();
   tf.top = coeffs.b;
   tf.bottom = coeffs.a;
   startGuiThread(); }

private static void startGuiThread() {
   EventQueue.invokeLater( new Runnable() {
      @Override
      public void run() {
         guiThreadMain(); }}); }

private static void guiThreadMain() {
   try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      new TestIirFilterTransferPlot().setVisible(true); }
    catch (Throwable e) {
      System.err.print("Error: ");
      e.printStackTrace(System.err);
      JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(9); }}

}
