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

// This module is based on the EchoFilter class by Douglas Lyon, docjava.com.

package biz.source_code.dsp.filter;

/**
* A very simple echo filter that adds an echo to a signal.
*
* <p>
* This filter produces multiple echoes by adding attenuated delayed output samples to the input signal.
*/
public class EchoFilter implements SignalFilter {

private int        echoDelay;
private double     attenuation;
private double[]   delayBuf;
private int        delayBufPos;

/**
* Creates a new echo filter.
*
* @param echoDelay
*    Echo delay time in samples.
* @param attenuation
*    Echo attenuation factor. Must be below 1 to prevent feedback overflow.
**/
public EchoFilter (int echoDelay, double attenuation) {
   if (echoDelay < 1 || attenuation >= 1) {
      throw new IllegalArgumentException(); }
   this.echoDelay = echoDelay;
   this.attenuation = attenuation;
   delayBuf = new double[echoDelay]; }

@Override public double step (double inputValue) {
   double outputValue = inputValue + attenuation * delayBuf[delayBufPos];
   delayBuf[delayBufPos] = outputValue;
   delayBufPos = (delayBufPos + 1) % echoDelay;
   return outputValue; }

}
