package br.com.bj.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class TesteSpark{
	private static final Pattern SPACE = Pattern.compile( " " );
	private static final Map< String, Integer > MAP = new HashMap< >();

	static {
		MAP.put( "Joao", 1 );
		MAP.put( "mesma", 1 );
		MAP.put( "em", 1 );
		MAP.put( "cidade", 1 );
		MAP.put( "maria", 1 );
		MAP.put( "piratininga,", 1 );
		MAP.put( "onde", 1 );
		MAP.put( "trabalha", 2 );
	}

	public static void main( String args[] ) {
		JavaSparkContext sparkContext = new SparkUtil().createJavaSparkContext();
		List< String > tokens = Arrays.asList( "Joao trabalha em piratininga, mesma cidade onde trabalha maria" );

		JavaRDD< String > lines = sparkContext.parallelize( tokens );
		JavaRDD< String > words = lines.flatMap( word -> Arrays.asList( SPACE.split( word ) ) );
		JavaPairRDD< String, Integer > counts = words.mapToPair( token -> new Tuple2< String, Integer >( token, 1 ) ).reduceByKey( ( x, y ) -> x + y );

		List< Tuple2< String, Integer > > output = counts.collect();
		for ( Tuple2< String, Integer > tuple : output ) {
			System.out.println( tuple._2() + " - " + MAP.get( tuple._1() ) );
		}
	}

}