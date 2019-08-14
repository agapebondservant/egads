package com.yahoo.egads;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.egads.utilities.FileInputProcessor;
import com.yahoo.egads.utilities.FileUtils;
import com.yahoo.egads.utilities.InputProcessor;
import com.yahoo.egads.utilities.StdinProcessor;

/**
 * 
 * @author
 * Injectable version of the Egads Processor.
 *
 */
public class EgadsService {
	static Logger LOGGER = LoggerFactory.getLogger(EgadsService.class);
	
	public static void execute(String... args) throws Exception {

        if (args.length == 0) {
            throw new Exception("Usage: java Egads config.ini (input [STDIN,CSV])");
        }

        // TODO: This config will be retrieved from ConfigDB later,
        // for now it is assumed it's a static file.
        Properties p = new Properties();
        String config = args[0];
        File f = new File(config);
        boolean isRegularFile = f.exists();
        
        if (isRegularFile) {
            InputStream is = new FileInputStream(config);
            p.load(is);
        } else {
        	FileUtils.initProperties(config, p);
        }
        
        // Set the input type.
        InputProcessor ip = null;
        if (p.getProperty("INPUT") == null || p.getProperty("INPUT").equals("CSV")) {
            ip = new FileInputProcessor(args[1]);
        } else {
            ip = new StdinProcessor();
        }
        
        // Set other properties, if applicable
        if (args.length > 2) {
        	// Set the AD_MODEL: must be one of : 
        	//  1. ExtremeLowDensityModel
        	//  2. AdaptiveKernelDensityChangePointDetector
            //  3. KSigmaModel
        	//  4. NaiveModel
        	//  5. DBScanModel
        	//  6. SimpleThresholdModel
        	String ad = args[2];
        	if (!ad.matches("^[1-6]")) {
        		throw new Exception("Error: AD_TYPE must be between 1 and 6");
        	}
            LOGGER.debug("Level was " + getSupportedTypes()[Integer.valueOf(ad)-1]);
        	p.setProperty("AD_MODEL", getSupportedTypes()[Integer.valueOf(ad)-1]);
        }
        
        // Process the input the we received (either STDIN or as a file).
        ip.processInput(p);
	}
	
	private static String[] getSupportedTypes() {
		return new String[] {
				"ExtremeLowDensityModel",
	        	"AdaptiveKernelDensityChangePointDetector",
	            "KSigmaModel",
	        	"NaiveModel",
	        	"DBScanModel",
	        	"SimpleThresholdModel"
		};
	}
}
