# ColorEditor
Basic graphical interface that allows the user to alter the colors
of one or more images.

## Usage
The image is opened and saved using the <b>File</b> menu, the <b>Settings</b> menu allows
to show or hide the histograms (the histograms update in real time, they can be
resized by dragging the side bar of the histogram panel with the mouse),
the <b>Mode</b> menu allows the user to alter the image in RGB or LMS color space
(Lab is still not implemented).<br><br>
It's possible to alter only a portion of the image by clicking on it
and drawing a rectangle. The saved image will have the following filename:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename-COEFF\[...]-SELECTION\[x772.0,y1761.0,W1103.0,H1040.0].jpg</i>
<br>
where <b>x</b> and <b>y</b> represent the start of the selection (with 0,0 being
the upper left corner), and <b>W</b> and <b>H</b> the Width and Height of the
selection.<br><br>
The user loads an image, then select one or more channels by clicking
on the buttons on the bottom part of the window. Using the UP and DOWN
keys on the keyboard the values of the selected channels are increased
or reduced.<br><br>
By pressing the "R" key on keyboard the image is reset, while keeping the "X"
key pressed shows the original unaltered image (and goes back to
the altered image once the key is released).<br><br>
The altered image can then be saved. It will be saved in the same path
of the original image.<br><br>
<details>
    <summary>RGB mode</summary>
    <h3>RGB Mode</h3>
    The name of the saved image contains information
    on the coefficients used to linear map each pixel's R, G and B values.<br>
    Eg:<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename-COEFF\[R0.5,G1.0,B1.0].jpg</i>
        <br>
        means that the R value of each pixel has been halved.
</details><br>
<details>
    <summary>LMS mode</summary>
    <h3>LMS mode</h3>
    If we were to simulate (for example) protanopia (lacking of L cones) just by setting to 0 all the L values in the image,
    we would obtain an inaccurate image, altering also blue and turning white into a greenish white;
    whereas making the L value a function of the M and S value, we can make the result independent of
    the original L value (simulating the lacking L cones in the retina), and also calculate two
    coefficients (with which we'll make a linear combination of S and M values) under the constraint
    that white should remain white and blue should remain blue (protanopes have no problems in seeing
    white or blue).<br>
    The same applies to deuteranopia (lacking of M cones) simulation, but by making the M value (and not the L value anymore)
    a function of L and S values.<br><br>
    Selecting M or L channel the user can alter the image's colors by decreasing the amount of chromatic
    information brought in by the M or L channel. For example, selecting the M channel and pressing the DOWN
    button on the keyboard, the M channel of the resulting image becomes gradually less dependent on the M
    channel of the input image and more dependent on the L and S channels.
    <br>
    In a future implementation the user could be allowed to choose color space, reference colors and gamma
    used to make all the calculations, right now they are fixed to
    standard sRGB.<br><br>
    The name of the saved image will be as follows:<br>
    Eg:<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename-COEFF\[KxVAL].jpg</i>
        <br>
    Where <i>Kx</i> refers to the channel used to alter the image (KM or KL for M or L channels)
    and VAL ranges between 0 and 1 (where 0 means that the output image is identical to the input image
    and 1 means that the M or L channels of the output image are calculated solely using the values
    of the other two channels).
</details>

<br><br>

## To do
- Allow the user to load multiple files and move between them
using the LEFT and RIGHT keys on the keyboard.
- Allow the user to choose color space, reference white and gamma
- (In LMS mode) allow the user to pick different reference colors (now fixed to sRGB white and blue)
- Improve performances
  
## Credits
Proprietary modules of this software are CC-BY Luca Armellin<br><br>
This software makes use of <a href="https://www.jfree.org/jfreechart/">JFreeChart</a>.<br>
JFreeChart is open source or, more specifically, free software. It is distributed under the terms of the
<a href="http://www.gnu.org/licenses/lgpl.html">GNU Lesser General Public Licence (LGPL)</a>,
which permits use in proprietary applications.