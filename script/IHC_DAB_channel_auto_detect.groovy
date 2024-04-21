import qupath.imagej.tools.IJTools
import qupath.lib.gui.images.servers.RenderedImageServer
import qupath.lib.gui.viewer.overlays.HierarchyOverlay
import qupath.lib.regions.RegionRequest
import qupath.ext.biop.cellpose.Cellpose2D
import static qupath.lib.gui.scripting.QPEx.*
import qupath.lib.display.settings.ImageDisplaySettings

double downsample = 1
// create a DAB channel rendered server for cell detection
def viewer = getCurrentViewer()
def imageData = getCurrentImageData()
def display = viewer.getImageDisplay()
display.setUseGrayscaleLuts(true)
def dab = display.availableChannels()[2] // 2 is the DAB channel
display.setImageData(imageData, true)
display.setChannelSelected(dab,true)
display.autoSetDisplayRange(dab,0.01)
display.setUseInvertedBackground(true)


def test = new ImageDisplaySettings().create("test", 1, false, ['DAB'])                    
def server = new RenderedImageServer.Builder(imageData)
    .display(display)
    .downsamples(downsample)
    //.layers(new HierarchyOverlay(viewer.getImageRegionStore(), viewer.getOverlayOptions(), imageData))
    .settings(test)
    .build()
def renderImageData = new ImageData<>(server, imageData.getHierarchy(), imageData.getImageType())

def pathModel = ('.\TrueTH model\TrueTH.339756') // change the model path
def cellpose = Cellpose2D.builder( pathModel )
//        .pixelSize( 0.48 )                  // Resolution for detection in um  
          .normalizePercentilesGlobal(0.1, 99.8, 5) // Convenience global percentile normalization. arguments are percentileMin, percentileMax, dowsample.
          .tileSize(1024)                  // If your GPU can take it, make larger tiles to process fewer of them. Useful for Omnipose
          .cellposeChannels(0,0)           //  use the grayscale channel
          .cellprobThreshold(-0.1)          // If too few ROIs are returned, lower the threshold. Likewise, increase this value if returning too much ROI
          .flowThreshold(0.7)              // If too few ROIs are returned, increase the threshold. Likewise, if returning too much ROI please lower this value
          //.diameter(39)                    // Median object diameter.
          .setOverlap( 15 )
        .addParameter("no_norm")         // Disable automatic normalization
//          .classify("TH")       // PathClass to give newly created objects
          .measureShape()                  // Add shape measurements
          .measureIntensity()              // Add cell measurements (in all compartments)
//        .createAnnotations()             // Make annotations instead of detections. This ignores cellExpansion
        .simplify(0)                     // Simplification 1.6 by default, set to 0 to get the cellpose masks as precisely as possible
        .build()

// Run detection for the selected objects

//def pathObjects = getSelectedObjects() // To process only selected annotations, useful while testing
def pathObjects = getAnnotationObjects() // To process all annotations. For working in batch mode
if (pathObjects.isEmpty()) {
    Dialogs.showErrorMessage("Cellpose", "Please select a parent object!")
    return
}

cellpose.detectObjects(renderImageData, pathObjects)

