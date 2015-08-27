# JSMin Ant Task #

**A custom task for Apache Ant which acts as an interface to the JSMin program.**

## About JSMin ##

_"JSMin is a filter which removes comments and unnecessary whitespace from javascript files. It typically reduces filesize by half, resulting in faster downloads. It also encourages a more expressive programming style because it eliminates the download cost of clean, literate self-documentation."_

Excerpt from [Douglas Crockford's JSMin page](http://www.crockford.com/javascript/jsmin.html)

## Description ##

The JSMin Ant task acts as an interface to the JSMin Java class. The task can be used for the automated minification of javascript files in your build and deploy processes.

  * Specify destination directory option.
  * Suffix minified files option.
  * Support for nested filesets
  * Tested with Apache Ant 6.5+. Please let us know if the task works with earlier versions!!

## Parameters ##

| **Attribute** | **Description** | **Required** |
|:--------------|:----------------|:-------------|
| srcfile       | Specify a javascript source file to be minified. | Yes, unless a nested fileset has been specified. |
| suffix        | If `true` suffix minified files with .min. IE: file.js becomes file.min.js. Default is `false`. | No           |
| suffixvalue   | If `suffix` attribute is `true` then you can pass a custom suffix value through. IE: With `-min` file.js becomes file-min.js. Default is `.min`. | No           |
| destdir       | The destination directory for minified files to be outputted to. Task defaults to the files source directory if ommitted. | No           |
| force         | Overwrite a destination file if it already exists. Default is `false` | No           |
|copyright      | Prepend a copyright notice to the minified files | No           |

## Examples ##

Define the task in your build file:

```
<taskdef name="jsmin"
        classname="net.matthaynes.jsmin.JSMin_Task"
        classpath="/path/to/jsmin.jar"/>
```

Using the task on a single file, with the _.min_ suffix added to the file extension:

```
<jsmin srcfile="file.js" suffix="true" />
```

Using the task with a fileset, and an output directory specified:

```
<jsmin destdir="./outputdirectory">
       <fileset dir="./somejsdirectory/" includes="**/*.js"/>
</jsmin>
```

Prepending a copyright notice to the minified file:

```
<jsmin srcfile="file.js" copyright="(c) 2008 whoever.com" />
```

Will add the following to the top of the file:

```
/* (c) 2008 whoever.com */
```

## Support ##

If you have any issues then please let us know, I will see what I can do.  I would also be very interested to hear what platforms and Ant versions people are using as I have not had time to extensivley test this task.

## Project Stats ##

&lt;wiki:gadget url="http://www.ohloh.net/projects/jsmin-ant-task/widgets/project\_languages.xml" border="1" width="350" height="200" /&gt;

&lt;wiki:gadget url="http://www.ohloh.net/projects/jsmin-ant-task/widgets/project\_factoids.xml" border="0" width="350" height="125" /&gt;

&lt;wiki:gadget url="http://www.ohloh.net/projects/jsmin-ant-task/widgets/project\_cocomo.xml"  border="0" width="350" height="240" /&gt;

&lt;wiki:gadget url="http://www.ohloh.net/projects/jsmin-ant-task/widgets/project\_basic\_stats.xml" height="220"  border="1" /&gt;