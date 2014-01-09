// Copyright 2014 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
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
import biz.source_code.dsp.signal.RmsNormalizer;

// Test program for the RmsNormalizer class.
// It reads a signal from a WAV file, normalizes the amplitude level and writes the result into another WAV file.
public class TestRmsNormalizer {

public static void main (String[] args) throws Exception {
   if (args.length != 2) {
      throw new Exception("Invalid number of command line arguments."); }
   String inputFileName = args[0];
   String outputFileName = args[1];
   AudioIo.AudioSignal signal = AudioIo.loadWavFile(inputFileName);
   float targetRms = 0.2F;
   int segmentSize = signal.samplingRate / 10;             // 100 ms
   RmsNormalizer.normalize(signal.data, targetRms, segmentSize);
   AudioIo.saveWavFile(outputFileName, signal); }

}
