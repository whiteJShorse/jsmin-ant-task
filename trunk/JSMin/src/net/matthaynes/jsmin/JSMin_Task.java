/*
 * JSMin_Task.java
 * 
 * MHAYNES 03/06/2007
 * 
 * Ant task for the JSMin class.
 * 
 * USAGE:
 *  	<taskdef name="jsmin"
 *  		classname="net.matthaynes.jsmin.JSMin_Task" 
 *  		classpath="jsmin.jar"/>
 *
 *  	<jsmin>
 *			<fileset  dir="js_dir/" includes="*.js"/>
 *       </jsmin>    
 * 
 * TO DO:
 * 		Improve javadoc.
 */

// Part of this package
package net.matthaynes.jsmin;

// Import dependancies
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.BuildException;

import java.util.Vector;
import java.io.*;


public class JSMin_Task extends Task {

	Vector<FileSet> filesets = new Vector<FileSet>();	
	String srcfile;
	
	/**
	 * Receives a nested fileset from the ant task 
	 * @param fileset The nested fileset to recieve.
	 */
	public void addFileSet(FileSet fileset) {
		if (!filesets.contains(fileset)) {
			filesets.add(fileset);
		}
	}	
	
	/**
	 * Receives the srcfile attribute from the ant task
	 * @param srcfiles
	 */
    public void setSrcfile(String srcfile) {
        this.srcfile = srcfile;
    }
    
    /**
     * Calls the JSMin class using the file parameter
     * @param file The file for the JSMin class to parse
     */
    public void callJsMin(String file) {
    	log("Minimizing " + file);
    	
    	// Construct name of ouput file, just add th .min before the last .
    	String outputFile = file.substring(0,file.lastIndexOf("."));
    	outputFile = outputFile + ".min";
    	outputFile = outputFile + file.substring(file.lastIndexOf("."), file.length());

    	// Call JSMin class main method, pass through input and output file
    	String[] arguments = new String[] {file, outputFile};
    	JSMin.main(arguments);
    	
    }

    /**
     * Executes the task
     */
    public void execute() throws BuildException { 
    	
    	// If we have a src file passed through....
    	if (this.srcfile != null) {
    		
    		// Call JSMin class with src file passed through
    		callJsMin(this.srcfile);
    		
    	// Otherwise if there is a fileset ...
    	} else if (filesets.size() != 0) {
    		
    		// Loop through fileset
            for (int i = 0; i < filesets.size(); i++) {

            	// Get current fileset
            	FileSet fs = (FileSet)filesets.elementAt(i);
            	
            	// Ummm....?
            	DirectoryScanner ds = fs.getDirectoryScanner(getProject());

            	// Get base directory from fileset
            	File dir = ds.getBasedir();
            	
            	// Get included files from fileset
            	String[] srcs = ds.getIncludedFiles();

            	// Loop through files
            	for (int j = 0; j < srcs.length; j++) {
            		  
            		// Make file object from base directory and filename
        			File temp = new File(dir,srcs[j]);
            		
        			// Call the JSMin class with this file
        			callJsMin(temp.getAbsolutePath());
            	 }
            }    
    		
        // If no srcfile or fileset passed through, throw ant error
    	} else {
    		throw new BuildException("You must specify a srcfile attribute or a fileset child element", getLocation());    		
    	}
        
    }

}