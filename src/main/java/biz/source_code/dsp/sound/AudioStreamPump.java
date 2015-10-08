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

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
* A data pump for audio streams.
*
* <p>This class maintains an internal thread to copy audio data from
* an input stream to an output stream.
* It is used for real-time audio i/o.
*/
public class AudioStreamPump {

private TargetDataLine    inputLine;             // may be null!
private AudioInputStream  inputStream;
private SourceDataLine    outputLine;
private int               pumpBufferSize;
private volatile boolean  stopped;
private Exception         pumpException;
private Thread            thread;

/**
* Creates a pump for real-time input with filters.
*
* <p>This constructor is used when a real-time data stream from an input line is piped through filters that provide an <code>AudioInputStream</code>.
* The data is read from the <code>inputStream</code>, but the <code>inputLine</code> is used to start and stop the input stream flow.
*
* @param inputLine
*    A <code>TargetDataLine</code> that will be used to start/stop the input audio stream.
* @param inputStream
*    A <code>AudioInputStream</code> that will be used to read the audio data.
* @param outputLine
*    The output line.
* @param pumpBufferTimeMs
*    Time in milliseconds, used to determine the size of the internal buffer.
*    For a minimal real-time delay, the buffer should be as small as possible, but if the buffer is too small, performance will degrade.
*/
public AudioStreamPump (TargetDataLine inputLine, AudioInputStream inputStream, SourceDataLine outputLine, int pumpBufferTimeMs) {
   this.inputLine = inputLine;
   this.inputStream = inputStream;
   this.outputLine = outputLine;
   AudioFormat audioFormat = inputStream.getFormat();
   if (!audioFormat.matches(outputLine.getFormat())) {
      throw new IllegalArgumentException("Audio formats of input and output streams do not match."); }
   pumpBufferSize = Math.round(audioFormat.getFrameRate() * pumpBufferTimeMs / 1000) * audioFormat.getFrameSize(); }

/**
* Creates a pump for real-time input without filters.
*
* <p>This constructor is used when a real-time input data stream comes directly from the input line.
*
* @param inputLine
*    The input line.
* @param outputLine
*    The output line.
* @param pumpBufferTimeMs
*    Time in milliseconds, used to determine the size of the internal buffer.
*    For a minimal real-time delay, the buffer should be as small as possible, but if the buffer is too small, performance will degrade.
*/
public AudioStreamPump (TargetDataLine inputLine, SourceDataLine outputLine, int pumpBufferTimeMs) {
   this(inputLine, new AudioInputStream(inputLine), outputLine, pumpBufferTimeMs); }

/**
* Creates a pump for non-real-time input.
*
* <p>This constructor is used with non-real-time input, e.g. input from a sound file.
*
* @param inputStream
*    The input stream.
* @param outputLine
*    The output line.
*/
public AudioStreamPump (AudioInputStream inputStream, SourceDataLine outputLine) {
   this(null, inputStream, outputLine, 500); }

/**
* Starts the pump.
*/
public void start() {
   if (thread != null) {
      throw new IllegalStateException(); }
   stopped = false;
   pumpException = null;
   thread = new Thread() {
      @Override public void run() {
         threadMain(); }};
   thread.setDaemon(true);
   thread.setName("AudioStreamPump-thread");
   thread.setPriority(Thread.MAX_PRIORITY);
   thread.start(); }

/**
* Waits until the pump completes, e.g. because the end of the input stream has been reached or because of an error.
*
* @param millis
*    The maximum number of milliseconds to wait.
*/
public void waitForCompletion (long millis) throws InterruptedException {
   if (thread == null) {
      return; }
   thread.join(millis); }

/**
* Stops the pump immediatelly.
*/
public void stop() throws Exception {
   if (thread == null) {
      return; }
   stopped = true;
   if (inputLine != null) {
      inputLine.stop();
      inputLine.flush(); }
   outputLine.stop();
   outputLine.flush();
   thread.join();
   thread = null;
   if (pumpException != null) {
      throw pumpException; }}

private void threadMain() {
   try {
      pump1(); }
    catch (Exception e) {
      pumpException = e; }}

private void pump1() throws IOException {
   try {
      if (inputLine != null) {
         inputLine.flush();
         inputLine.start(); }
      outputLine.flush();
      outputLine.start();
      pump2(); }
    finally {
      if (inputLine != null) {
         inputLine.stop();
         inputLine.flush(); }
      outputLine.stop();
      outputLine.flush(); }}

private void pump2() throws IOException {
   byte[] buf = new byte[pumpBufferSize];
   while (!stopped) {
      int rxLen = inputStream.read(buf, 0, buf.length);
      if (rxLen <= 0 || stopped) {
         break; }
      int txLen = outputLine.write(buf, 0, rxLen);
      if (txLen != rxLen && !stopped) {
         throw new IOException("Audio output line blocked."); }}
   if (!stopped) {
      outputLine.drain(); }}

}
