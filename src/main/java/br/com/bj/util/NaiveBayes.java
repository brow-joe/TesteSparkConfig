package br.com.bj.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

//TODO Java 8
public class NaiveBayes{
	private static String[ ][ ] data = {
			/**/{ "Sunny", "Hot", "High", "Weak" },
			/**/{ "Sunny", "Hot", "High", "Strong" },
			/**/{ "Overcast", "Hot", "High", "Weak" },
			/**/{ "Rain", "Mild", "High", "Weak" },
			/**/{ "Rain", "Cool", "Normal", "Weak" },
			/**/{ "Rain", "Cool", "Normal", "Strong" },
			/**/{ "Overcast", "Cool", "Normal", "Strong" },
			/**/{ "Sunny", "Mild", "High", "Weak" },
			/**/{ "Sunny", "Cool", "Normal", "Weak" },
			/**/{ "Rain", "Mild", "Normal", "Weak" },
			/**/{ "Sunny", "Mild", "Normal", "Strong" },
			/**/{ "Overcast", "Mild", "High", "Strong" },
			/**/{ "Overcast", "Hot", "Normal", "Weak" },
			/**/{ "Rain", "Mild", "High", "Strong" } };

	private static String[ ] labels = { "Outlook", "Temperature", "Humidity", "Wind" };

	private static String[ ] dataOutcomes = {
			/**/"No",
	        /**/"No",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"No",
	        /**/"Yes",
	        /**/"No",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"Yes",
	        /**/"No" };

	@SuppressWarnings( "unused" )
	private static String outcomeLabel = "Play Tennis?";

	public static void main( String[ ] args ) {
		List< String > outcomeList = Arrays.stream( dataOutcomes ).distinct().collect( Collectors.toList() );
		String[ ] outcomes = outcomeList.toArray( new String[ 0 ] );

		Map< Integer, Long > outcomeDataCounter = new HashMap< >();
		IntStream.range( 0, outcomes.length ).forEach( outcome -> {
			Long sum = Arrays.stream( dataOutcomes ).mapToLong( it -> StringUtils.equals( it, outcomes[ outcome ] ) ? 1 : 0 ).sum();
			outcomeDataCounter.put( outcome, sum );
		} );

		Map< Integer, Map< Integer, Map< Integer, Long > > > labelCounter = new HashMap< >();
		Map< Integer, String[ ] > labelTargets = new HashMap< >();

		//TODO Count
		IntStream.range( 0, labels.length ).forEach( label -> {
			List< String > targetList = Arrays.stream( data ).map( d -> d[ label ] ).distinct().collect( Collectors.toList() );
			String[ ] targets = targetList.toArray( new String[ 0 ] );

			Map< Integer, Map< Integer, Long > > targetCounter = new HashMap< >();

			IntStream.range( 0, targets.length ).forEach( target -> {

				Map< Integer, Long > outcomeCounter = new HashMap< >();
				IntStream.range( 0, outcomes.length ).forEach( outcome -> {
					long sum = IntStream.range( 0, data.length ).mapToLong( i -> ( StringUtils.equals( dataOutcomes[ i ], outcomes[ outcome ] ) ) ? ( StringUtils.equals( data[ i ][ label ], targets[ target ] ) ? 1 : 0 ) : 0 ).sum();
					outcomeCounter.put( outcome, sum );
				} );

				targetCounter.put( target, outcomeCounter );
			} );

			labelCounter.put( label, targetCounter );
			labelTargets.put( label, targets );
		} );

		Map< Integer, Map< Integer, Map< Integer, Double > > > targetCoeficients = new HashMap< >();
		//TODO Target Coeficients 
		labelCounter.entrySet().forEach( entry -> {

			Map< Integer, Map< Integer, Double > > targetCoeficient = new HashMap< >();
			entry.getValue().entrySet().forEach( target -> {

				Map< Integer, Double > outcomeCoeficients = new HashMap< >();
				target.getValue().entrySet().forEach( outcome -> {
					double coeficient = calculeBayes( outcome.getValue(), outcomeDataCounter.get( outcome.getKey() ) );
					outcomeCoeficients.put( outcome.getKey(), coeficient );
				} );

				targetCoeficient.put( target.getKey(), outcomeCoeficients );
			} );

			targetCoeficients.put( entry.getKey(), targetCoeficient );
		} );

		//TODO Outcome Coeficients
		long total = outcomeDataCounter.entrySet().stream().mapToLong( outcome -> {
			return outcome.getValue();
		} ).sum();
		Map< Integer, Double > outcomeCoeficients = new HashMap< >();
		outcomeDataCounter.entrySet().forEach( outcome -> {
			double coeficient = calculeBayes( outcome.getValue(), total );
			outcomeCoeficients.put( outcome.getKey(), coeficient );
		} );

		//TODO Print
		outcomeCoeficients.entrySet().forEach( entry -> {
			System.out.println( outcomes[ entry.getKey() ] + " : " + entry.getValue() );
		} );
		System.out.println();

		targetCoeficients.entrySet().forEach( entry -> {
			System.out.println( labels[ entry.getKey() ] );
			entry.getValue().entrySet().forEach( target -> {
				System.out.println( "\t" + labelTargets.get( entry.getKey() )[ target.getKey() ] );
				target.getValue().entrySet().forEach( outcome -> {
					System.out.println( "\t\t" + outcomes[ outcome.getKey() ] + " : " + outcome.getValue() );
				} );
			} );
			System.out.println();
		} );
	}

	private static double calculeBayes( long target, long outcome ) {
		double alpha = 0.01;
		return ( target + alpha ) / ( outcome + alpha );
	}
}