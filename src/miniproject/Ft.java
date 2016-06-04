/*
 * This is a Fourier Transform
 */
package miniproject;


public class Ft {
	
	private final int SAMPLE_RATE=44100;
	int window=1100;
	int numSamples=0;
	private double[][] complexSignal=new double[window][2];
	double[] audioSamplesFromFile;
	double[] audioBuffer = new double[window];

	int count=0;
	
	Ft(){
		
		audioSamplesFromFile = StdAudio.read("/Users/Andreugrimalt/Desktop/pap/piano220440.wav");
		numSamples=audioSamplesFromFile.length;
	}
	
	public void complexSignal(double complexFreq){
		double complexPhase = 2*Math.PI*complexFreq/SAMPLE_RATE;
		for(int i=0; i<window; i++){
			complexSignal[i][0]=Math.cos(complexPhase*i);
			complexSignal[i][1]=Math.sin(complexPhase*i);		
		}
	}
	public void dotProduct(int iteration){
		double[] dotP = new double[2];
		for(double i=0; i<1000; i+=0.5){
			complexSignal(i);
			for(int j=0; j<window; j++){
				dotP[0]+=audioBuffer[j]*complexSignal[j][0];
				dotP[1]+=audioBuffer[j]*complexSignal[j][1];
			}
			/*if(dotP[0]<1E-3){
				dotP[0]=0;
			}
			if(dotP[1]<1E-3){
				dotP[1]=0;
			}*/
			
			System.out.println(i+", "+Math.sqrt(dotP[0]*dotP[0]+dotP[1]*dotP[1]));
			// To do a 3D plot
			//System.out.println(i+", "+iteration+", "+Math.sqrt(dotP[0]*dotP[0]+dotP[1]*dotP[1]));
			dotP[0]=0;
			dotP[1]=0;
		}
	}
	

	
	
	
	public static void main(String[]args){
		Ft ft = new Ft();
	
		// Create a buffer that will contain just 1 chunk of the audio file. The size has to be double of the Yin window size.
		// Load chunks in the buffer and analyze each one of them. Careful with (i+j) and arrayIndexOutOfBounds.
		int k=0;
		for(int j=0; j<ft.audioSamplesFromFile.length-(ft.window); j+=ft.window){
			for(int i=0; i<ft.audioBuffer.length; i++){
				ft.audioBuffer[i]=ft.audioSamplesFromFile[i+j];
			}
			ft.dotProduct(k++);			
		}

	}
	
}
