import qupath.lib.regions.*
import ij.*
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

// Read RGB image & show in ImageJ (won't work for multichannel!)
double downsample = 1.0
def server = getCurrentImageData().getServer()
int w = (server.getWidth() / downsample) as int
int h = (server.getHeight() / downsample) as int
def img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY)

def g2d = img.createGraphics()
g2d.scale(1.0/downsample, 1.0/downsample)
g2d.setColor(Color.WHITE)
// for detection 
for (detection in getDetectionObjects()) {
 roi = detection.getROI()
 def shape = roi.getShape()
 g2d.setPaint(Color.white);
 g2d.fill(shape)
 g2d.setStroke(new BasicStroke(2)); // 8-pixel wide pen
 g2d.setPaint(Color.black);
 g2d.draw(shape)
}

g2d.dispose()

def name = getProjectEntry().getImageName() //+ '.tiff'
def path = buildFilePath(PROJECT_BASE_DIR, 'detection mask')
mkdirs(path)
def fileoutput = new File( path,name+'.'+'png')
ImageIO.write(img, 'png', fileoutput)
println('Results exporting...')