/**
 *
 * JSMin_Task is ant Ant interface to the JSMin.java program.
 *
 * Usage:
 *  	<taskdef name="jsmin"
 *  		classname="net.matthaynes.jsmin.JSMin_Task"
 *  		classpath="jsmin.jar"/>
 *
 *  	<jsmin>
 *			<fileset  dir="js_dir/" includes="*.js"/>
 *      </jsmin>
 *
 * Updated 1st August 2007
 * @author Matthew Haynes
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

	Vector filesets = new Vector();
	String srcfile;
	String destdir;
	boolean suffix;
	boolean force = false;

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
	 * Receives the destdir attribute from the ant task.
	 * @param destdir
	 */
	public void setDestdir (String destdir) {
		this.destdir = destdir;
	}

    /**
     * Receives the force attribute from the ant task
     * @param force
     */
    public void setForce(boolean force) {
    	this.force = force;
    }	

	/**
	 * Receives the srcfile attribute from the ant task
	 * @param srcfiles
	 */
    public void setSrcfile(String srcfile) {
        this.srcfile = srcfile;
    }

    /**
     * Receives the suffix attribute from the ant task
     * @param suffix
     */
    public void setSuffix(boolean suffix) {
    	this.suffix = suffix;
    }

    /**
     * Calls the JSMin class using the file parameter. First outputs to a temp file and then
     * renames this to the file specified in getOutputFile() method.
     * @param file The name of the js file for the JSMin class to parse.
     */
    public void callJsMin(String file) {

    	try {

        	// Declare src file
        	File srcFile = new File(file);

        	// Declare output file
        	File output = new File (getOutputDirectory(srcFile),getOutputFileName(srcFile));

        	// Declare temp file
        	File tmpFile = File.createTempFile("JSMinAntTask","tmp");

        	// Declare input / output streams
        	FileInputStream inputStream = new FileInputStream(srcFile);
        	FileOutputStream outputStream = new FileOutputStream(tmpFile);

        	// Invoke JSMin, passing params through as file input and output streams
        	JSMin jsmin = new JSMin(inputStream, outputStream);
    		jsmin.jsmin();

    		// Close file streams
    		inputStream.close();
    		outputStream.close();

			// Copy temp file to output file.
    		copyFile(tmpFile,output);
    		
    		// Delete the temp file
    		deleteFile(tmpFile);

    	} catch(Exception e) {

    		throw new BuildException(e);

    	}

    }
    
    /**
     * Copies one file for another, if global variable force==true
     * @param src The source file to copy
     * @param dst The destination file to copy to
     */
    public void copyFile (File src, File dst) throws IOException {
    	
    	if (dst.exists() && force == false) {
    		
    		log("Not Minimizing " + dst.getAbsolutePath() + ". File already exists, use the force attrbute to overwrite.");
    		
    	} else {
    	
	        InputStream in = new FileInputStream(src);
	        OutputStream out = new FileOutputStream(dst);
	    
	        // Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        
	        in.close();
	        out.close();
	        
	        log("Minimizing " + dst.getAbsolutePath());
    	}
    }
    
    /**
     * Deletes specified file
     * @param file
     */
    public void deleteFile(File file) {
    	
    	file.delete();
    	
    }
    
   /**
     * Returns the output filename, adds .min.js as suffix attribute is set to true.
     * @param file The source file.
     * @return outputFile The output filename.
     */
    public String getOutputFileName (File file) {

    	// Get output file name....
    	String outputFile;
    	String inputFile = file.getName();

    	if (this.suffix == true) {

        	// Construct name of ouput file, just add th .min before the last .
        	outputFile = inputFile.substring(0,inputFile.lastIndexOf("."));
        	outputFile = outputFile + ".min";
        	outputFile = outputFile + inputFile.substring(inputFile.lastIndexOf("."), inputFile.length());

    	}  else {
    		outputFile = inputFile;
    	}

    	return outputFile;
    }

    /**
     * Returns the output directory, uses destdir if specified, creating it if doesn't already exist.
     * @param file The source file.
     * @return outputDirectory The output directory.
     */
    public File getOutputDirectory (File file) {

        File outputDirectory;

        // If destdir has been set then use it
    	if (this.destdir != null) {

    		outputDirectory = new File(this.destdir);

    		// If destdir doesn't exist then create it...
    		if (!outputDirectory.isDirectory()) {

    			try {

    				// Make directory
    				outputDirectory.mkdirs();

    			} catch(Exception e) {

    				throw new BuildException(e);

    			}
    		}

    	} else {

    		// Use source directory...
    		outputDirectory = new File(file.getParent());
    	}

    	return outputDirectory;
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