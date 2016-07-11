package br.com.bj.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkUtil{
	private final String APP_NAME = "Brow-JoeSpk";
	private final String MASTER = "local";
	private final String MEMORY_EXECUTOR = "4g";

	public JavaSparkContext createJavaSparkContext() {
		String path = getPath();
		if ( StringUtils.isEmpty( path ) ) {
			return null;
		} else {
			System.setProperty( "hadoop.home.dir", path );

			SparkConf sparkConf = getSparkConfig();
			return new JavaSparkContext( sparkConf );
		}
	}

	private SparkConf getSparkConfig() {
		return new SparkConf()
		/**/ .setAppName( APP_NAME )
		/**/ .setMaster( MASTER )
		/**/ .set( "spark.executor.memory", MEMORY_EXECUTOR );
		/*.set( "spark.eventLog.enabled", "true" )
		  .set( "spark.serializer", "org.apache.spark.serializer.KryoSerializer" )
		  .set( "spark.driver.cores", "1" )
		  .set( "spark.local.dir", "/tmp" )
		  .set( "spark.reducer.maxSizeInFlight", "48m" )
		  .set( "spark.hadoop.cloneConf", "false" )*/
	}

	private String getPath() {
		try {
			URL url = getClass().getResource( "/hadoop-2.6.4/" );
			URI uri = url.toURI();

			StringBuilder path = new StringBuilder( new File( uri.getPath() ).getAbsolutePath() );
			path.append( File.separator );

			return path.toString();
		} catch ( URISyntaxException e ) {
			e.printStackTrace();
		}
		return null;
	}

}