# ColorEditor
Basic graphical interface that allows the user to alter the colors
of one or more images.

## Usage
The user loads an image, then select one or more channels by clicking
on the buttons on the bottom part of the window. Using the UP and DOWN
keys on the keyboard the values of the selected channels are increased
or reduced.<br>
Pressing "R" key on keyboard the image is reset, while keeping the "X"
key pressed shows the original unaltered image (and goes back to
the altered image once the key is released).<br><br>
The altered image can then be saved. It will be saved in the same path
of the original image, the name of the saved image contains information
on the coefficients used to linear map each pixel's R, G and B values.<br>
Eg:<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename-COEFF\[R0.5,G1.0,B1.0].jpg</i>
    <br>
    means that the R value of each pixel has been halved.
<br><br>
It's possible to alter only a portion of the image by clicking on it
and drawing a rectangle. The saved image will have the following filename:
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename-COEFF\[R0.5,G1.0,B1.0]-SELECTION\[x772.0,y1761.0,W1103.0,H1040.0].jpg</i>
<br>
where <b>x</b> and <b>y</b> represent the start of the selection (with 0,0 being
the upper left corner), and <b>W</b> and <b>H</b> the Width and Height of the
selection.
<br>

## To do
- Allow the user to load multiple files and move between them
using the LEFT and RIGHT keys on the keyboard.
- Allow the user to alter chroma in uniform color spaces
(eg CIE L\*a\*b\*)