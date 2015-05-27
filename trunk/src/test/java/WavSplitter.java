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

import biz.source_code.dsp.signal.ActivityDetector;
import biz.source_code.dsp.signal.EnvelopeDetector;
import biz.source_code.dsp.sound.AudioIo;
import biz.source_code.dsp.sound.AudioIo.AudioSignal;

// Test program for the ActivityDetector class.
// It splits a WAV file into multiple WAV files, according to the silence gaps within the record.
// For a multi-channel WAV file, only the first channel is used for the activity detector.
public class WavSplitter {

private static final float thresholdLevel     = 0.002F;    // sound envelope treshold
private static final float minActivityTime    = 0.25F;     // minimum activity segment time in seconds
private static final float minSilenceTime     = 0.20F;     // minimum silence segment time in seconds
private static final float leadTime           = 0.05F;     // lead time for output zones in seconds
private static final float trailTime          = 0F;        // trail time for output zones in seconds

public static void main (String[] args) throws Exception {
   if (args.length == 0) {
      displayHelp();
      return; }
   if (args.length != 2) {
      throw new Exception("Invalid number of command line arguments."); }
   String inputFileName = args[0];
   String outputFileNameMask = args[1];
   splitWavFile(inputFileName, outputFileNameMask); }

private static void splitWavFile (String inputFileName, String outputFileNameMask) throws Exception {
   AudioSignal signal = AudioIo.loadWavFile(inputFileName);
   int[] zones = findSoundZones(signal);
   if (zones.length == 0) {
      System.out.println("No sound zones found in WAV file.");
      return; }
   for (int zoneNo = 1; zoneNo <= zones.length/2; zoneNo++) {
      writeZoneWavFile(signal, zoneNo, zones[2*(zoneNo-1)], zones[2*(zoneNo-1)+1], outputFileNameMask); }}

private static int[] findSoundZones (AudioSignal signal) {
   EnvelopeDetector envelopeDetector = new EnvelopeDetector(signal.samplingRate);
   float[] envelope = envelopeDetector.process(signal.data[0]);
   int minActivityLen = Math.round(minActivityTime * signal.samplingRate);
   int minSilenceLen  = Math.round(minSilenceTime  * signal.samplingRate);
   ActivityDetector activityDetector = new ActivityDetector(thresholdLevel, minActivityLen, minSilenceLen);
   return activityDetector.process(envelope); }

private static void writeZoneWavFile (AudioSignal signal, int zoneNo, int activityStartPos, int activityEndPos, String outputFileNameMask) throws Exception {
   String outputFileName = genOutputFileName(outputFileNameMask, zoneNo);
   int startPos = Math.max(0, activityStartPos - Math.round(leadTime * signal.samplingRate));
   int endPos = Math.min(signal.data[0].length, activityEndPos + Math.round(trailTime * signal.samplingRate));
   AudioIo.saveWavFile(outputFileName, signal, startPos, endPos - startPos); }

private static String genOutputFileName (String outputFileNameMask, int zoneNo) {
   int p0 = outputFileNameMask.indexOf('#');
   if (p0 == -1) {
      throw new IllegalArgumentException("No \"#\" sign in output file name mask."); }
   int p = p0 + 1;
   while (p < outputFileNameMask.length() && outputFileNameMask.charAt(p) == '#') {
      p++; }
   int minDigits = p - p0;
   return outputFileNameMask.substring(0, p0) + formatInt0(zoneNo, minDigits) + outputFileNameMask.substring(p); }

// Formats an integer value with leading zeros.
private static String formatInt0 (int value, int len) {
   char[] buf = new char[Math.max(11, len)];
   int p = buf.length;
   int p0 = buf.length - len + (value<0?1:0);
   int i = Math.abs(value);
   while (i > 0 || p > p0) {
      buf[--p] = Character.forDigit(i % 10, 10);
      i /= 10; }
   if (value < 0) buf[--p] = '-';
   return new String(buf, p, buf.length-p); }

private static void displayHelp() {
   System.out.println();
   System.out.println("This program splits a WAV file into multiple WAV files.");
   System.out.println("Author: Christian d'Heureuse, www.source-code.biz");
   System.out.println();
   System.out.println("Command line arguments:");
   System.out.println(" inputFileName outputFileNameMask");
   System.out.println();
   System.out.println("outputFileNameMask:");
   System.out.println(" Must contain \"#\" signs as a placeholder for the file number.");
   System.out.println(" The number of \"#\" signs controls the number of digits to be used.");
   System.out.println(" Example: c:\\temp\\part##.wav."); }

}
