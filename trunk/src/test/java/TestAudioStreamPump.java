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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

// Test program for the AudioStreamPump class.
// This program copies audio data in real-time from the default audio input device
// to the default audio output device during 5 seconds.
public class TestAudioStreamPump {

private static final int durationMs         = 5000;
private static final int samplingRate       = 44100;
private static final int inputBufferTimeMs  = 100;
private static final int pumpBufferTimeMs   = 20;
private static final int outputBufferTimeMs = 500;

public static void main (String[] args) throws Exception {
   AudioFormat audioFormat = new AudioFormat(samplingRate, 16, 1, true, false);
   TargetDataLine inputLine = AudioSystem.getTargetDataLine(audioFormat);
   int inputBufferSize = Math.round(audioFormat.getFrameRate() * inputBufferTimeMs / 1000) * audioFormat.getFrameSize();
   inputLine.open(audioFormat, inputBufferSize);
   SourceDataLine outputLine = AudioSystem.getSourceDataLine(audioFormat);
   int outputBufferSize = Math.round(audioFormat.getFrameRate() * outputBufferTimeMs / 1000) * audioFormat.getFrameSize();
   outputLine.open(audioFormat, outputBufferSize);
   AudioStreamPump pump = new AudioStreamPump(inputLine, outputLine, pumpBufferTimeMs);
   pump.start();
   System.out.println("Pump started, waiting " + (durationMs / 1000F) + " seconds.");
   pump.waitForCompletion(durationMs);
   pump.stop();
   System.out.println("Pump stopped.");
   inputLine.close();
   outputLine.close(); }

}
