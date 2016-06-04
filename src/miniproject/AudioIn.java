package miniproject;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class AudioIn {
	public void getTheInput() throws LineUnavailableException{
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		TargetDataLine line = AudioSystem.getTargetDataLine(format);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object
		if (!AudioSystem.isLineSupported(info)) {
		    // Handle the error ... 

		}
		// Obtain and open the line.
		try {
		    line = (TargetDataLine) AudioSystem.getLine(info);
		    line.open(format);
		} catch (LineUnavailableException ex) {
		    // Handle the error ... 
		}
		// Assume that the TargetDataLine, line, has already
		// been obtained and opened.
		ByteArrayOutputStream out  = new ByteArrayOutputStream();
		int numBytesRead;
		byte[] data = new byte[line.getBufferSize() / 5];

		// Begin audio capture.
		line.start();
		while (true) {
			   // Read the next chunk of data from the TargetDataLine.
			   numBytesRead =  line.read(data, 0, data.length);
			   
			   // Save this chunk of data.
			   out.write(data, 0, numBytesRead);
			}   
	}
}
