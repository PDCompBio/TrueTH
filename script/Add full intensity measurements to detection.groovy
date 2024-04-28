import qupath.lib.analysis.features.ObjectMeasurements
import qupath.lib.images.ImageData
import qupath.lib.images.servers.ImageServerMetadata
import qupath.lib.images.servers.TransformedServerBuilder

def imageData = getCurrentImageData()
def currentserver = getCurrentServer()// for IF image
def server = new TransformedServerBuilder(imageData.getServer())
    .deconvolveStains(imageData.getColorDeconvolutionStains())
    .build()
 
def measurements = ObjectMeasurements.Measurements.values() as List
def compartments = ObjectMeasurements.Compartments.values() as List 
def downsample = 1.0
for (detection in getDetectionObjects()) {
  ObjectMeasurements.addIntensityMeasurements(
      server, detection, downsample, measurements, compartments
      )
}
for (detection in getDetectionObjects()) {
  ObjectMeasurements.addIntensityMeasurements(
      currentserver, detection, downsample, measurements, compartments
      )
}

detections = getDetectionObjects()
detections.each{
    relativeDistribution2 = measurement(it, "Red: Mean")-measurement(it, "Green: Mean")
    it.getMeasurementList().putMeasurement("Red/Green", relativeDistribution2)
}
println("done")