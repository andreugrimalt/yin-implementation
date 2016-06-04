/*

Summary
-------

Example class runs a program that detects the pitch, onset and duration of notes in audio files.


*/

package miniproject;

import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;

public class Example {

	/**
	 * @param args
	 * @throws LineUnavailableException 
	 */
	
	public static void main(String[] args) throws LineUnavailableException {
		
		ArrayList<double[]> data = new ArrayList<double[]>();
		/* YIN reading a wav file */
		// Store an audio file as samples.
		
		double[] audioSamplesFromFile;
		audioSamplesFromFile = StdAudio.read("/Users/Andreugrimalt/Desktop/pap/pianoCrochet120.wav");
		int yinWindow=1103;
		// Create a buffer that will contain just 1 chunk of the audio file. The size has to be double of the Yin window size.
		double[] audioBuffer = new double[2*yinWindow];
		// Load chunks in the buffer and analyze each one of them. Careful with (i+j) and arrayIndexOutOfBounds.
		for(int j=0; j<audioSamplesFromFile.length-(2*yinWindow); j+=(2*yinWindow)){
			for(int i=0; i<audioBuffer.length; i++){
				audioBuffer[i]=audioSamplesFromFile[i+j];
			}
			Yin yin =new Yin(yinWindow);
			
			yin.autocorrelation(audioBuffer);
			yin.differenceFunction(audioBuffer);
			yin.cumulativeMeanDifferenceFunction();
			yin.absoluteThreshold();
			yin.findDips();
			yin.bestLocalEstimate();
			data.add(yin.printData(j));
		}
		
		
		//YIN for signals that have amplitude variations
		/*
		Signal sine = new Signal(0.3,440);
		for(int i=1104; i<1105; i++){
			
			// To check how a windowed signal behaves.
			/*
			double[] s = new double[2*i];
			for(int j=0; j<i; j++){
				s[j] = sine.getSamples()[j] * 0.5*(1-Math.cos(2*Math.PI*j/(s.length/2-1)));
			}
			for(int j=i; j<2*i; j++){
				s[j] = s[j-i];
			}
			for(int j=0; j<s.length; j++){
				//System.out.println(j+", "+s[j]);
			}*/
		/*
			// Exponential decay
			double[] s= new double[2*i];
			for(int j=0; j<2*i; j++){
				s[j] = sine.getSamples()[j]*Math.exp(-j/100.0);
				//System.out.println(j+", "+s[j]);
			}

			Yin yin = new Yin(i);
			// Calculates autocorrelation.
			yin.autocorrelation(s);
			yin.differenceFunctionAmplitudeVariation(s);
			//yin.differenceFunction(s);
			yin.cumulativeMeanDifferenceFunction();
			yin.absoluteThreshold();
			yin.findDips();
			// Why we need to divide by 2?
			System.out.println(i+", "+yin.calcFreq()/2.0);
		}*/
	
		
		//YIN for sine tones
		/*
		Yin yin = new Yin(1104);
		Signal sine = new Signal(0.3,4234);
		yin.autocorrelation(sine.getSamples());
		yin.differenceFunction(sine.getSamples());
		yin.cumulativeMeanDifferenceFunction();
		yin.absoluteThreshold();
		yin.findDips();
		yin.calcFreq(); // To check that the best estimate is really the best.
		System.out.println("Best Estimation= "+yin.bestLocalEstimate());
		*/
		
		// YIN different window sizes.
		/*
		Signal sine = new Signal(0.3,1000);
		for(int i=1; i<3000; i++){
		Yin yin = new Yin(i);
	
		// Calculates autocorrelation.
		
		yin.autocorrelation(sine.getSamples());
		yin.differenceFunction(sine.getSamples());
		yin.cumulativeMeanDifferenceFunction();
		yin.absoluteThreshold();
		yin.findDips();
		yin.bestLocalEstimate();
		System.out.println(i+", "+yin.printData(0)[0]);
		}*/
	}
}
