def PX = getDetectionObjects().findAll {measurement(it, 'Area px^2' ) < 150} // filter cell size by pixel
removeObjects(PX, true)


def um = getDetectionObjects().findAll {measurement(it, 'Area µm^2' ) < 30} // filter cell size by um
removeObjects(um, true)


def circularity = getDetectionObjects().findAll {measurement(it, 'Circularity' ) < 0.3} //filter cell size by circularity
removeObjects(circularity, true)
