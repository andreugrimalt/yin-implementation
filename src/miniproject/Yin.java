/*

Summary
-------

YIN class implements the YIN algorithm for pitch estimation on audio signals.

Description
-----------

1. Calculate autocorrelation of the signal.
2. Calculate difference function.
3. Calculate cumulative mean difference function. 
4. Apply absolute threshold.
5. Look for minima and perform parabolic interpolation
6. Select the best estimate.
7. Convert the best estimate units from samples to Hz

*/
package miniproject;

import java.util.ArrayList;

public class Yin {
	
	private double[] autoCorrelation;
	private double[] differenceFunction;
	
	private double[] cumulativeMeanDifference;
	private double[] absoluteThreshold;
	ArrayList<Double> tau = new ArrayList<Double>();
	ArrayList<Double> tauY = new ArrayList<Double>();
	ArrayList<Double> frequencies = new ArrayList<Double>();
	private int maxLag;
	double frequencyEstimate;
	
	public Yin(int theMaxLag){
		maxLag=theMaxLag;
		autoCorrelation = new double[maxLag];
		differenceFunction = new double[maxLag];
		cumulativeMeanDifference = new double[maxLag];
		absoluteThreshold = new double[maxLag];
	
	}
	
	public double[] getAutoCorrelation(){
		return autoCorrelation;
	}

	public void autocorrelation(double[] theSignal){

		for(int i=0; i<maxLag; i++){
			for(int j=0; j<maxLag; j++){
				autoCorrelation[i] += theSignal[j]*theSignal[(j+i)];
			}
			//System.out.println(i+", "+autoCorrelation[i]);	
		}
	}
	
	/*public void modifiedAutocorrelation(double[] theSignal){

		for(int i=0; i<maxLag; i++){
			for(int j=0; j<maxLag-i; j++){
				autoCorrelation[i] += theSignal[j]*theSignal[(j+i)];
			}
			//System.out.println(autoCorrelation[i]+", "+i);
		}
	}*/

	public void differenceFunction(double[] theSignal){
		
		for(int i=0; i<maxLag; i++){
			double autoCorrelationTau=0;
			// Value of r(0) for each tau.
			for(int j=i; j<i+maxLag; j++){
				autoCorrelationTau+= theSignal[j]*theSignal[j];
			}
			//System.out.println(autoCorrelationTau);
			differenceFunction[i]=autoCorrelation[0]+autoCorrelationTau-2*autoCorrelation[i];
			//System.out.println(i+", "+differenceFunction[i]);
		}
		//System.out.println(autoCorrelation[0]);
	}
	
	public void differenceFunctionAmplitudeVariation(double[] theSignal){
		
		for(int i=0; i<maxLag; i++){
			double autoCorrelationTau=0;
			// Value of r(0) for each tau.
			for(int j=i; j<i+maxLag; j++){
				autoCorrelationTau+=theSignal[j]*theSignal[j];
			}
			differenceFunction[i]=autoCorrelation[0]*(1-((autoCorrelation[i]*autoCorrelation[i])/(autoCorrelation[0]*autoCorrelationTau)));
			//System.out.println(i+", "+differenceFunction[i]);
		}
		//System.out.println(autoCorrelation[0]);
	}
	
	public void cumulativeMeanDifferenceFunction(){
		double sum=0;

		for(int i=0; i<maxLag; i++){
			if(i==0){
			cumulativeMeanDifference[i] = 1;
			}else{
				sum+=differenceFunction[i];
				cumulativeMeanDifference[i]=differenceFunction[i]/((1.0/i)*sum);
			}
			//System.out.println(i+", "+cumulativeMeanDifference[i]);
		}
	}
	
	public void absoluteThreshold(){
		double threshold = 0.1;
		for(int i=0; i<maxLag; i++){
			if(cumulativeMeanDifference[i]<=threshold){
				absoluteThreshold[i] = cumulativeMeanDifference[i];
			}else{
				absoluteThreshold[i] = 1;
			}
			//System.out.println(i+", "+absoluteThreshold[i]);
		}
	}
	
	public void findDips(){
		int j=1;
		ArrayList<Integer> dip = new ArrayList<Integer>();
		
		while(j<maxLag-1){
			if(absoluteThreshold[j]!=1.0&&absoluteThreshold[j-1]>absoluteThreshold[j]&&absoluteThreshold[j]<absoluteThreshold[j+1]){
				dip.add(j);
			}
			j++;
		}
		int k=0;
		while(k<dip.size()){
			
			k++;
		}
		for(int i=0; i<dip.size();i++){
			
			//System.out.println("-------------------");
			//System.out.println("dip"+i+": "+dip.get(i)+", "+absoluteThreshold[dip.get(i)]);
			//System.out.println("parabolic dips x= "+(dip.get(i)-1)+", "+dip.get(i)+", "+(dip.get(i)+1));
			//System.out.println("parabolic dips y= "+absoluteThreshold[(dip.get(i)-1)]+", "+absoluteThreshold[dip.get(i)]+", "+absoluteThreshold[(dip.get(i)+1)]);
			parabollicInterpolation(dip.get(i)-1,dip.get(i),dip.get(i)+1);
		}
	}
	
