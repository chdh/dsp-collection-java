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

import biz.source_code.dsp.sound.AudioIo;
import biz.source_code.dsp.sound.AudioIo.AudioSignal;
import biz.source_code.dsp.swing.SignalPlot;
import java.awt.EventQueue;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

// Test program for the SignalPlot class.
// Reads a WAV file and displays the signal.
@SuppressWarnings("serial")
public class TestSignalPlot extends JFrame {

private static AudioSignal   audioSignal;

private TestSignalPlot() {
   setLocationByPlatform(true);
   setSize(new Dimension(1200, 300));
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   SignalPlot signalPlot = new SignalPlot(audioSignal.data[0], -1, 1);
   signalPlot.setZoomModeHorizontal(true);
   setContentPane(signalPlot); }

//------------------------------------------------------------------------------

public static void main (String[] args) throws Exception {
   if (args.length != 1) {
      throw new Exception("Invalid number of command line arguments."); }
   String fileName = args[0];
   audioSignal = AudioIo.loadWavFile(fileName);
   startGuiThread(); }

private static void startGuiThread() {
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
   new TestSignalPlot().setVisible(true); }

}
