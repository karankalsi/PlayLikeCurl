
Introduction
============

It is an project for implementing 'Page Curl' similar to what we see in Google Play Books using OpenGL 1.0.
The source code is released under MIT License can be used in commercial or personal projects.

The main motive to work on 'PlayLikeCurl' is that its way more efficient than the traditional curl effect and is
better looking because it's way more smoother than the traditional curl.

In this project the below common sinusoidal graph equation is used to achieve the play like curl:-
<b>Asin(2πλx)</b><br /> <br /> 

Where,<br /> 
A = Amplitude (i.e. the elevation of curl we want).<br /> 
λ = Wavelength (i.e. the length of the curl we want).<br /> 
x = The X axis variable which will change as you move the page.<br /> 

In this i have drawn 3 pages on SurfaceView namely 'LeftPage' , 'CenterPage' and 'RightPage',
'CenterPage' is always visible and is responsible for 'right curl' animation while 'LeftPage' is responsible
for 'left curl' animation and 'RightPage' just stay static.<br /> 

For detecting gestures the default GestureDetector class has been used.




ToDo
====
* Need to add shadow below the page when it will move.

References
======================
* Harism Page Curl [https://github.com/harism/android_page_curl]
* WikiPedia [https://en.wikipedia.org/wiki/Sine]
* OpenGL Tutorials By Google [https://developer.android.com/guide/topics/graphics/opengl.html]