	// Check all this.
	public void parabollicInterpolation(double a, double b, double c){
		// Fit to parabola of the form y=a*(x-x2)*(x-x2)+b*(x-x2)+y2
		double alpha=0;
		double betta=0;
		double gamma=0;
		
		double x1=a;
		double x2=b;
		double x3=c;
		double y1=absoluteThreshold[(int)a];
		double y2=absoluteThreshold[(int)b];
		double y3=absoluteThreshold[(int)c];
		alpha=((y3-y2)/(x3-x2)-(y2-y1)/(x2-x1))/(x3-x1);
		betta=((y3-y2)/(x3-x2)*(x2-x1)+(y2-y1)/(x2-x1)*(x3-x2))/(x3-x1);
		gamma=y2;
		
		//System.out.println("Eq: "+alpha+", "+betta+", "+gamma);
		
		
		// Inverse parabolic interpolation finds the vertex.
		double x=0;
		
		//System.out.println(a+","+b+","+c);
		for(int i=0; i<10; i++){
			// Iteration
			x = b-(1.0/2.0)*(Math.pow((b-a),2)/(b-a))*(absoluteThreshold[Math.round((float)b)]-absoluteThreshold[Math.round((float)c)])-Math.pow((b-c),2)*(absoluteThreshold[Math.round((float)b)]-absoluteThreshold[Math.round((float)a)])/((absoluteThreshold[Math.round((float)b)]-absoluteThreshold[Math.round((float)c)])-(b-c)*(absoluteThreshold[Math.round((float)b)]-absoluteThreshold[Math.round((float)a)]));
			
			if(x>b&&absoluteThreshold[(int)x]<absoluteThreshold[(int)b]){
				a=b;
				b=x;
				//c=c;
			}
		
			if(x>b&&absoluteThreshold[(int)x]>absoluteThreshold[(int)b]){
			    //a=a;
				//b=b;
				c=x;
			}
		
			if(x<b&&absoluteThreshold[(int)x]<absoluteThreshold[(int)b]){
				//a=a;
				b=x;
				//c=c;
			}
		
			if(x<b&&absoluteThreshold[(int)x]>absoluteThreshold[(int)b]){
				a=x;
				//b=b;
				//c=c;
			}
		}
		// Check the ordinate to determine best estimate.
		double tauOrdinate = alpha*(x-x2)*(x-x2) + betta*(x-x2) + gamma;
		//System.out.println("Parabolic interpolation x= "+x+ " y= "+tauOrdinate);
		tauY.add(tauOrdinate);
		tau.add(x);
	
	}
	
	public double bestLocalEstimate(){
		/* Check for the minimum of d'(tau) in the window*/
		double bestFrequency=0;
		if(tau.size()>0){
			double tempMin=tauY.get(0);
			double index = 0; 
			for(int i=0; i<tauY.size();i++){
				if(tauY.get(i)<tempMin){
					tempMin=tauY.get(i);
					index=i;
				}
			}
			bestFrequency=calcFreq(index);
			//System.out.println("On dip= "+(index+1));
			//System.out.println("BBBest Local is: "+tempMin+", "+(index+1));
		}
		if(tau.size()==0){
			bestFrequency=0;
		}
		frequencyEstimate=bestFrequency;
		return bestFrequency;
		
		//double freq=1/((tau.get((int)index)/(index+1))/Signal.SAMPLE_RATE);
		//System.out.println("best freq= "+freq);
		
	}
	
	public double calcFreq(double index){
		double freq=0;
		//System.out.println("index is= "+index);
		if(index<tau.size()){
		freq=1/((tau.get((int)index)/(index+1))/Signal.SAMPLE_RATE);
		}else{
			freq=0;
		}
		return freq;
	}

	public int getMidiNumber(double frequency){
		int midiNumber = (int)(69+12*Math.log(Math.round(frequency)/440.0));
		return midiNumber;
	}
	
	public double[] printData(int timeFrame){

			double[] dataArray = new double[3];
			dataArray[0]=frequencyEstimate;
			dataArray[1]=getMidiNumber(frequencyEstimate);
			// Approximate to 0
			if(dataArray[1]<0){
				dataArray[1]=0;
			}
			dataArray[2]=timeFrame/44100.0;
			//System.out.println(dataArray[0]);
			System.out.println("Best Estimation (Hz) = "+dataArray[0]+"		Midi Note = "+dataArray[1]+"		TimeFrame (s) = "+dataArray[2]);
			return dataArray;
	}
}
