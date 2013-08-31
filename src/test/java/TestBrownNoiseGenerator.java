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

import biz.source_code.dsp.signal.BrownNoiseGenerator;
import biz.source_code.dsp.sound.AudioIo;

// Test program for the BrownNoiseGenerator class.
// Writes the output of the noise generator into a WAV file.
public class TestBrownNoiseGenerator {

public static void main (String[] args) throws Exception {
   if (args.length != 0) {
      throw new Exception("This test program has no command line parameters."); }
   String outputFileName = "TestBrownNoiseGenerator-output.wav";
   int    samplingRate   = 44100;                          // Hz
   double duration       = 2;                              // seconds
   int samples = (int)Math.round(duration * samplingRate);
   float[] buf = new float[samples];
   generateBrownNoise(buf);
   AudioIo.saveWavFile(outputFileName, buf, samplingRate); }

private static void generateBrownNoise (float[] buf) {
   BrownNoiseGenerator gen = new BrownNoiseGenerator();
   for (int i = 0; i < buf.length; i++) {
      buf[i] = (float)gen.getNext(); }}

} // end class TestBrownNoiseGenerator
