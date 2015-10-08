// Copyright 2015 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
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

import biz.source_code.dsp.sound.AudioStreamPump;
import biz.source_code.dsp.sound.EchoFilterAudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

// Test program for the EchoFilter class.
// This program routes a real-time audio stream from the default input audio device
// through the echo filter to the default output audio device.
public class TestEchoFilter {

private static final int    samplingRate        = 44100;
private static final int    channels            = 2;
private static final int    inputBufferTimeMs   = 100;
private static final int    pumpBufferTimeMs    = 20;
private static final int    outputBufferTimeMs  = 500;

public static void main (String[] args) throws Exception {
   int    echoDelayMs = 500;
   double attenuation = 0.75;
   if (args.length > 2) {
      throw new Exception("Invalid number of command line arguments."); }
   if (args.length >= 1) {
      echoDelayMs = Integer.parseInt(args[0]); }
   if (args.length >= 2) {
      attenuation = Double.parseDouble(args[1]); }
   testEcho(echoDelayMs, attenuation); }

private static void testEcho (int echoDelayMs, double attenuation) throws Exception {
   AudioFormat audioFormat = new AudioFormat(samplingRate, 16, channels, true, false);
   TargetDataLine inputLine = AudioSystem.getTargetDataLine(audioFormat);
   int inputBufferSize = Math.round(audioFormat.getFrameRate() * inputBufferTimeMs / 1000) * audioFormat.getFrameSize();
   inputLine.open(audioFormat, inputBufferSize);
   AudioInputStream inputStream = new AudioInputStream(inputLine);
   AudioInputStream echoInputStream = EchoFilterAudioInputStream.getAudioInputStream(inputStream, echoDelayMs, attenuation);
   SourceDataLine outputLine = AudioSystem.getSourceDataLine(audioFormat);
   int outputBufferSize = Math.round(audioFormat.getFrameRate() * outputBufferTimeMs / 1000) * audioFormat.getFrameSize();
   outputLine.open(audioFormat, outputBufferSize);
   AudioStreamPump pump = new AudioStreamPump(inputLine, echoInputStream, outputLine, pumpBufferTimeMs);
   pump.start();
   System.out.print("Echo started, press Enter to stop - ");
   System.in.read();
   pump.stop();
   inputLine.close();
   outputLine.close(); }

}
