import qupath.lib.objects.PathObjects
import qupath.lib.objects.PathCellObject
import qupath.lib.roi.ROIs
import qupath.lib.regions.ImagePlane

// get current image and server
def imageData = getCurrentImageData()
def server = imageData.getServer()
selectAnnotations()
def detections = getDetectionObjects() 
println '==='
println 'Detections: ' + detections.size()
println '==='

// export results
def name = getProjectEntry().getImageName() + '.txt'
def path = buildFilePath(PROJECT_BASE_DIR, 'TH_counts_results')
mkdirs(path)
path = buildFilePath(path, name)
saveAnnotationMeasurements(path, "Image","Num Detections")
print 'Results exported to ' + path