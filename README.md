#ColorEditor
Basic graphical interface that allows the user to alter the colors
of one or more images.

##Usage
The user loads an image, then select one or more channels by clicking
on the buttons on the bottom part of the window. Using the UP and DOWN
keys on the keyboard the values of the selected channels are increased
or reduced.<br><br>
The altered image can then be saved. It will be saved in the same path
of the original image, the name of the saved image contains information
on the coefficients used to linear map each pixel's R, G and B values.<br>
Eg:<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>imagename_\[R0.5-G1-B1].jpg</i>
    <br>
    means that the R value of each pixel has been halved.

##To do
- Allow the user to load multiple files and move between them
using the LEFT and RIGHT keys on the keyboard.
- Allow the user to alter only a portion of the image.
- Allow the user to alter chroma in uniform color spaces
(eg CIE L\*a\*b\*)