public class ScoringAlg {
	public static void main(String[] args) {
		double test = 0;
		int [] RPMArr = new int [20];
		for( int i = 0; i < RPMArr.length; i++){
			RPMArr[i] = 3000;
		}
		
		test = calcScore (1000, 2500, 4000, RPMArr, .15);
		System.out.println("Score: " + test);
	}
	
	public static double calcScore (int lowerBase, int midBase, int upperBase, int [] parameters, double weight) {
		int hardBaseCount = 0, normBaseCount = 0 , easyBaseCount = 0, zeroCount = 0;
		double normPct, easyPct, zeroPct, acceptPct;
		double score;

		for ( int i = 0; i < parameters.length; i ++ ) {
			if ( parameters[i] > upperBase ) {
				hardBaseCount++;
			}
			else if (parameters[i] > midBase){
				normBaseCount++;
			}
			else if (parameters[i] > lowerBase) {
				easyBaseCount++;
			}
			else {
				zeroCount++;
			}
		}

		normPct = (double) normBaseCount / (double) parameters.length;
		easyPct = (double) easyBaseCount / (double) parameters.length;
		zeroPct =  (double) zeroCount / (double) parameters.length;

		acceptPct = normPct + easyPct + zeroPct;	
		score = acceptPct * weight * 1000;

		return score;
	}
}


