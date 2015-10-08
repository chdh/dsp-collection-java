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

import biz.source_code.dsp.filter.SignalFilter;
import java.io.InputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
* Wrapper for using a signal filter with the Java Sound API.
*
* <p>
* This class provides an {@link javax.sound.sampled.AudioInputStream} for
* filtering a sound stream.
*/
public class SignalFilterAudioInputStream {

// Dummy constructor to suppress Javadoc.
private SignalFilterAudioInputStream() {}

/**
* Returns an AudioInputStream that supplies the filtered audio signal.
*
* @param in
*    The input AudioInputStream.
* @param signalFilters
*    An array of signal filters, one for each channel.
*/
public static AudioInputStream getAudioInputStream (AudioInputStream in, SignalFilter[] signalFilters) {
   FilterStream filterStream = new FilterStream(in, signalFilters);
   return new AudioInputStream(filterStream, in.getFormat(), in.getFrameLength()); }

//------------------------------------------------------------------------------

private static class FilterStream extends InputStream {

private static final int     inBufFrames = 4096;

private AudioInputStream     in;
private SignalFilter[]       signalFilters;
private AudioFormat          format;
private int                  channels;
private int                  frameSize;
private byte[]               inBuf;
private float[][]            floatBufs;

public FilterStream (AudioInputStream in, SignalFilter[] signalFilters) {
   this.in = in;
   this.signalFilters = signalFilters;
   format = in.getFormat();
   channels = format.getChannels();
   if (channels != signalFilters.length) {
      throw new IllegalArgumentException(); }
   frameSize = format.getFrameSize();
   inBuf = new byte[inBufFrames * frameSize];
   floatBufs = new float[channels][];
   for (int channel = 0; channel < channels; channel++) {
      floatBufs[channel] = new float[inBufFrames]; }}

@Override public int read (byte[] outBuf, int outOffs, int len1) throws IOException {
   int len2 = Math.min(len1, inBuf.length);
   int len3 = (len2 / frameSize) * frameSize;
   int len = in.read(inBuf, 0, len3);
   if (len <= 0) {
      return len; }
   if (len % frameSize != 0) {
      throw new AssertionError(); }
   int frames = len / frameSize;
   AudioIo.unpackAudioStreamBytes(format, inBuf, 0, floatBufs, 0, frames);
   for (int channel = 0; channel < channels; channel++) {
      SignalFilter signalFilter = signalFilters[channel];
      float[] floatBuf = floatBufs[channel];
      for (int i = 0; i < frames; i++) {
         floatBuf[i] = (float)signalFilter.step(floatBuf[i]); }}
   AudioIo.packAudioStreamBytes(format, floatBufs, 0, outBuf, outOffs, frames);
   return len; }

@Override public int read() throws IOException {
   throw new AssertionError(); }

}}
