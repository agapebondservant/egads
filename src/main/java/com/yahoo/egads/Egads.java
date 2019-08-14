// main entry point for egads processing node

package com.yahoo.egads;

/*
 * Call stack.
 * 
 * Anomaly Dtection Use Case (Assuming a trained model). 
 * Egads.Main()
 *   ProcessableObjectFactory.create
 *     Create AnomalyDetector
 *       BuildADModel()
 *     Create ModelAdapter
 *       BuildTSModel()
 *   po.process
 *     TODO: write to anomaly DB.
 * 
 */

public class Egads {
    public static void main(String[] args) throws Exception {
        EgadsService.execute(args);
    }
}
