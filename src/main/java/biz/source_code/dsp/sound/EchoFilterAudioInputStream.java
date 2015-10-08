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

package biz.source_code.dsp.sound;

import biz.source_code.dsp.filter.EchoFilter;
import java.io.InputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
* An echo filter for the Java Sound API.
*/
public class EchoFilterAudioInputStream {

// Dummy constructor to suppress Javadoc.
private EchoFilterAudioInputStream() {}

/**
* Returns an AudioInputStream that provides the input signal with an added echo.
*
* @param in
*    The input AudioInputStream.
* @param echoDelayMs
*    Echo delay time in milliseconds.
* @param attenuation
*    Echo attenuation factor. Must be below 1 to prevent feedback overflow.
*/
public static AudioInputStream getAudioInputStream (AudioInputStream in, int echoDelayMs, double attenuation) {
   AudioFormat format = in.getFormat();
   int echoDelaySamples = Math.round(format.getSampleRate() * echoDelayMs / 1000);
   int channels = format.getChannels();
   EchoFilter[] echoFilters = new EchoFilter[channels];
   for (int channel = 0; channel < channels; channel++) {
      echoFilters[channel] = new EchoFilter(echoDelaySamples, attenuation); }
   return SignalFilterAudioInputStream.getAudioInputStream(in, echoFilters); }

}
