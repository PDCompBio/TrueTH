import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathAnnotationObject
import qupath.lib.objects.PathDetectionObject

// Get the list of all images in the current project
def project = getProject()
def imagesToExport = project.getImageList()

// Separate each measurement value in the output file with a tab ("\t")
def separator = ","

// Choose the columns that will be included in the export
// Note: if 'columnsToInclude' is empty, all columns will be included
def columnsToInclude = new String[]{"Image", "Num Detections"}

def AnnotationExportType = PathAnnotationObject.class
def DetectionExportType = PathDetectionObject.class
// Choose your *full* output path
def outputPath =  buildFilePath(PROJECT_BASE_DIR,"detection")
mkdirs(outputPath)
def AnnoOutputFile = new File(outputPath+"\\"+"detection.csv")
def DetecOutPutFile = new File(outputPath+"\\"+"detection_measurements.csv")
// Create the measurementExporter and start the export
def AnnoExporter  = new MeasurementExporter()
                  .imageList(imagesToExport)            // Images from which measurements will be exported
                  .separator(separator)                 // Character that separates values
                  .includeOnlyColumns(columnsToInclude) // Columns are case-sensitive
                  .exportType(AnnotationExportType)               // Type of objects to export
                  //.filter(obj -> obj.getPathClass() == getPathClass("positive"))    // Keep only objects with class 'positive'
                  .exportMeasurements(AnnoOutputFile)        // Start the export process

def DetecExporter  = new MeasurementExporter()
                  .imageList(imagesToExport)            
                  .separator(separator)                 
                  //.includeOnlyColumns(columnsToInclude) 
                  .exportType(DetectionExportType)              
                  //.filter(obj -> obj.getPathClass() == getPathClass("positive"))
                  .exportMeasurements(DetecOutPutFile)        



print "Done!"