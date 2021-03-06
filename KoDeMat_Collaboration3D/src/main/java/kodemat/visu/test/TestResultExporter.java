/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kodemat.visu.test;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Amjad
 */
public class TestResultExporter {

    public static void writeTestResult(long startTime, long beforeProcessingtime, long endTime) {
        try {
            String filename = "Test_Results.txt";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            // we will write end-start, then beforeProcessingtime - start
            fw.write((endTime-startTime)+","+(beforeProcessingtime-startTime)+"\n");//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }
}
