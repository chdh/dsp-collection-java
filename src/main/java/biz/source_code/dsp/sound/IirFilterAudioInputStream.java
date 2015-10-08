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

package biz.source_code.dsp.sound;

import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import javax.sound.sampled.AudioInputStream;

/**
* Wrapper for using the IIR filter class with the Java Sound API.
*
* <p>
* This class provides an {@link javax.sound.sampled.AudioInputStream} for
* filtering a sound stream.
*/
public class IirFilterAudioInputStream {

// Dummy constructor to suppress Javadoc.
private IirFilterAudioInputStream() {}

/**
* Returns an AudioInputStream that supplies the filtered audio signal.
*
* @param in
*    The input AudioInputStream.
* @param coeffs
*    The IIR filter coefficients.
*/
public static AudioInputStream getAudioInputStream (AudioInputStream in, IirFilterCoefficients coeffs) {
   int channels = in.getFormat().getChannels();
   IirFilter[] iirFilters = new IirFilter[channels];
   for (int channel = 0; channel < channels; channel++) {
      iirFilters[channel] = new IirFilter(coeffs); }
   return SignalFilterAudioInputStream.getAudioInputStream(in, iirFilters); }

}
