/*

Summary
-------

Signal class generates a sine tone of a specified frequency.

*/

package miniproject;

public class Signal {

	public static final int SAMPLE_RATE=44100;
	public static int numSamples;
	private double[] samples;
	
	public Signal(double seconds, double frequency){
		// Number of samples.
		numSamples = (int)(SAMPLE_RATE*seconds);
		// Initialise samples array.
		samples = new double[numSamples];
		// Phase.
		double phase = 2*Math.PI*frequency/SAMPLE_RATE;
		// Fill array with a sine tone with frequency=f.
		for(int i=0; i<numSamples; i++){
			samples[i]=Math.sin(phase*i);
			//System.out.println(samples[i]+","+i);
		}
	}
	
	public double[] getSamples(){
		return samples;
	}
}
