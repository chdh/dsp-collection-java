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
import biz.source_code.dsp.sound.IirFilterAudioInputStreamExstrom;
import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

// Test program for filtering a WAV file with a Butterworth IIR filter (Exstrom method).
public class WavFilterExstrom {

public static void main (String[] args) throws Exception {
   if (args.length == 0) {
      displayHelp();
      return; }
   if (args.length != 6) {
      throw new Exception("Invalid number of command line arguments."); }
   String inputFileName = args[0];
   FilterPassType filterPassType = FilterPassType.valueOf(args[1]);
   int filterOrder = Integer.valueOf(args[2]);
   double fcf1 = Double.valueOf(args[3]);
   double fcf2 = Double.valueOf(args[4]);
   String outputFileName = args[5];
   filterWavFile(inputFileName, filterPassType, filterOrder, fcf1, fcf2, outputFileName); }

private static void filterWavFile (String inputFileName, FilterPassType filterPassType, int filterOrder, double fcf1, double fcf2, String outputFileName) throws Exception {
   AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(inputFileName));
   AudioInputStream filterStream = IirFilterAudioInputStreamExstrom.getAudioInputStream(inputStream, filterPassType, filterOrder, fcf1, fcf2);
   AudioSystem.write(filterStream, AudioFileFormat.Type.WAVE, new File(outputFileName)); }

private static void displayHelp() {
   System.out.println();
   System.out.println("This program filters a WAV file.");
   System.out.println("Author: Christian d'Heureuse, www.source-code.biz");
   System.out.println();
   System.out.println("Command line arguments:");
   System.out.println(" inputFileName filterPassType filterOrder fcf1 fcf2 outputFileName");
   System.out.println();
   System.out.println("filterPassType:");
   System.out.println(" lowpass, highpass, bandpass or bandstop.");
   System.out.println();
   System.out.println("fcf1:");
   System.out.println(" Filter cutoff frequency in Hz for lowpass/highpass,");
   System.out.println(" lower cutoff frequency in Hz for bandpass/bandstop.");
   System.out.println();
   System.out.println("fcf2:");
   System.out.println(" Upper cutoff frequency in Hz for bandpass/bandstop,");
   System.out.println(" ignored for lowpass/highpass."); }

}
